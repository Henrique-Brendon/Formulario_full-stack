package com.backend.formulario.util.exceptions;

public class CampoObrigatorioException extends IllegalArgumentException {
    public CampoObrigatorioException(String campo) {
        super(campo);
    }
}

