package ru.outofmemory.zelixmonitorbackend.services;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixmonitorbackend.dto.config.ConfigDto;
import ru.outofmemory.zelixmonitorbackend.dto.config.MinerConfigDto;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ConfigService {
    private final ObjectMapper objectMapper;
    private final Path path = Path.of("settings.json");

    @Getter
    @Setter
    private ConfigDto config;

    @PostConstruct
    public void initConfig() {
        if (Files.exists(path)) {
            log.info("Загрузка конфига из: {}", path);
        } else {
            log.info("Создание нового конфига: {}", path);
            config = new ConfigDto();
            config.setApiUrl("http://192.168.1.10");
            if (config.getUuid() == null) {
                config.setUuid(UUID.randomUUID());
            }
            this.writeConfig();
            return;
        }
        config = objectMapper.readValue(path.toFile(), ConfigDto.class);
        if (config.getUuid() == null) {
            config.setUuid(UUID.randomUUID());
            this.writeConfig();
        }
    }

    public void writeConfig() {
        objectMapper.writeValue(path.toFile(), config);
    }

    public void writeApiUrl(String url) {
        this.config.setApiUrl(url);
        this.writeConfig();
    }

    public void writeApiToken(String key) {
        this.config.setApiToken(key);
        this.writeConfig();
    }

    public void addMiner(MinerConfigDto miner){
        this.config.getMiners().add(miner);
        this.writeConfig();
    }

    public void delMiner(UUID id) {
        this.config.getMiners().removeIf(configMiner -> id.equals(configMiner.getUuid()));
        this.writeConfig();
    }
}
