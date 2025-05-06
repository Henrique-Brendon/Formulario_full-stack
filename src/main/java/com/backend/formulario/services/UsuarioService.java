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
            
            validarCamposObrigatoriosUsuario(usuarioDTO);

            validarCamposObrigatoriosCep(cepInfoDTO);
    
            CepInfo cepInfo = buildarEndereco(cepInfoDTO);
        
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
    
    private void validarCamposObrigatoriosUsuario(UsuarioDTO usuarioDTO) {
        Map<String, Object> campos = new HashMap<>();
        campos.put("nome", usuarioDTO.nome());
        campos.put("email", usuarioDTO.email());
        campos.put("senha", usuarioDTO.senha());
        campos.put("dataNascimento", usuarioDTO.dataNascimento());
    
        ValidadorDeCamposObrigatorios.validarCampos(campos);
    }
    
    private void validarCamposObrigatoriosCep(CepInfoDTO cepInfoDTO) {
        if (cepInfoDTO == null || cepInfoDTO.cepDTO() == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }
    
        Map<String, Object> campos = new HashMap<>();
        campos.put("cep", cepInfoDTO.cepDTO().cep());
        campos.put("estado", cepInfoDTO.cepDTO().estado());
        campos.put("cidade", cepInfoDTO.cepDTO().cidade());
        campos.put("numero de casa", cepInfoDTO.numeroCasa());
    
        ValidadorDeCamposObrigatorios.validarCampos(campos);
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
