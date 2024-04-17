package com.example.fipe.service;

import com.example.fipe.model.FipeEntity;
import com.example.fipe.repository.FipeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FipeService {

    private final FipeRepository fipeRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/carros/marcas/";

    @Autowired
    public FipeService(FipeRepository fipeRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.fipeRepository = fipeRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String consultarMarcas() {
        return consultarURL(BASE_URL);
    }

    public String consultarModelos(int id) {
        return consultarURL(BASE_URL + id + "/modelos");
    }

    public String consultarAno(int marca, int modelo) {
        return consultarURL(BASE_URL + marca + "/modelos/" + modelo + "/anos/");
    }

    public String consultarValor(int marca, int modelo, String ano) {
        return consultarURL(BASE_URL + marca + "/modelos/" + modelo + "/anos/" + ano);
    }

    private String consultarURL(String apiUrl) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String dados = responseEntity.getBody();
                salvarDados(dados);
                return dados;
            } else {
                return "Falha ao obter dados. Código do status: " + responseEntity.getStatusCode();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar a API: " + e.getMessage(), e);
        }
    }

    private void salvarDados(String dados) {
        try {
            JsonNode jsonNode = objectMapper.readTree(dados);
            for (JsonNode node : jsonNode) {
                String codigo = node.get("codigo").asText();
                String nome = node.get("nome").asText();

                FipeEntity fipeEntity = new FipeEntity();
                fipeEntity.setCodigo(codigo);
                fipeEntity.setNome(nome);

                fipeRepository.save(fipeEntity);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar os dados JSON: " + e.getMessage(), e);
        }
    }
}
