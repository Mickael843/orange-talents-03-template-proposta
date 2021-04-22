package com.mikkaeru.request.card.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LockSolicitationResponse {

    // TODO Criar o enum Resultado com dois valores possíveis. (BLOQUEADO, NAO_BLOQUEADO)

    @JsonProperty
    private final String resultado;

    @JsonCreator
    public LockSolicitationResponse(String resultado) {
        this.resultado = resultado;
    }
}
