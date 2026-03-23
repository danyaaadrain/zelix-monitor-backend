package ru.outofmemory.zelixmonitorbackend.dto;


import lombok.Data;

@Data
public class AddMinerRequestDto {
    private String ip;
    private String username;
    private String password;
}
