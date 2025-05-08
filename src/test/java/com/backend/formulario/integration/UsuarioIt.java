package com.backend.formulario.integration;

import static com.backend.formulario.common.CepConstrants.*;
import static com.backend.formulario.common.UsuarioConstrants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.controllers.dtos.CepInfoDTO;
import com.backend.formulario.controllers.dtos.UsuarioDTO;
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

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerObjetosNulos")
    void deveRetornar400_QuandoObjetosDTOForemNulos_IT(UsuarioDTO usuarioDTO, CepInfoDTO cepInfoDTO, String campoNulo) throws Exception {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(usuarioDTO, cepInfoDTO);
    
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
        assertTrue(response.getBody().contains("\"message\":\"O objeto '" + campoNulo + "' está nulo.\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }    

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposUsuarioVazio")
    void deveRetornar400_QuandoCamposObrigatoriosDoUsuarioEstiveremVazios(String nome, Instant dataNascimento, String email, String senha, String mensagemEsperada) throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, dataNascimento, email, senha);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(usuarioDTO, CEP_INFO_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
            objectMapper.writeValueAsString(payload),
            headers
        );

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        ResponseEntity<String> response = restTemplate.exchange(
            url, HttpMethod.POST, request, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"message\":\"" + mensagemEsperada + "\""));
        assertTrue(response.getBody().contains("\"error\":\"Campo obrigatório\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposUsuarioNull")
    void deveRetornar400_QuandoCamposObrigatoriosDoUsuarioEstiveremNull(String nome, Instant dataNascimento, String email, String senha, String mensagemEsperada) throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, dataNascimento, email, senha);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(usuarioDTO, CEP_INFO_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
            objectMapper.writeValueAsString(payload),
            headers
        );

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        ResponseEntity<String> response = restTemplate.exchange(
            url, HttpMethod.POST, request, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"message\":\"" + mensagemEsperada + "\""));
        assertTrue(response.getBody().contains("\"error\":\"Campo obrigatório\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposCepVazios")
    void deveRetornar400_QuandoCamposObrigatoriosDoCepForemVazios(
            String cep, String estado, String cidade, String bairro, String endereco, String numeroCasa, String mensagemEsperada) throws Exception {

        CepDTO cepDTO = new CepDTO(cep, estado, cidade, bairro, endereco);
        CepInfoDTO cepInfoDTO = new CepInfoDTO(cepDTO, numeroCasa);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, cepInfoDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
            objectMapper.writeValueAsString(payload),
            headers
        );

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        ResponseEntity<String> response = restTemplate.exchange(
            url, HttpMethod.POST, request, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"message\":\"" + mensagemEsperada + "\""));
        assertTrue(response.getBody().contains("\"error\":\"Campo obrigatório\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposCepNull")
    void deveRetornar400_QuandoCamposObrigatoriosDoCepForemNull(
            String cep, String estado, String cidade, String bairro, String endereco, String numeroCasa, String mensagemEsperada) throws Exception {

        CepDTO cepDTO = new CepDTO(cep, estado, cidade, bairro, endereco);
        CepInfoDTO cepInfoDTO = new CepInfoDTO(cepDTO, numeroCasa);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, cepInfoDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(
            objectMapper.writeValueAsString(payload),
            headers
        );

        String url = "http://localhost:" + port + "/usuario/inserirUsuario";

        ResponseEntity<String> response = restTemplate.exchange(
            url, HttpMethod.POST, request, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"message\":\"" + mensagemEsperada + "\""));
        assertTrue(response.getBody().contains("\"error\":\"Campo obrigatório\""));
        assertTrue(response.getBody().contains("\"path\":\"/usuario/inserirUsuario\""));
    }


}
