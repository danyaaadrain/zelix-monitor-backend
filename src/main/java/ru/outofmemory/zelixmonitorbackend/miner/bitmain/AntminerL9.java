package ru.outofmemory.zelixmonitorbackend.miner.bitmain;

import ru.outofmemory.zelixmonitorbackend.Utils;
import ru.outofmemory.zelixmonitorbackend.dto.ChainTempDto;
import ru.outofmemory.zelixmonitorbackend.dto.backend.ChainBackendDto;
import ru.outofmemory.zelixmonitorbackend.enums.MinerAlgo;
import ru.outofmemory.zelixmonitorbackend.dto.backend.MinerBackendDto;
import ru.outofmemory.zelixmonitorbackend.dto.backend.PoolBackendDto;
import ru.outofmemory.zelixmonitorbackend.dto.cgi.GetMinerConfResponse;
import ru.outofmemory.zelixmonitorbackend.dto.cgi.GetSystemInfoResponse;
import ru.outofmemory.zelixmonitorbackend.dto.config.MinerConfigDto;
import ru.outofmemory.zelixmonitorbackend.dto.socket.AntminerPoolsResponse;
import ru.outofmemory.zelixmonitorbackend.dto.socket.AntminerStatsResponse;
import ru.outofmemory.zelixmonitorbackend.miner.BaseMiner;
import ru.outofmemory.zelixmonitorbackend.network.RequestCgi;
import ru.outofmemory.zelixmonitorbackend.network.RequestHttp;
import ru.outofmemory.zelixmonitorbackend.network.RequestSocket;

import java.util.Iterator;
import java.util.List;

public class AntminerL9 extends BaseMiner {

    private final RequestSocket requestSocket;
    private final RequestHttp requestHttp;
    private final RequestCgi requestCgi;

    private AntminerStatsResponse stats_response;

    public AntminerL9(MinerConfigDto data, RequestSocket requestSocket, RequestCgi requestCgi, RequestHttp requestHttp) {
        super(data);
        this.requestSocket = requestSocket;
        this.requestHttp = requestHttp;
        this.requestCgi = requestCgi;
    }

    @Override
    protected void updateMiner() {
        if (!this.isLoggedIn()) {
            return;
        }
        GetSystemInfoResponse response = requestCgi.fetchJson(this.getIp(), "get_system_info", this.getWebCredentials(), GetSystemInfoResponse.class);
        if (this.isLoggedIn()) {
            this.setMac(response.mac);
            this.setSn(response.sn);
        }
    }

    @Override
    public void checkAuth() {
        try {
            requestCgi.fetchJson(this.getIp(), "get_system_info", this.getWebCredentials(), GetSystemInfoResponse.class);
            this.setLoggedIn(true);
        } catch (Exception e) {
            this.setLoggedIn(false);
        }
    }

    @Override
    protected void getAlgo(MinerBackendDto report) {
        report.setAlgo(MinerAlgo.LTC);
    }

    @Override
    public void fetchCommon(MinerBackendDto report) {
        report.setMinerInfo(this);
        this.stats_response = requestSocket.fetchJson(this.getIp(), "stats", true, AntminerStatsResponse.class);
        var stats = stats_response.STATS.getFirst();

        report.setRate(stats.rate_5s);
        report.setRateAvg(stats.rate_avg);
        report.setRateUnit(stats.rate_unit);
        report.setFans(stats.fan);
        report.setPower(stats.power);
        report.setUptime(stats.elapsed);
    }

    @Override
    public void fetchChains(MinerBackendDto report) {
        List<ChainTempDto> chainTemps = Utils.parseMinerTemp(requestHttp.getChipsTemp(this.getIp()));
        Iterator<ChainTempDto> iterator = chainTemps.iterator();

        this.stats_response.STATS.getFirst().chain.forEach(chain -> {
            ChainBackendDto chainBackend = new ChainBackendDto();

            chainBackend.setChainId(chain.index);
            chainBackend.setChipCount(chain.asic_num);
            chainBackend.setChipTemp(iterator.next().getChipTemps());
            chainBackend.setChipStatus(chain.asic);
            chainBackend.setPcbMin(chain.temp_pcb[0]);
            chainBackend.setPcbMax(chain.temp_pcb[1]);
            chainBackend.setHwErrors(chain.hw);

            report.getChains().add(chainBackend);
        });
    }

    @Override
    public void fetchPools(MinerBackendDto report) {
        AntminerPoolsResponse poolsResponse = requestSocket.fetchJson(this.getIp(), "pools", true, AntminerPoolsResponse.class);
        GetMinerConfResponse minerConfResponse = requestCgi.fetchJson(this.getIp(), "get_miner_conf", this.getWebCredentials(), GetMinerConfResponse.class);

        poolsResponse.POOLS.forEach(pool -> {
            PoolBackendDto poolBackend = new PoolBackendDto();

            poolBackend.setPoolId(pool.index);
            poolBackend.setPriority(pool.priority);
            poolBackend.setUrl(pool.url);
            poolBackend.setUsername(pool.user);
            poolBackend.setPassword(minerConfResponse.pools.get(pool.index).pass);
            poolBackend.setStatus(pool.status);
            poolBackend.setAccepted(pool.accepted);
            poolBackend.setRejected(pool.rejected);
            poolBackend.setStale(pool.stale);
            poolBackend.setLastShareTime(pool.lstime);
            poolBackend.setDiff(pool.diff.isEmpty() ? 0D : Double.parseDouble(pool.diff));

            report.getPools().add(poolBackend);
        });
    }
}