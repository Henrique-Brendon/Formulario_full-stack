package com.backend.formulario.common;

import java.time.LocalDate;
import java.time.ZoneId;

import com.backend.formulario.controllers.dtos.UsuarioDTO;

public class UsuarioConstrants {
    public static final UsuarioDTO USUARIO_DTO = new UsuarioDTO("Laura",
        LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(), "laura@email.com", "senha123");
}
