package com.mikkaeru.request.card.dto;

import java.time.LocalDateTime;

public class WalletRequest {

    private final String id;
    private final String email;
    private final String emissor;
    private final LocalDateTime associadaEm;

    public WalletRequest(String id, String email, String emissor, LocalDateTime associadaEm) {
        this.id = id;
        this.email = email;
        this.emissor = emissor;
        this.associadaEm = associadaEm;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEmissor() {
        return emissor;
    }

    public LocalDateTime getAssociadaEm() {
        return associadaEm;
    }
}
