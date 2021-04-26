package com.mikkaeru.request.card;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.request.card.dto.AssociateWalletRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.DigitalWalletRequest;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.Wallet;
import com.mikkaeru.request.card.repository.CardRepository;
import com.mikkaeru.request.card.repository.WalletRepository;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import static com.mikkaeru.request.card.model.WalletType.PAYPAL;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/cards/{cardId}/wallets")
public class DigitalWalletController {

    private final CardResource cardResource;
    private final CardRepository cardRepository;
    private final WalletRepository walletRepository;

    public DigitalWalletController(CardResource cardResource, CardRepository cardRepository, WalletRepository walletRepository) {
        this.cardResource = cardResource;
        this.cardRepository = cardRepository;
        this.walletRepository = walletRepository;
    }

    @PostMapping
    public ResponseEntity<?> associate(@PathVariable String cardId, @RequestBody @Valid DigitalWalletRequest walletRequest) {
        CardResponse cardResponse = null;

        try {
            cardResponse = cardResource.getCard(cardId);
        } catch (FeignException e) {
            e.printStackTrace();
        }

        if (cardResponse == null) {
            return ResponseEntity.notFound().build();
        }

        if (cardResponse.existPayPalWallet()) {
            return ResponseEntity.unprocessableEntity().body(
                    new Problem("Já existe uma carteira Paypal associada a esse cartão!", UNPROCESSABLE_ENTITY.value(), now())
            );
        }

        Card card = cardResponse.toModel();
        AssociateWalletResponse walletResponse = null;

        try {
            walletResponse = cardResource.associate(
                    card.getCardNumber(), new AssociateWalletRequest(walletRequest.getEmail(), PAYPAL.getName())
            );
        } catch (FeignException e) {
            e.printStackTrace();
        }

        Wallet wallet = null;
        if (walletResponse != null) {
            wallet = walletRepository.save(walletRequest.toModel(card, PAYPAL));
        }

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{code}")
                        .buildAndExpand(requireNonNull(wallet).getWalletCode())
                        .toUri()
        ).build();
    }
}
