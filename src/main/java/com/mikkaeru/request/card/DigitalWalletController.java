package com.mikkaeru.request.card;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.DigitalWalletRequest;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.Wallet;
import com.mikkaeru.request.card.repository.WalletRepository;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

import static com.mikkaeru.request.card.model.WalletType.PAYPAL;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/cards/{cardId}/wallets")
public class DigitalWalletController {

    private final CardResource cardResource;
    private final WalletRepository walletRepository;
    private final AssociateDigitalWallet digitalWallet;

    public DigitalWalletController(CardResource cardResource, WalletRepository walletRepository, AssociateDigitalWallet digitalWallet) {
        this.cardResource = cardResource;
        this.walletRepository = walletRepository;
        this.digitalWallet = digitalWallet;
    }

    @PostMapping
    public ResponseEntity<?> associate(@PathVariable String cardId, @RequestBody @Valid DigitalWalletRequest walletRequest) {
        Optional<CardResponse> cardResponse = findCard(cardId);

        if (cardResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (cardResponse.get().existPayPalWallet()) {
            return ResponseEntity.unprocessableEntity().body(
                    new Problem("Já existe uma carteira Paypal associada a esse cartão!", UNPROCESSABLE_ENTITY.value(), now())
            );
        }

        Card card = cardResponse.get().toModel();
        Optional<AssociateWalletResponse> walletResponse = digitalWallet.associate(walletRequest, card);

        if (walletResponse.isPresent()) {
            Wallet wallet = walletRepository.save(walletRequest.toModel(card, PAYPAL));

            return ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{code}")
                            .buildAndExpand(requireNonNull(wallet).getWalletCode())
                            .toUri()
            ).build();
        }

        return ResponseEntity.badRequest().build();
    }

    private Optional<CardResponse> findCard(String cardId) {
        Optional<CardResponse> cardResponse;

        try {
            cardResponse = Optional.of(cardResource.getCard(cardId));
        } catch (FeignException e) {
            return Optional.empty();
        }

        return cardResponse;
    }
}
