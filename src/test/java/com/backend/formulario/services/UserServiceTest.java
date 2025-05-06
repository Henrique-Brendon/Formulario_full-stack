package com.backend.formulario.services;

import static com.backend.formulario.common.UsuarioConstrants.USUARIO_DTO;
import static com.backend.formulario.common.CepConstrants.CEP_INFO_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.controllers.dtos.CepInfoDTO;
import com.backend.formulario.controllers.dtos.UsuarioDTO;
import com.backend.formulario.entities.Usuario;
import com.backend.formulario.repositories.CepInfoRepository;
import com.backend.formulario.repositories.UsuarioRepository;
import com.backend.formulario.util.exceptions.CampoObrigatorioException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CepInfoRepository cepInfoRepository;

    @Test
    void deveInserirUsuarioComEnderecoValido() {
        // Arrange
        Usuario usuarioSalvo = Usuario.builder()
            .id(1L)
            .nome("Laura")
            .email("laura@gmail.com")
            .senha("senha123")
            .dataNascimento(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant())
            .build();

        when(usuarioRepository.save(any())).thenReturn(usuarioSalvo);

        // Act
        usuarioService.inserir(USUARIO_DTO, CEP_INFO_DTO);

        // Asserts
        verify(usuarioRepository).save(any(Usuario.class));
        verify(cepInfoRepository).save(argThat(cep -> cep.getUsuarioId() == 1L));
    }

    @Test
    void deveLancarExcecaoQuandoCepForNulo() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.inserir(USUARIO_DTO, null);
        });

        assertEquals("Endereço não pode ser nulo.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposUsuarioVazio")
    void deveLancarExcecaoQuandoCamposObrigatoriosEstiveremVazios(
            String nome, Instant dataNascimento, String email, String senha, String mensagemEsperada) {

        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, dataNascimento, email, senha);

        // Act & Assert
        CampoObrigatorioException exception = Assertions.assertThrows(CampoObrigatorioException.class, () -> {
            usuarioService.inserir(usuarioDTO, CEP_INFO_DTO);
        });

        // Assertiva da mensagem
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposUsuarioNull")
    void deveLancarExcecaoQuandoCamposObrigatoriosEstiveremNull(
            String nome, Instant dataNascimento, String email, String senha, String mensagemEsperada) {

        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO(nome, dataNascimento, email, senha);

        // Act & Assert
        CampoObrigatorioException exception = Assertions.assertThrows(CampoObrigatorioException.class, () -> {
            usuarioService.inserir(usuarioDTO, CEP_INFO_DTO);
        });

        // Assertiva da mensagem
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposCepVazios")
    void deveLancarExcecaoQuandoCamposCepObrigatoriosEstiveremVazios(
            String cep, String estado, String cidade, String bairro, String endereco, String numeroCasa, String mensagemEsperada) {

        // Arrange
        CepInfoDTO cepInfoDTO = new CepInfoDTO(new CepDTO(cep, estado, cidade, bairro, endereco), numeroCasa);

        // Act & Assert
        CampoObrigatorioException exception = Assertions.assertThrows(CampoObrigatorioException.class, () -> {
            usuarioService.inserir(USUARIO_DTO, cepInfoDTO);
        });

        // Assertiva da mensagem
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("com.backend.formulario.testdata.UsuarioTestDataProvider#fornecerCamposCepNull")
    void deveLancarExcecaoQuandoCamposCepObrigatoriosEstiveremNull(
            String cep, String estado, String cidade, String bairro, String endereco, String numeroCasa, String mensagemEsperada) {

        // Arrange
        CepInfoDTO cepInfoDTO = new CepInfoDTO(new CepDTO(cep, estado, cidade, bairro, endereco), numeroCasa);

        // Act & Assert
        CampoObrigatorioException exception = Assertions.assertThrows(CampoObrigatorioException.class, () -> {
            usuarioService.inserir(USUARIO_DTO, cepInfoDTO);
        });

        // Assertiva da mensagem
        assertEquals(mensagemEsperada, exception.getMessage());
    }

}
