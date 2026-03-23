package ru.outofmemory.zelixmonitorbackend.dto.backend;

import lombok.Data;
import ru.outofmemory.zelixmonitorbackend.enums.MinerType;
import ru.outofmemory.zelixmonitorbackend.enums.MinerAlgo;
import ru.outofmemory.zelixmonitorbackend.miner.BaseMiner;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class MinerBackendDto {
    private UUID uuid;
    private String ip;
    private String mac;
    private MinerType type;
    private String sn;
    private MinerAlgo algo;

    private double rate;
    private double rateAvg;
    private String rateUnit;
    private int[] fans = new int[4];
    private int power;
    private long uptime;

    private ArrayList<ChainBackendDto> chains = new ArrayList<>();
    private ArrayList<PoolBackendDto> pools = new ArrayList<>();

    public void setMinerInfo(BaseMiner miner) {
        this.uuid = miner.getUuid();
        this.ip = miner.getIp();
        this.mac = miner.getMac();
        this.type = miner.getType();
        this.sn = miner.getSn();
    }
}
