package com.backend.formulario.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Table("USUARIOS")
public class Usuario {
    
    @Id
    private Long id;
    private String nome;
    private Instant dataNascimento;
    private String email;
    private String senha;
    
}
