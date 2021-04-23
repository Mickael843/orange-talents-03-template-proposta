package com.mikkaeru.request.card.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationCardResponse {

    @JsonProperty
    private final String resultado;

    @JsonCreator
    public NotificationCardResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
