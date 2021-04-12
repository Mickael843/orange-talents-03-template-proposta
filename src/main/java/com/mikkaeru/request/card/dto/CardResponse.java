package com.mikkaeru.request.card.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mikkaeru.request.card.model.Card;

public class CardResponse {

    private String id;
//    private String titular;
//    private BigDecimal limite;
//    private String idProposta;
//    private DueDate vencimento;
//    private String renegociacao;
//    private LocalDateTime emitidoEm;
//    private List<Blockade> bloqueios;
//    private List<Notice> avisos;
//    private List<Portfolio> carteiras;
//    private List<Installments> parcelas;

    @JsonCreator
    public CardResponse(@JsonProperty("id") String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Card toModel() {
        return new Card(this.id);
    }
}
