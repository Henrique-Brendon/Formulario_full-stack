package com.backend.formulario.common;

import com.backend.formulario.controllers.dtos.CepDTO;
import com.backend.formulario.controllers.dtos.CepInfoDTO;

public class CepConstrants {
    public static final CepInfoDTO CEP_INFO_DTO = new CepInfoDTO(
        new CepDTO("05716-070", "SP", "São Paulo", "Vila Andrade", "Rua Itacaiúna"), 22);
}
