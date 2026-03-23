package ru.outofmemory.zelixmonitorbackend.dto.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ConfigDto {
    private UUID uuid;
    private String apiUrl;
    private String apiToken;
    private List<MinerConfigDto> miners = new ArrayList<>();
}
