package com.backend.formulario.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.formulario.entities.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    
}
