package ru.outofmemory.zelixmonitorbackend.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.outofmemory.zelixmonitorbackend.Utils;
import ru.outofmemory.zelixmonitorbackend.dto.backend.ReportBackendDto;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.Map.Entry;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ConfigService configService;
    private final MinerService minerService;

    private final RestTemplate restTemplate;
    private final ReportBackendDto reportBackendDto = new ReportBackendDto();

    private Instant lastErrorLog = Instant.MIN;
    private static final Duration ERROR_LOG_COOLDOWN = Duration.ofMinutes(5);

    @PostConstruct
    private void buildReport() throws Exception {
        this.reportBackendDto.setMonitorUuid(configService.getConfig().getUuid());
        Entry<String, String> network = Utils.getLocalIpAndMac();
        this.reportBackendDto.setMonitorIp(network.getKey());
        this.reportBackendDto.setMonitorMac(network.getValue());
        this.reportBackendDto.setOsName(System.getProperty("os.name"));
    }

    public void buildReportAndSend() {
        this.reportBackendDto.getMiners().clear();
        this.reportBackendDto.setApiToken(configService.getConfig().getApiToken());
        this.reportBackendDto.setUptimeMillis(ManagementFactory.getRuntimeMXBean().getUptime());
        this.minerService.getMiners().forEach(miner -> {
            this.reportBackendDto.getMiners().add(miner.fetchAllData());
        });

        try {
            this.restTemplate.postForObject(
                    configService.getConfig().getApiUrl() + "/api/monitor/report",
                    reportBackendDto,
                    String.class
            );
        } catch (RestClientException e) {
            Instant now = Instant.now();
            if (Duration.between(lastErrorLog, now).compareTo(ERROR_LOG_COOLDOWN) > 0) {
                log.warn("Ошибка отправки отчёта: {}", e.getMessage());
                lastErrorLog = now;
            }
        }
    }
}
