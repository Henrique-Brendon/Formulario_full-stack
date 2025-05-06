package com.backend.formulario.controllers;

import static com.backend.formulario.common.UsuarioConstrants.USUARIO_DTO;
import static com.backend.formulario.common.CepConstrants.CEP_INFO_DTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.formulario.controllers.dtos.CepInfoDTO;
import com.backend.formulario.controllers.dtos.UsuarioDTO;
import com.backend.formulario.controllers.dtos.UsuarioWithCepInfoDTOs;
import com.backend.formulario.services.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
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
    
}