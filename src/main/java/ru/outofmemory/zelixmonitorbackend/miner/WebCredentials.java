package ru.outofmemory.zelixmonitorbackend.miner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebCredentials {
    private String username;
    private String password;

    public static WebCredentials ANTMINER;

    static {
        ANTMINER = new WebCredentials("root", "root");
    }
}
