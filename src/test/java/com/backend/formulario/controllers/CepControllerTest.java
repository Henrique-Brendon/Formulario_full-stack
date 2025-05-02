
package com.backend.formulario.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.services.CepService;
import com.backend.formulario.util.exceptions.CepFormatoInvalidoException;
import com.backend.formulario.util.exceptions.CepNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
    
@WebMvcTest(CepController.class)
public class CepControllerTest {
        
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CepService cepService;

    private CepDTO cepDTO;

    @BeforeEach
    public void setup() {
        cepDTO = new CepDTO("05716-070", "SP", "São Paulo", "Vila Andrade", "Rua Itacaiúna");

    }

    @Test
    void localizarEndereco_QuandoCepValido_EntaoRetornaCepDTO() throws Exception {
        when(cepService.verificarEndereco(any())).thenReturn(cepDTO);

        mockMvc.perform(get("/cep/localizarEndereco")
            .param("cep", "05716-070")
            .content(objectMapper.writeValueAsString(cepDTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cep").value("05716-070"))
            .andExpect(jsonPath("$.estado").value("SP"))
            .andExpect(jsonPath("$.cidade").value("São Paulo"))
            .andExpect(jsonPath("$.bairro").value("Vila Andrade"))
            .andExpect(jsonPath("$.endereco").value("Rua Itacaiúna"));
    }
    
    @Test
    void deveRetornar404_QuandoCepNaoEncontrado() throws Exception {
        when(cepService.verificarEndereco(any())).thenThrow(new CepNotFoundException("CEP não encontrado"));

        mockMvc.perform(get("/cep/localizarEndereco")
                .param("cep", "00000-000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("CEP não encontrado: 00000-000"))
                .andExpect(jsonPath("$.path").value("/cep/localizarEndereco"));
    }

    @Test
    void deveRetornar400_QuandoFormatoDeCepForInvalido() throws Exception {
        when(cepService.verificarEndereco(any())).thenThrow(new CepFormatoInvalidoException("Formato de CEP inválido"));
            mockMvc.perform(get("/cep/localizarEndereco")
            .param("cep", "000000-000"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Formato de CEP inválido: 000000-000"))
            .andExpect(jsonPath("$.path").value("/cep/localizarEndereco"));
    }

}
    