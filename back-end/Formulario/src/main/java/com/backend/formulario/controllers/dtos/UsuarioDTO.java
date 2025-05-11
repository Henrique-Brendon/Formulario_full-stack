package com.backend.formulario.controllers.dtos;

import java.time.Instant;

public record UsuarioDTO(
    String nome,
    Instant dataNascimento,
    String email,
    String senha
) {
    
}
