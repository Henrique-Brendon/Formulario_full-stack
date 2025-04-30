package com.backend.formulario.util.exceptions;

public class CepFormatoInvalidoException extends RuntimeException {
    public CepFormatoInvalidoException(String cep) {
        super("Formato de CEP inválido: " + cep);
    }
}
