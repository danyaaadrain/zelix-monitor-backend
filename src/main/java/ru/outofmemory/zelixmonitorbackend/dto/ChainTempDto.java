package ru.outofmemory.zelixmonitorbackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChainTempDto {
    private int chainId;
    private List<Integer> chipTemps = new ArrayList<>();
}
