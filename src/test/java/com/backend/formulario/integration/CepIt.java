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

    @Test
    void deveRetornar404_QuandoCepNaoEncontrado() {
        String cepInexistente = "00000-000";
        String url = "http://localhost:" + port + "/cep/localizarEndereco?cep=" + cepInexistente;
    
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"status\":404"));
        assertTrue(response.getBody().contains("\"message\":\"CEP não encontrado: " + cepInexistente + "\""));
        assertTrue(response.getBody().contains("\"path\":\"/cep/localizarEndereco\""));
    }
    

    @Test
    void deveRetornar400_QuandoFormatoDeCepForInvalido() {
        String cepInvalido = "000000-000";
        String url = "http://localhost:" + port + "/cep/localizarEndereco?cep=" + cepInvalido;
    
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"status\":400"));
        assertTrue(response.getBody().contains("\"message\":\"Formato de CEP inválido: " + cepInvalido + "\""));
        assertTrue(response.getBody().contains("\"path\":\"/cep/localizarEndereco\""));
    }

}
