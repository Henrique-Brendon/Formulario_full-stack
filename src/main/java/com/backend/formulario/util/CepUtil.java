package com.backend.formulario.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CepUtil implements Serializable {
    
    public static String consultarCep(String cep) throws IOException  {
        String encodedCep = URLEncoder.encode(cep, StandardCharsets.UTF_8.toString());
        String urlString = String.format("https://viacep.com.br/ws/%s/json", encodedCep);

        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        if(conexao.getResponseCode() != 200) {
            throw new IOException("Falha na requisição ao consultar o CEP.");
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()))) {
            StringBuilder respota = new StringBuilder();
            String inputLine;

            while((inputLine = br.readLine())!= null) {
                respota.append(inputLine);
            }
            return respota.toString();
        }
    }
}
