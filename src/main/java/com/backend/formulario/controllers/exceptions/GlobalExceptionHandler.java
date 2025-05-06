package com.backend.formulario.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.backend.formulario.util.exceptions.CampoObrigatorioException;
import com.backend.formulario.util.exceptions.CepFormatoInvalidoException;
import com.backend.formulario.util.exceptions.CepNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CampoObrigatorioException.class)
    public ResponseEntity<ErrorResponse> handleCampoObrigatorioException(
        CampoObrigatorioException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Campo obrigatório",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handlerNullPointerException(
        NullPointerException ex, HttpServletRequest request) {
    
        String detailedMessage = "Um valor nulo foi encontrado onde não era esperado.";
        if (ex.getMessage() != null) {
            if (ex.getMessage().equals("UsuarioDTO")) {
                detailedMessage = "O objeto 'UsuarioDTO' está nulo.";
            } else if (ex.getMessage().equals("cepInfoDTO")) {
                detailedMessage = "O objeto 'CepInfoDTO' está nulo.";
            }
        }
    
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Null Pointer Exception",
            detailedMessage, request.getRequestURI()
        );
    
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCepNotFoundException(
        CepNotFoundException ex, HttpServletRequest request) {
            
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "CEP não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CepFormatoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleCepFormatoInvalidoException(
            CepFormatoInvalidoException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "CEP inválido",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
