package ru.outofmemory.zelixmonitorbackend.dto.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixmonitorbackend.enums.MinerType;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MinerConfigDto {
    private UUID uuid;
    private String ip;
    private MinerType type;
    private String username;
    private String password;
}
