package com.backend.formulario.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.util.exceptions.CepFormatoInvalidoException;

@ExtendWith(MockitoExtension.class)
public class CepServiceTest {
    
    private CepService cepService;

    @BeforeEach
    void setUp() {
        cepService = new CepService();
    }

    @Test
    void deveRetornarCepDTO_QuandoJsonValidoInformado() {
        String jsonValido = """
            {
                "cep": "05716-070",
                "uf": "SP",
                "localidade": "São Paulo",
                "bairro": "Vila Andrade",
                "logradouro": "Rua Itacaiúna"
            }
        """;

        CepDTO dto = cepService.verificarEndereco(jsonValido);

        assertNotNull(dto);
        assertEquals("05716-070", dto.cep());
        assertEquals("SP", dto.estado());
        assertEquals("São Paulo", dto.cidade());
        assertEquals("Vila Andrade", dto.bairro());
        assertEquals("Rua Itacaiúna", dto.endereco());
    }

    @Test
    void deveLancarNullPointerException_QuandoJsonForNulo() {
        String jsonNulo = null;

        Exception ex = assertThrows(NullPointerException.class, () -> {
            cepService.verificarEndereco(jsonNulo);
        });

        assertEquals("O objeto não pode estar nulo.", ex.getMessage());
    }

    @Test
    void deveLancarJSONException_QuandoJsonMalFormatado() {
        String jsonMalFormado = "{ \"cep\": \"05716-070\" ";

        assertThrows(CepFormatoInvalidoException.class, () -> {
            cepService.verificarEndereco(jsonMalFormado);
        });
    }
}
