package com.backend.formulario.services;

import static com.backend.formulario.common.UsuarioConstrants.USUARIO_DTO;
import static com.backend.formulario.common.CepConstrants.CEP_INFO_DTO;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.formulario.entities.Usuario;
import com.backend.formulario.repositories.CepInfoRepository;
import com.backend.formulario.repositories.UsuarioRepository;

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

}
