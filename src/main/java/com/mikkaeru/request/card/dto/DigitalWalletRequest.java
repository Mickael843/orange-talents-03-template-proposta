package com.mikkaeru.request.card.dto;

import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.Wallet;
import com.mikkaeru.request.card.model.WalletType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.mikkaeru.request.card.model.WalletType.valueOf;
import static java.util.Locale.ROOT;
import static java.util.UUID.randomUUID;

public class DigitalWalletRequest {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    private final String wallet;

    public DigitalWalletRequest(@NotBlank @Email String email, @NotBlank String wallet) {
        this.email = email;
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public WalletType getWallet() {
        return valueOf(wallet.toUpperCase(ROOT));
    }

    public Wallet toModel(Card card) {
        return new Wallet(email, randomUUID().toString(), valueOf(wallet.toUpperCase(ROOT)), card.getCardCode());
    }
}
