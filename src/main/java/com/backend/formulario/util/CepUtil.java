package com.backend.formulario.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.backend.formulario.util.exceptions.CepFormatoInvalidoException;
import com.backend.formulario.util.exceptions.CepNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CepUtil implements Serializable {

    public static String consultarCep(String cep) throws IOException {
        log.info("Iniciando consulta para o CEP: {}", cep);
    
        String encodedCep = URLEncoder.encode(cep, StandardCharsets.UTF_8.toString());
        String urlString = String.format("https://viacep.com.br/ws/%s/json", encodedCep);
    
        log.debug("URL da requisição: {}", urlString);
    
        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setRequestProperty("Accept", "application/json");
    
        int statusCode = conexao.getResponseCode();

        if (statusCode == 400) {
            log.error("CEP com formato inválido: {}", cep);
            throw new CepFormatoInvalidoException(cep);
        }

    
        if (statusCode != 200) {
            log.error("Erro ao consultar CEP {}. HTTP status: {}", cep, statusCode);
            throw new IOException("Erro ao consultar o CEP.");
        }
    
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder resposta = new StringBuilder();
            String inputLine;
    
            while ((inputLine = br.readLine()) != null) {
                resposta.append(inputLine);
            }
    
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(resposta.toString());
    
            if (rootNode.has("erro") && rootNode.get("erro").asBoolean()) {
                log.warn("CEP não encontrado: {}", cep);
                throw new CepNotFoundException(cep);
            }
    
            return resposta.toString();
        }
    }

}
