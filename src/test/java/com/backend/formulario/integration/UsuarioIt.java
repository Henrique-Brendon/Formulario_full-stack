package com.backend.formulario.integration;

import static com.backend.formulario.common.CepConstrants.*;
import static com.backend.formulario.common.UsuarioConstrants.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.backend.formulario.controllers.dtos.UsuarioWithCepInfoDTOs;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioIt {
    
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void salvarUsuario_ComDadosValidos_EntaoRetornarAJuncaoDeDtos() {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, CEP_INFO_DTO);

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        ResponseEntity<UsuarioWithCepInfoDTOs> response = restTemplate.postForEntity(
            url, payload, UsuarioWithCepInfoDTOs.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        assertEquals("Laura", response.getBody().usuarioDTO().nome());
        assertEquals("laura@email.com", response.getBody().usuarioDTO().email());
        assertEquals("senha123", response.getBody().usuarioDTO().senha());
        
        assertEquals("05716-070", response.getBody().cepInfoDTO().cepDTO().cep());
        assertEquals("SP", response.getBody().cepInfoDTO().cepDTO().estado());
        assertEquals("São Paulo", response.getBody().cepInfoDTO().cepDTO().cidade());
        assertEquals("Vila Andrade", response.getBody().cepInfoDTO().cepDTO().bairro());
        assertEquals("Rua Itacaiúna", response.getBody().cepInfoDTO().cepDTO().endereco());
        assertEquals("22", response.getBody().cepInfoDTO().numeroCasa());
    }

        @Test
    void deveRetornar400_QuandoUsuarioDTOForNulo() throws Exception {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(null, CEP_INFO_DTO);

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payload), headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            String.class
        );
        

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"status\":400"));
        assertTrue(response.getBody().contains("\"message\":\"O objeto 'UsuarioDTO' está nulo.\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }

    @Test
    void deveRetornar400_QuandoCepInfoDTOForNulo() throws Exception {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, null);

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
            objectMapper.writeValueAsString(payload),
            headers
        );

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"status\":400"));
        assertTrue(response.getBody().contains("\"message\":\"O objeto 'CepInfoDTO' está nulo.\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }
}
