package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.AssociateWalletRequest;
import com.mikkaeru.request.card.dto.DigitalWalletRequest;
import com.mikkaeru.request.card.model.Card;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mikkaeru.request.card.model.WalletType.PAYPAL;

@Component
public class AssociateDigitalWallet {

    private final CardResource cardResource;

    public AssociateDigitalWallet(CardResource cardResource) {
        this.cardResource = cardResource;
    }

    public Optional<AssociateWalletResponse> associate(DigitalWalletRequest walletRequest, Card card) {
        Optional<AssociateWalletResponse> walletResponse = Optional.empty();

        try {
            walletResponse = Optional.of(cardResource.associate(
                    card.getCardNumber(), new AssociateWalletRequest(walletRequest.getEmail(), PAYPAL.getName())
            ));
        } catch (FeignException.FeignClientException ignored) { }

        return walletResponse;
    }
}
