package com.backend.formulario.testdata;

import static com.backend.formulario.common.CepConstrants.CEP_INFO_DTO;
import static com.backend.formulario.common.UsuarioConstrants.USUARIO_DTO;

import java.time.Instant;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class UsuarioTestDataProvider {

    public static Stream<Arguments> fornecerObjetosNulos() {
        return Stream.of(
            Arguments.of(USUARIO_DTO, null, "CepInfoDTO"),
            Arguments.of(null, CEP_INFO_DTO, "UsuarioDTO")
        );
    }

    public static Stream<Arguments> fornecerCamposUsuarioVazio() {
        return Stream.of(
            Arguments.of("", Instant.now(), "email@example.com", "senha123", "O campo 'nome' não pode ser vazio."),
            Arguments.of("João", Instant.now(), "", "senha123", "O campo 'email' não pode ser vazio."),
            Arguments.of("João", Instant.now(), "email@example.com", "", "O campo 'senha' não pode ser vazio.")
        );
    }

    public static Stream<Arguments> fornecerCamposUsuarioNull() {
        return Stream.of(
            Arguments.of(null, Instant.now(), "email@example.com", "senha123", "O campo 'nome' não pode ser nulo."),
            Arguments.of("João", null, "email@example.com", "senha123", "O campo 'dataNascimento' não pode ser nulo."),
            Arguments.of("João", Instant.now(), null, "senha123", "O campo 'email' não pode ser nulo."),
            Arguments.of("João", Instant.now(), "email@example.com", null, "O campo 'senha' não pode ser nulo.")
        );
    }

    public static Stream<Arguments> fornecerCamposCepVazios() {
        return Stream.of(
            Arguments.of("", "SP", "São Paulo", "Bairro", "Rua A", "123", "O campo 'cep' não pode ser vazio."),
            Arguments.of("12345678", "", "São Paulo", "Bairro", "Rua A", "123", "O campo 'estado' não pode ser vazio."),
            Arguments.of("12345678", "SP", "", "Bairro", "Rua A", "123", "O campo 'cidade' não pode ser vazio."),
            Arguments.of("12345678", "SP", "São Paulo", "Bairro", "Rua A", "", "O campo 'numero de casa' não pode ser vazio.")
        );
    }

    public static Stream<Arguments> fornecerCamposCepNull() {
        return Stream.of(
            Arguments.of(null, "SP", "São Paulo", "Bairro", "Rua A", "123", "O campo 'cep' não pode ser nulo."),
            Arguments.of("12345678", null, "São Paulo", "Bairro", "Rua A", "123", "O campo 'estado' não pode ser nulo."),
            Arguments.of("12345678", "SP", null, "Bairro", "Rua A", "123", "O campo 'cidade' não pode ser nulo."),
            Arguments.of("12345678", "SP", "São Paulo", "Bairro", "Rua A", null, "O campo 'numero de casa' não pode ser nulo.")
        );
    }
}