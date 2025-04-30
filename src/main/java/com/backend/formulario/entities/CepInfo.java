package com.backend.formulario.entities;

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
public class CepInfo {
    private Long id;
    String cep;
    String estado;
    String cidade;
    String bairro;
    String endereco;
    int numCasa;
}
