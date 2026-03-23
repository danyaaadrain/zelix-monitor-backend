package ru.outofmemory.zelixmonitorbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MinerResponseDto {
    private UUID uuid;
    private String ip;
    private String mac;
    private String sn;
    private String minerType;
    private Boolean online;
    private Boolean loggedIn;
}
