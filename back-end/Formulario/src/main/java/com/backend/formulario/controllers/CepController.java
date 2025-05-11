package com.backend.formulario.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.services.CepService;
import com.backend.formulario.util.CepUtil;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/cep")
public class CepController {
    
    @Autowired
    private CepService cepService;

    @Autowired
    private CepUtil cepUtil;

    @GetMapping("/localizarEndereco")
    public ResponseEntity<CepDTO> localizarEndereco(@RequestParam String cep) throws IOException, InterruptedException {
        CepDTO resposta = cepService.verificarEndereco(cepUtil.consultarCep(cep));
        return ResponseEntity.status(HttpStatus.OK).body(resposta);
    }
    
}
