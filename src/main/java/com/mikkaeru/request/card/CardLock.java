package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.repository.CardLockRepository;
import com.mikkaeru.utils.WebUtils;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.stereotype.Component;

@Component
public class CardLock {

    private final Tracer tracer;
    private final WebUtils webUtils;
    private final CardResource cardResource;
    private final CardLockRepository cardLockRepository;

    public CardLock(Tracer tracer, WebUtils webUtils, CardResource cardResource, CardLockRepository cardLockRepository) {
        this.tracer = tracer;
        this.webUtils = webUtils;
        this.cardResource = cardResource;
        this.cardLockRepository = cardLockRepository;
    }

    public void lock(Card card) {
        Span activeSpan = tracer.activeSpan();
        LockSolicitationResponse lockSolicitationResponse = null;

        try {
            activeSpan.log("Realizando requisição ao serviço de cartão - recurso de bloqueio de cartão!");
            lockSolicitationResponse = cardResource.blockCard(
                    card.getCardNumber(), new LockSolicitation(webUtils.getUserAgent())
            );
        } catch (FeignException.FeignClientException ignored) { }

        if (lockSolicitationResponse != null) {
            cardLockRepository.save(new com.mikkaeru.request.card.model.CardLock(card.getCardCode(), webUtils.getClientIp(), webUtils.getUserAgent()));
        }
    }
}
