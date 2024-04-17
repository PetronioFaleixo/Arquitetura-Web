package com.example.clima.service;

import com.example.clima.model.ClimaEntity;
import com.example.clima.repository.ClimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ClimaService {

    private final ClimaRepository climaRepository;

    @Autowired
    public ClimaService(ClimaRepository climaRepository) {
        this.climaRepository = climaRepository;
    }

    public String preverTempo() {
        String apiURL = "https://apiadvisor.climatempo.com.br/api/v1/anl/synoptic/locale/BR?token=12b3e1a01ec62614cd4a9ac2c939f981";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiURL, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Código de status: " + responseEntity.getStatusCode());
            String dadosMeteorologicos = responseEntity.getBody();
            System.out.println(dadosMeteorologicos);
            salvarDadosMeteorologicos(dadosMeteorologicos);
            return dadosMeteorologicos;
        } else {
            return "Falha ao obter dados meteorológicos. Código de status: " + responseEntity.getStatusCode();
        }
    }

    private void salvarDadosMeteorologicos(String dadosMeteorologicos) {
        try {
            List<ClimaEntity> climaEntities = parseJsonParaClimaEntities(dadosMeteorologicos);
            climaRepository.saveAll(climaEntities);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar dados meteorológicos no banco de dados.", e);
        }
    }

}
