package com.backend.formulario.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.backend.formulario.controllers.dtos.CepDTO;

@Service
public class CepService {
    
    public CepDTO verificarEndereco(String endereco) {
        if(parse(endereco) == null) {
            throw new NullPointerException("O objeto não pode estar nulo.");
        }

        return parse(endereco);
    }

    private CepDTO parse(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return new CepDTO(
            jsonObject.getString("cep"),
            jsonObject.getString("uf"),
            jsonObject.getString("localidade"),
            jsonObject.getString("bairro"),
            jsonObject.getString("logradouro")
        );
    }
}
