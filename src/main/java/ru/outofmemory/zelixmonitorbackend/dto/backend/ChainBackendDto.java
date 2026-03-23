package ru.outofmemory.zelixmonitorbackend.dto.backend;

import lombok.Data;

import java.util.List;

@Data
public class ChainBackendDto {
    private int chainId;
    private int chipCount;
    private List<Integer> chipTemp;
    private String chipStatus;
    private int pcbMin;
    private int pcbMax;
    private long hwErrors;
}
