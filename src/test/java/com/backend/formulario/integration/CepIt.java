package com.backend.formulario.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.backend.formulario.controllers.dtos.CepDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CepIt {

    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveRetornarEnderecoCorretoQuandoConsultarCep() {
        String url = "http://localhost:" + port + "/cep/localizarEndereco?cep=05716-070";
        
        ResponseEntity<CepDTO> response = restTemplate.getForEntity(url, CepDTO.class);
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("05716-070", response.getBody().cep());
        assertEquals("SP", response.getBody().estado());
        assertEquals("São Paulo", response.getBody().cidade());
        assertEquals("Vila Andrade", response.getBody().bairro());
        assertEquals("Rua Itacaiúna", response.getBody().endereco());
    }
    

}
