package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.model.CardLock;
import com.mikkaeru.request.card.repository.CardLockRepository;
import com.mikkaeru.utils.WebUtils;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CardLockService {

    private final Tracer tracer;
    private final WebUtils webUtils;
    private final CardResource cardResource;
    private final CardLockRepository cardLockRepository;

    public CardLockService(Tracer tracer, WebUtils webUtils, CardResource cardResource, CardLockRepository cardLockRepository) {
        this.tracer = tracer;
        this.webUtils = webUtils;
        this.cardResource = cardResource;
        this.cardLockRepository = cardLockRepository;
    }

    public void lock(Card card) {
        Span activeSpan = tracer.activeSpan();
        Optional<LockSolicitationResponse> lockSolicitationResponse = Optional.empty();

        try {
            activeSpan.log("Realizando requisição ao serviço de cartão - recurso de bloqueio de cartão!");
            lockSolicitationResponse = Optional.ofNullable(cardResource.blockCard(
                    card.getCardNumber(), new LockSolicitation(webUtils.getUserAgent())));
        } catch (FeignException.FeignClientException ignored) { }

        lockSolicitationResponse.ifPresent(l -> cardLockRepository.save(new CardLock(card.getCardCode(), webUtils.getClientIp(), webUtils.getUserAgent())));
    }
}
