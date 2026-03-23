package ru.outofmemory.zelixmonitorbackend.services;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixmonitorbackend.dto.AddMinerRequestDto;
import ru.outofmemory.zelixmonitorbackend.dto.MinerSettingsRequestDto;
import ru.outofmemory.zelixmonitorbackend.dto.config.MinerConfigDto;
import ru.outofmemory.zelixmonitorbackend.miner.BaseMiner;
import ru.outofmemory.zelixmonitorbackend.miner.MinerFactory;
import ru.outofmemory.zelixmonitorbackend.miner.WebCredentials;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class MinerService {
    private final ConfigService configService;
    private final MinerFactory minerFactory;

    @Getter
    private final List<BaseMiner> miners = new ArrayList<>();

    @PostConstruct
    public void loadMiners() {
        log.info("Загрузка устройств из конфига");
        int activeDevices = 0;
        for (MinerConfigDto minerConfig : configService.getConfig().getMiners()) {
            if (this.checkOnline(minerConfig.getIp())) {
                activeDevices++;
            }
            this.miners.add(minerFactory.createMinerFromConfig(minerConfig));
        }

        log.info("Загрузка завершена, устройств онлайн: {}", activeDevices);
    }

    public boolean checkOnline(String ip) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 4028), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void addMiner(AddMinerRequestDto request) {
        this.miners.stream().findFirst().filter(m -> m.getIp().equals(request.getIp())).ifPresent(m -> {
            throw new RuntimeException("Майнер с указанным IP уже создан");
        });
        if(!this.checkOnline(request.getIp())) {
            throw new RuntimeException("Майнер с указанным IP оффлайн или не существует");
        }
        MinerConfigDto minerConfigDto;
        try {
            minerConfigDto = minerFactory.createConfigFromDto(request);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось определить тип майнера");
        }
        configService.addMiner(minerConfigDto);
        BaseMiner baseMiner = minerFactory.createMinerFromConfig(minerConfigDto);
        baseMiner.checkAuth();
        this.miners.add(baseMiner);
    }

    public void changeSettings(UUID uuid, MinerSettingsRequestDto request) {
        if (request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
            throw new RuntimeException("Имя пользователя и пароль не могут быть пустыми");
        }
        BaseMiner miner = this.miners.stream()
                .filter(m -> m.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Майнер не найден"));

        MinerConfigDto configMiner = configService.getConfig().getMiners().stream()
                .filter(m -> m.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Майнер не найден"));

        WebCredentials creds = new WebCredentials(request.getUsername(), request.getPassword());

        miner.setWebCredentials(creds);
        configMiner.setUsername(request.getUsername());
        configMiner.setPassword(request.getPassword());

        configService.writeConfig();
    }

    public void deleteMiners(List<UUID> uuids) {
        this.miners.removeIf(m -> uuids.contains(m.getUuid()));
        configService.getConfig().getMiners().removeIf(m -> uuids.contains(m.getUuid()));
        configService.writeConfig();
    }
}

