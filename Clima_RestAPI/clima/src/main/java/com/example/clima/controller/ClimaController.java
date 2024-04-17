package com.example.clima.controller;

import com.example.clima.model.ClimaEntity;
import com.example.clima.service.ClimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clima")
public class ClimaController {

    private final ClimaService climaService;

    @Autowired
    public ClimaController(ClimaService climaService) {
        this.climaService = climaService;
    }

    @GetMapping("/previsao")
    public ResponseEntity<String> preverTempo() {
        String previsao = climaService.preverTempo();
        return ResponseEntity.ok(previsao);
    }

    @PostMapping("/inserir")
    public ResponseEntity<ClimaEntity> inserir(@RequestBody ClimaEntity climaEntity) {
        ClimaEntity novoClima = climaService.inserir(climaEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoClima);
    }
}
