package ru.outofmemory.zelixmonitorbackend.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MinerType {
    ANTMINER_L9("Antminer L9");

    private final String name;

    MinerType(String name) {
        this.name = name;
    }

    public static MinerType fromName(String name) {
        return Arrays.stream(values())
                .filter(t -> t.name.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown miner type: " + name));
    }
}
