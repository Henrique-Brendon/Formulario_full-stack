package com.backend.formulario.controllers;

import static com.backend.formulario.common.UsuarioConstrants.USUARIO_DTO;
import static com.backend.formulario.common.CepConstrants.CEP_INFO_DTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.stream.Stream;

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.controllers.dtos.CepInfoDTO;
import com.backend.formulario.controllers.dtos.UsuarioDTO;
import com.backend.formulario.controllers.dtos.UsuarioWithCepInfoDTOs;
import com.backend.formulario.services.UsuarioService;
import com.backend.formulario.util.exceptions.CampoObrigatorioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;


    @Test
    void salvarUsuario_ComDadosValidos_EntaoRetornaAJuncaoDeDtos() throws JsonProcessingException, Exception {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, CEP_INFO_DTO);
    
        mockMvc.perform(post("/usuario/inserirUsuario")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.usuarioDTO.nome").value("Laura"))
            .andExpect(jsonPath("$.usuarioDTO.email").value("laura@email.com"))
            .andExpect(jsonPath("$.usuarioDTO.senha").value("senha123"))
            .andExpect(jsonPath("$.cepInfoDTO.cepDTO.cep").value("05716-070"))
            .andExpect(jsonPath("$.cepInfoDTO.cepDTO.estado").value("SP"))
            .andExpect(jsonPath("$.cepInfoDTO.cepDTO.cidade").value("São Paulo"))
            .andExpect(jsonPath("$.cepInfoDTO.cepDTO.bairro").value("Vila Andrade"))
            .andExpect(jsonPath("$.cepInfoDTO.cepDTO.endereco").value("Rua Itacaiúna"))
            .andExpect(jsonPath("$.cepInfoDTO.numeroCasa").value("22"));
    }

    @Test
    void deveRetornar400_QuandoUsuarioDTOForNulo() throws Exception {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(null, CEP_INFO_DTO);
    
        doThrow(new NullPointerException("UsuarioDTO"))
            .when(usuarioService).inserir(isNull(), any(CepInfoDTO.class));
    
        mockMvc.perform(post("/usuario/inserirUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Null Pointer Exception"))
            .andExpect(jsonPath("$.message").value("O objeto 'UsuarioDTO' está nulo."))
            .andExpect(jsonPath("$.path").value("/usuario/inserirUsuario"));
    }

    @Test
    void deveRetornar400_QuandoCepInfoDTOForNulo() throws Exception {
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, null);
    
        doThrow(new NullPointerException("cepInfoDTO"))
            .when(usuarioService).inserir(any(UsuarioDTO.class), isNull());
    
        mockMvc.perform(post("/usuario/inserirUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Null Pointer Exception"))
            .andExpect(jsonPath("$.message").value("O objeto 'CepInfoDTO' está nulo."))
            .andExpect(jsonPath("$.path").value("/usuario/inserirUsuario"));
    }
    
    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposUsuarioVazio")
    void deveRetornar400_QuandoCamposObrigatoriosEstiveremVazios(String nome, Instant dataNascimento, String email, String senha, String mensagemEsperada) throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, dataNascimento, email, senha);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(usuarioDTO, CEP_INFO_DTO);

        doThrow(new CampoObrigatorioException(mensagemEsperada))
            .when(usuarioService).inserir(any(UsuarioDTO.class), any(CepInfoDTO.class));

        mockMvc.perform(post("/usuario/inserirUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(mensagemEsperada))
            .andExpect(jsonPath("$.error").value("Campo obrigatório"))
            .andExpect(jsonPath("$.path").value("/usuario/inserirUsuario"));
    }

    private static Stream<Arguments> fornecerUsuarioComCamposInvalidos() {
        return Stream.of(
            Arguments.of("", Instant.now(), "email@example.com", "senha123", "O campo 'nome' não pode ser vazio."),
            Arguments.of("João", Instant.now(), "", "senha123", "O campo 'email' não pode ser vazio."),
            Arguments.of("João", Instant.now(), "email@example.com", "", "O campo 'senha' não pode ser vazio.")
        );
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposUsuarioNull")
    void deveRetornar400_QuandoCamposObrigatoriosEstiveremNull(
            String nome, Instant dataNascimento, String email, String senha, String mensagemEsperada) throws Exception {

        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, dataNascimento, email, senha);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(usuarioDTO, CEP_INFO_DTO);

        doThrow(new CampoObrigatorioException(mensagemEsperada))
            .when(usuarioService).inserir(any(UsuarioDTO.class), any(CepInfoDTO.class));

        mockMvc.perform(post("/usuario/inserirUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(mensagemEsperada))
            .andExpect(jsonPath("$.error").value("Campo obrigatório"))
            .andExpect(jsonPath("$.path").value("/usuario/inserirUsuario"));
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposCepVazios")
    void deveRetornar400_QuandoCamposObrigatoriosDoCepForemVazios(
            String cep, String estado, String cidade, String bairro, String endereco, String numeroCasa, String mensagemEsperada) throws Exception {

        CepDTO cepDTO = new CepDTO(cep, estado, cidade, bairro, endereco);
        CepInfoDTO cepInfoDTO = new CepInfoDTO(cepDTO, numeroCasa);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, cepInfoDTO);

        doThrow(new CampoObrigatorioException(mensagemEsperada))
            .when(usuarioService).inserir(any(UsuarioDTO.class), any(CepInfoDTO.class));

        mockMvc.perform(post("/usuario/inserirUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(mensagemEsperada))
            .andExpect(jsonPath("$.error").value("Campo obrigatório"))
            .andExpect(jsonPath("$.path").value("/usuario/inserirUsuario"));
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposCepNull")
    void deveRetornar400_QuandoCamposObrigatoriosDoCepForemNull(
            String cep, String estado, String cidade, String bairro, String endereco, String numeroCasa, String mensagemEsperada) throws Exception {

        CepDTO cepDTO = new CepDTO(cep, estado, cidade, bairro, endereco);
        CepInfoDTO cepInfoDTO = new CepInfoDTO(cepDTO, numeroCasa);
        UsuarioWithCepInfoDTOs payload = new UsuarioWithCepInfoDTOs(USUARIO_DTO, cepInfoDTO);

        doThrow(new CampoObrigatorioException(mensagemEsperada))
            .when(usuarioService).inserir(any(UsuarioDTO.class), any(CepInfoDTO.class));

        mockMvc.perform(post("/usuario/inserirUsuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(mensagemEsperada))
            .andExpect(jsonPath("$.error").value("Campo obrigatório"))
            .andExpect(jsonPath("$.path").value("/usuario/inserirUsuario"));
    }

}