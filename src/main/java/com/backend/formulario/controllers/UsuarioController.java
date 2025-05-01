package com.backend.formulario.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.formulario.controllers.dtos.UsuarioWithCepInfoDTOs;
import com.backend.formulario.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/inserirUsuario")
    public ResponseEntity<UsuarioWithCepInfoDTOs> inserirUsuario(@RequestBody UsuarioWithCepInfoDTOs entity) {
        usuarioService.inserir(entity.usuarioDTO(), entity.cepInfoDTO());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }
    
}
