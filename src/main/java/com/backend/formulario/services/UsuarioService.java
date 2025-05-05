package com.backend.formulario.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.formulario.controllers.dtos.CepInfoDTO;
import com.backend.formulario.controllers.dtos.UsuarioDTO;
import com.backend.formulario.entities.CepInfo;
import com.backend.formulario.entities.Usuario;
import com.backend.formulario.repositories.CepInfoRepository;
import com.backend.formulario.repositories.UsuarioRepository;
import com.backend.formulario.util.ValidadorDeCamposObrigatorios;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CepInfoRepository cepInfoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, CepInfoRepository cepInfoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cepInfoRepository = cepInfoRepository;
    }

    public void inserir(UsuarioDTO usuarioDTO, CepInfoDTO cepInfoDTO) {
        try {
            if (usuarioDTO == null) {
                throw new NullPointerException();
            }
            
            Map<String, Object> campos = new HashMap<>();
            campos.put("nome", usuarioDTO.nome());
            campos.put("email", usuarioDTO.email());
            campos.put("senha", usuarioDTO.senha());
            campos.put("dataNascimento", usuarioDTO.dataNascimento());
    
            ValidadorDeCamposObrigatorios.validarCampos(campos);
    
            CepInfo cepInfo = validarEndereco(cepInfoDTO);
        
            Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.nome())
                .dataNascimento(usuarioDTO.dataNascimento())
                .email(usuarioDTO.email())
                .senha(usuarioDTO.senha())
                .build();
    
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            
            cepInfo.setUsuarioId(usuarioSalvo.getId());
            cepInfoRepository.save(cepInfo);
        }catch(NullPointerException e) {
            throw e;
        }
    }
    

    private CepInfo validarEndereco(CepInfoDTO cepInfoDTO) {
        if(cepInfoDTO == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }

        if (isVazio(cepInfoDTO.cepDTO().cep()) ||
            isVazio(cepInfoDTO.cepDTO().estado()) ||
            isVazio(cepInfoDTO.cepDTO().cidade()) ||
            // Algumas localizações não tem bairro e endereço
            cepInfoDTO.numeroCasa() <= 0) {
            throw new IllegalArgumentException("Todos os campos do endereço são obrigatórios e devem ser válidos.");
        }

        return buildarEndereco(cepInfoDTO);
    }

    private boolean isVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private CepInfo buildarEndereco(CepInfoDTO cepInfoDTO) {
        CepInfo cepInfo = CepInfo.builder()
            .cep(cepInfoDTO.cepDTO().cep())
            .estado(cepInfoDTO.cepDTO().estado())
            .cidade(cepInfoDTO.cepDTO().cidade())
            .bairro(cepInfoDTO.cepDTO().bairro())
            .endereco(cepInfoDTO.cepDTO().endereco())
            .numCasa(cepInfoDTO.numeroCasa())
            .build();

        return cepInfo;
    }
    
}
