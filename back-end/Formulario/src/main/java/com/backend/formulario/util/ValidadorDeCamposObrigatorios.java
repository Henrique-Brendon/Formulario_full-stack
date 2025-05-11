package com.backend.formulario.util;

import java.util.Map;

import com.backend.formulario.util.exceptions.CampoObrigatorioException;

public class ValidadorDeCamposObrigatorios {

    /**
     * Valida se os campos fornecidos estão preenchidos.
     * Campos nulos ou em branco disparam uma exceção personalizada.
     * Campos de tipo objeto disparam uma NullPointerException.
     *
     * @param camposUmMapa Mapa com o nome do campo como chave e o valor a ser validado.
     * @throws CampoObrigatorioException se algum campo obrigatório estiver vazio ou nulo.
     * @throws NullPointerException se algum objeto estiver nulo.
     */
    public static void validarCampos(Map<String, ?> campos) {
        for (Map.Entry<String, ?> campo : campos.entrySet()) {
            if (campo.getValue() == null) {
                throw new CampoObrigatorioException("O campo '" + campo.getKey() + "' não pode ser nulo.");
            }
            
            if (campo.getValue() instanceof String str && str.trim().isEmpty()) {
                throw new CampoObrigatorioException("O campo '" + campo.getKey() + "' não pode ser vazio.");
            }
        }
    }
}
