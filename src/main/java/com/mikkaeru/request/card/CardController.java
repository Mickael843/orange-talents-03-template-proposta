package com.mikkaeru.request.card;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.CardLock;
import com.mikkaeru.request.card.repository.CardLockRepository;
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
    private final CardLockRepository cardLockRepository;

    public CardController(WebUtils webUtils, CardResource cardResource, CardLockRepository cardLockRepository) {
        this.webUtils = webUtils;
        this.cardResource = cardResource;
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

        if (cardResponse.isBlocked()) {
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

        if (lockSolicitationResponse != null) {
            cardLockRepository.save(new CardLock(card.getCardCode(), webUtils.getClientIp(), webUtils.getUserAgent()));
        }

        return ResponseEntity.ok().build();
    }
}
