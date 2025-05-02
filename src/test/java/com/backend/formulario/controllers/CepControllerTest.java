
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
    


}
    