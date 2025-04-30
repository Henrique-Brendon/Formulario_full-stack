package com.backend.formulario.controllers.dtos;

public record CepDTO(
    String cep,
    String estado,
    String cidade,
    String bairro,
    String endereco
) {
    
}
