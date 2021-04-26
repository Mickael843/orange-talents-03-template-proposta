package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.AssociateWalletRequest;
import com.mikkaeru.request.card.dto.DigitalWalletRequest;
import com.mikkaeru.request.card.model.Card;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AssociateDigitalWallet {

    private final CardResource cardResource;

    public AssociateDigitalWallet(CardResource cardResource) {
        this.cardResource = cardResource;
    }

    public Optional<AssociateWalletResponse> associate(DigitalWalletRequest walletRequest, Card card) {
        Optional<AssociateWalletResponse> walletResponse = Optional.empty();

        try {
            walletResponse = Optional.of(cardResource.associate(card.getCardNumber(), new AssociateWalletRequest(
                    walletRequest.getEmail(), walletRequest.getWallet().getName()
            )));
        } catch (FeignException.FeignClientException ignored) { }

        return walletResponse;
    }
}
