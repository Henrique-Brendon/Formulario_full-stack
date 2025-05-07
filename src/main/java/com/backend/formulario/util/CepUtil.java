package com.backend.formulario.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.backend.formulario.util.exceptions.CepFormatoInvalidoException;
import com.backend.formulario.util.exceptions.CepNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CepUtil implements Serializable {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CepUtil(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String consultarCep(String cep) throws IOException, InterruptedException  {
        log.info("Iniciando consulta para o CEP: {}", cep);

        String encodedCep = URLEncoder.encode(cep, StandardCharsets.UTF_8.toString());
        String url = String.format("https://viacep.com.br/ws/%s/json", encodedCep);

        log.debug("URL da requisição: {}", url);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> resposta = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = resposta.statusCode();
        if(resposta.statusCode() == 400) {
            log.error("CEP com formato inválido: {}", cep);
            throw new CepFormatoInvalidoException(cep);
        }

        if(resposta.statusCode() != 200) {
            log.error("Erro ao consultar CEP {}. HTTP status: {}", cep, statusCode);
            throw new IOException("Erro ao consultar cep");
        }

        JsonNode rootNode = objectMapper.readTree(resposta.body());

        if(rootNode.has("erro") && rootNode.get("erro").asBoolean()) {
            throw new CepNotFoundException(cep);
        }

        return resposta.body();
    }

}
