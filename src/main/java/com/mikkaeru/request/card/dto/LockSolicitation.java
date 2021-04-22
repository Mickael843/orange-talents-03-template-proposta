package com.mikkaeru.request.card.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LockSolicitation {

    @JsonProperty
    private final String sistemaResponsavel;

    @JsonCreator
    public LockSolicitation(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }
}
