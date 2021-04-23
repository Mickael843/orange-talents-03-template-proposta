package com.mikkaeru.request.card.dto;

import java.time.LocalDate;

public class NotificationCardRequest {

    private final String destino;
    private final LocalDate validoAte;

    public NotificationCardRequest(String destino, LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}
