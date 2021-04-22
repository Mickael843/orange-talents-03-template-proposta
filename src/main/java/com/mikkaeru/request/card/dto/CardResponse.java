package com.mikkaeru.request.card.dto;

import com.mikkaeru.request.card.model.Card;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardResponse {

    private final String id;
    private final String titular;
    private final BigDecimal limite;
    private final String idProposta;
    private final String renegociacao;
    private final LocalDateTime emitidoEm;

    public CardResponse(String id, String titular, BigDecimal limite, String idProposta, String renegociacao, LocalDateTime emitidoEm) {
        this.id = id;
        this.titular = titular;
        this.limite = limite;
        this.idProposta = idProposta;
        this.renegociacao = renegociacao;
        this.emitidoEm = emitidoEm;
    }

    public Card toModel() {
        return new Card(this.id, this.titular, this.limite, this.idProposta, this.emitidoEm, this.renegociacao);
    }
}
