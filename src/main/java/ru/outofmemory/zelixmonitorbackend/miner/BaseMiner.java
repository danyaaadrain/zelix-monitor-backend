package ru.outofmemory.zelixmonitorbackend.miner;

import lombok.Getter;
import lombok.Setter;
import ru.outofmemory.zelixmonitorbackend.dto.backend.MinerBackendDto;
import ru.outofmemory.zelixmonitorbackend.dto.config.MinerConfigDto;
import ru.outofmemory.zelixmonitorbackend.enums.MinerType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseMiner {
    private UUID uuid;
    private String ip;
    private MinerType type;
    private WebCredentials webCredentials;

    private String mac;
    private String sn;

    private boolean isOnline;
    private boolean isLoggedIn;

    public BaseMiner(MinerConfigDto data) {
        this.uuid = data.getUuid();
        this.ip = data.getIp();
        this.type = data.getType();
        this.webCredentials = new WebCredentials(data.getUsername(), data.getPassword());
        this.mac = "EMPTY";
        this.sn = "EMPTY";
        this.isOnline = false;
        this.isLoggedIn = false;
        this.checkOnline();
    }

    public void checkOnline() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 4028), 1000);
            this.isOnline = true;
        } catch (IOException e) {
            this.isOnline = false;
        }
    }

    public MinerBackendDto fetchAllData() {
        MinerBackendDto report = new MinerBackendDto();
        this.getAlgo(report);
        try {
            this.fetchCommon(report);
            this.fetchChains(report);
            this.fetchPools(report);
        } catch (Exception ignored) {
        }
        return report;
    }

    public void tickMiner() {
        this.checkOnline();
        if (this.isOnline) {
            this.checkAuth();
            this.updateMiner();
        }
    }

    protected abstract void updateMiner();

    public abstract void checkAuth();

    protected abstract void getAlgo(MinerBackendDto report);

    protected abstract void fetchCommon(MinerBackendDto report);

    protected abstract void fetchChains(MinerBackendDto report);

    protected abstract void fetchPools(MinerBackendDto report);
}
