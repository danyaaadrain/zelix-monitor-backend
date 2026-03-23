package ru.outofmemory.zelixmonitorbackend.dto.backend;

import lombok.Data;

@Data
public class PoolBackendDto {
    private int poolId;
    private int priority;
    private String url;
    private String username;
    private String password;
    private String status;
    private long accepted;
    private long rejected;
    private long stale;
    private String lastShareTime;
    private double diff;
}
