package com.backend.formulario.controllers.exceptions;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> handleNullPointerException(
            NullPointerException ex,
            HttpServletRequest request
    ) {
        String rawMessage = ex.getMessage();
        String formattedMessage;

        if (rawMessage == null || rawMessage.isBlank()) {
            formattedMessage = "Um valor nulo foi encontrado onde não era esperado.";
        } else {
            formattedMessage = "O objeto '" + 
                Character.toUpperCase(rawMessage.charAt(0)) + 
                rawMessage.substring(1) + 
                "' está nulo.";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Null Pointer Exception");
        response.put("message", formattedMessage);
        response.put("path", request.getRequestURI());
        response.put("timestamp", OffsetDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
