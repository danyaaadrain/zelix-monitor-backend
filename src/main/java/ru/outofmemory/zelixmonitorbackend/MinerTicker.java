package ru.outofmemory.zelixmonitorbackend;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixmonitorbackend.miner.BaseMiner;
import ru.outofmemory.zelixmonitorbackend.services.MinerService;
import ru.outofmemory.zelixmonitorbackend.services.ReportService;

@Service
@RequiredArgsConstructor
public class MinerTicker {
    private final MinerService minerService;
    private final ReportService reportService;

    @Scheduled(fixedRate = 5000)
    public void tick() {
        minerService.getMiners().forEach(BaseMiner::tickMiner);
        reportService.buildReportAndSend();
    }
}
