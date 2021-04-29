package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.AssociateWalletRequest;
import com.mikkaeru.request.card.dto.DigitalWalletRequest;
import com.mikkaeru.request.card.model.Card;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AssociateDigitalWallet {

    private final Tracer tracer;
    private final CardResource cardResource;

    public AssociateDigitalWallet(Tracer tracer, CardResource cardResource) {
        this.tracer = tracer;
        this.cardResource = cardResource;
    }

    public Optional<AssociateWalletResponse> associate(DigitalWalletRequest walletRequest, Card card) {
        Span activeSpan = tracer.activeSpan();
        Optional<AssociateWalletResponse> walletResponse = Optional.empty();

        try {
            activeSpan.log("Realizando requisição oa sistema de cartão - recurso de bloqueio de cartões!");
            walletResponse = Optional.ofNullable(cardResource.associate(card.getCardNumber(), new AssociateWalletRequest(
                    walletRequest.getEmail(), walletRequest.getWallet().getName()
            )));
        } catch (FeignException.FeignClientException ignored) { }

        return walletResponse;
    }
}
