package ru.outofmemory.zelixmonitorbackend.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixmonitorbackend.dto.settings.SettingsDto;
import ru.outofmemory.zelixmonitorbackend.services.ConfigService;

@Log4j2
@RestController
@RequestMapping("api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final ConfigService configService;

    @GetMapping
    public SettingsDto getSettings() {
        SettingsDto response = new SettingsDto();
        response.setApiUrl(configService.getConfig().getApiUrl());
        response.setApiToken(configService.getConfig().getApiToken());
        return response;
    }

    @PatchMapping
    public ResponseEntity<?> updateSettings(@RequestBody SettingsDto settingsDto) {
        configService.writeApiUrl(settingsDto.getApiUrl());
        configService.writeApiToken(settingsDto.getApiToken());
        return ResponseEntity.ok().build();
    }
}
