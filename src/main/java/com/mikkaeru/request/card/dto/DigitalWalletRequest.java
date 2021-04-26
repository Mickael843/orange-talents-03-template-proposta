package com.mikkaeru.request.card.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.Wallet;
import com.mikkaeru.request.card.model.WalletType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static java.util.UUID.randomUUID;

public class DigitalWalletRequest {

    @Email
    @NotBlank
    @JsonProperty
    private final String email;

    @JsonCreator
    public DigitalWalletRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Wallet toModel(Card card, WalletType walletType) {
        return new Wallet(this.email, randomUUID().toString(), walletType, card.getCardCode());
    }
}
