package com.mikkaeru.request.card.dto;

import java.time.LocalDateTime;

public class Blocked {

    private final String id;
    private final Boolean ativo;
    private final LocalDateTime bloqueadoEm;
    private final String sistemaResponsavel;

    public Blocked(String id, Boolean ativo, LocalDateTime bloqueadoEm, String sistemaResponsavel) {
        this.id = id;
        this.ativo = ativo;
        this.bloqueadoEm = bloqueadoEm;
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getId() {
        return id;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getBloqueadoEm() {
        return bloqueadoEm;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
