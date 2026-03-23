package ru.outofmemory.zelixmonitorbackend.dto.backend;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ReportBackendDto {
    private String apiToken;
    private UUID monitorUuid;
    private String monitorIp;
    private String monitorMac;
    private Long uptimeMillis;
    private String osName;
    private List<MinerBackendDto> miners = new ArrayList<>();
}
