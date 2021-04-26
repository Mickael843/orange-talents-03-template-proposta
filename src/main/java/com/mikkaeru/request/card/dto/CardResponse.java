package com.mikkaeru.request.card.dto;

import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.WalletType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CardResponse {

    private final String id;
    private final String titular;
    private final BigDecimal limite;
    private final String idProposta;
    private final String renegociacao;
    private final LocalDateTime emitidoEm;
    private final List<Blocked> bloqueios;
    private final List<WalletRequest> carteiras;

    public CardResponse(String id, String titular, BigDecimal limite, String idProposta, String renegociacao, LocalDateTime emitidoEm, List<Blocked> bloqueios, List<WalletRequest> carteiras) {
        this.id = id;
        this.titular = titular;
        this.limite = limite;
        this.idProposta = idProposta;
        this.renegociacao = renegociacao;
        this.emitidoEm = emitidoEm;
        this.bloqueios = bloqueios;
        this.carteiras = carteiras;
    }

    public Card toModel() {
        return new Card(this.id, this.titular, this.limite, this.idProposta, this.emitidoEm, this.renegociacao);
    }

    public boolean isLocked() {
        return !this.bloqueios.isEmpty();
    }

    public boolean existWallet(WalletType walletType) {

        for (WalletRequest walletRequest: this.carteiras) {

            if (walletRequest.getEmissor().equalsIgnoreCase(walletType.getName())) {
                return true;
            }
        }

        return false;
    }
}
