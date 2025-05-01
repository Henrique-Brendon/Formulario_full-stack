package com.backend.formulario.entities;

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
@Table("ENDERECO")
public class CepInfo {
    
    @Id
    private Long id;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String endereco;
    private int numCasa;

    private Long usuarioId;
}
