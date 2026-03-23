package ru.outofmemory.zelixmonitorbackend.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixmonitorbackend.dto.AddMinerRequestDto;
import ru.outofmemory.zelixmonitorbackend.dto.MinerMapper;
import ru.outofmemory.zelixmonitorbackend.dto.MinerResponseDto;
import ru.outofmemory.zelixmonitorbackend.dto.MinerSettingsRequestDto;
import ru.outofmemory.zelixmonitorbackend.services.MinerService;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("api/miners")
@RequiredArgsConstructor
public class MinerController {
    private final MinerService minerService;
    private final MinerMapper minerMapper;

    @PostMapping
    public ResponseEntity<?> addMiner(@RequestBody AddMinerRequestDto request) {
        minerService.addMiner(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<MinerResponseDto> getMiners() {
        return minerService.getMiners().stream().map(minerMapper::toMinerResponseDto).toList();
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<?> changeSettings(@PathVariable UUID uuid, @RequestBody MinerSettingsRequestDto request) {
        minerService.changeSettings(uuid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMiner(@RequestBody List<UUID> uuids) {
        minerService.deleteMiners(uuids);
        return ResponseEntity.ok().build();
    }
}
