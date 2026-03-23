package ru.outofmemory.zelixmonitorbackend.miner;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.outofmemory.zelixmonitorbackend.dto.AddMinerRequestDto;
import ru.outofmemory.zelixmonitorbackend.dto.config.MinerConfigDto;
import ru.outofmemory.zelixmonitorbackend.dto.socket.AntminerVersionResponse;
import ru.outofmemory.zelixmonitorbackend.enums.MinerType;
import ru.outofmemory.zelixmonitorbackend.miner.bitmain.AntminerL9;
import ru.outofmemory.zelixmonitorbackend.network.RequestCgi;
import ru.outofmemory.zelixmonitorbackend.network.RequestHttp;
import ru.outofmemory.zelixmonitorbackend.network.RequestSocket;

import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class MinerFactory {
    private final RequestSocket requestSocket;
    private final RequestCgi requestCgi;
    private final RequestHttp requestHttp;

    public BaseMiner createMinerFromConfig(MinerConfigDto miner) {
        return switch (miner.getType()) {
            case ANTMINER_L9 -> new AntminerL9(miner, requestSocket, requestCgi, requestHttp);
        };
    }

    public MinerConfigDto createConfigFromDto(AddMinerRequestDto request) {
        MinerConfigDto minerConfigDto = new MinerConfigDto();
        AntminerVersionResponse version = requestSocket.fetchJson(request.getIp(), "version", false, AntminerVersionResponse.class);

        minerConfigDto.setUuid(UUID.randomUUID());
        minerConfigDto.setIp(request.getIp());
        minerConfigDto.setType(MinerType.fromName(version.getType()));
        minerConfigDto.setUsername(request.getUsername().isEmpty() ? WebCredentials.ANTMINER.getUsername() : request.getUsername());
        minerConfigDto.setPassword(request.getPassword().isEmpty() ? WebCredentials.ANTMINER.getPassword() : request.getPassword());

        return minerConfigDto;
    }


}
