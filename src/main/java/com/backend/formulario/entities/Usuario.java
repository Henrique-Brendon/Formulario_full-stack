package com.backend.formulario.entities;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode  
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {
    
    private Long id;
    private String nome;
    private Instant dataNascimento;
    private String email;
    private String senha;
}
