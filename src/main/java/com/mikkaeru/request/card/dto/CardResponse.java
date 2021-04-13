package com.mikkaeru.request.card.dto;

import com.mikkaeru.request.card.model.Card;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardResponse {

    private String id;
    private String titular;
    private BigDecimal limite;
    private String idProposta;
    private String renegociacao;
    private LocalDateTime emitidoEm;
//    private List<Notice> avisos;
//    private List<DueDate> vencimento;
//    private List<Blockade> bloqueios;
//    private List<Portfolio> carteiras;
//    private List<Installments> parcelas;


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
