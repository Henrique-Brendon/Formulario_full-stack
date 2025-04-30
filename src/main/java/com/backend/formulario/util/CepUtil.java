package com.backend.formulario.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CepUtil implements Serializable {
    
    public static String consultarCep(String cep) throws IOException  {
        log.info("Iniciando consulta para o CEP: {}", cep);

        String encodedCep = URLEncoder.encode(cep, StandardCharsets.UTF_8.toString());
        String urlString = String.format("https://viacep.com.br/ws/%s/json", encodedCep);

        log.debug("URL da requisição: {}", urlString);

        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        int statusCode = conexao.getResponseCode();
        if(conexao.getResponseCode() != 200) {
            log.error("Falha na requisição para o CEP {}. Código de status HTTP: {}", cep, statusCode);
            throw new IOException("Falha na requisição ao consultar o CEP.");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder resposta = new StringBuilder();
            String inputLine;

            while((inputLine = br.readLine())!= null) {
                resposta.append(inputLine);
            }

            log.info("Consulta ao CEP {} concluída com sucesso.", cep);
            log.debug("Resposta da API: {}", resposta.length() > 300 ? resposta.substring(0, 300) + "..." : resposta.toString());
            
            return resposta.toString();

        } catch(IOException e) {
            log.error("Erro ao ler a resposta da API para o CEP {}: {}", cep, e.getMessage(), e);
            throw e;
        }
    }
}
