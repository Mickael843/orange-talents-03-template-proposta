package com.mikkaeru.request.card;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.request.card.dto.CardResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final Tracer tracer;
    private final CardLock cardLock;
    private final CardResource cardResource;

    public CardController(Tracer tracer, CardLock cardLock, CardResource cardResource) {
        this.tracer = tracer;
        this.cardLock = cardLock;
        this.cardResource = cardResource;
    }

    @GetMapping("/{cardId}/blocks")
    public ResponseEntity<?> lockCard(@PathVariable String cardId) {
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("card.id", cardId);

        Optional<CardResponse> cardResponse = findCard(cardId);

        if (cardResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (cardResponse.get().isLocked()) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(new Problem("O cartão já está bloqueado", UNPROCESSABLE_ENTITY.value(), now()));
        }

       cardLock.lock(cardResponse.get().toModel());

        return ResponseEntity.ok().build();
    }

    private Optional<CardResponse> findCard(String cardId) {
        Optional<CardResponse> cardResponse = Optional.empty();

        try {
            cardResponse = Optional.of(cardResource.getCard(cardId));
        } catch (FeignException.FeignClientException ignored) { }

        return cardResponse;
    }
}
