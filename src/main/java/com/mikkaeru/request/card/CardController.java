package com.mikkaeru.request.card;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.CardLock;
import com.mikkaeru.request.card.repository.CardLockRepository;
import com.mikkaeru.request.card.repository.CardRepository;
import com.mikkaeru.utils.WebUtils;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final WebUtils webUtils;
    private final CardResource cardResource;
    private final CardRepository cardRepository;
    private final CardLockRepository cardLockRepository;

    public CardController(WebUtils webUtils, CardResource cardResource, CardRepository cardRepository, CardLockRepository cardLockRepository) {
        this.webUtils = webUtils;
        this.cardResource = cardResource;
        this.cardRepository = cardRepository;
        this.cardLockRepository = cardLockRepository;
    }

    @GetMapping("/{cardId}/blocks")
    public ResponseEntity<?> lockCard(@PathVariable String cardId) {

        CardResponse cardResponse = null;

        try {
            cardResponse = cardResource.getCard(cardId);
        } catch (FeignException e) {
            e.printStackTrace();
        }

        if (cardResponse == null) {
            return ResponseEntity.notFound().build();
        }

        Card card = cardResponse.toModel();

        // TODO Fazer uma requisição para o sistema de cartões para verificar se o cartão está bloqueado.
        // TODO Retirar o campo de status da entidade de cartão.

        if (card.isBlocked()) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(new Problem("O cartão já está bloqueado", UNPROCESSABLE_ENTITY.value(), now()));
        }

        LockSolicitationResponse lockSolicitationResponse = null;

        try {
            lockSolicitationResponse = cardResource.blockCard(card.getCardNumber(), new LockSolicitation(webUtils.getUserAgent()));
        } catch (FeignException e) {
            e.printStackTrace();
        }

        // TODO Verificar a resposta da requisição, e montar uma estrutura de condição
        // TODO que só salvara o bloqueio se o status do enum for igual a BLOQUEADO.
        if (lockSolicitationResponse != null) {
            card.lockCard();
            cardLockRepository.save(new CardLock(card, webUtils.getClientIp(), webUtils.getUserAgent()));
        }

        return ResponseEntity.ok().build();
    }
}
