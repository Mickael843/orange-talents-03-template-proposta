package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.repository.CardLockRepository;
import com.mikkaeru.utils.WebUtils;
import feign.FeignException;
import org.springframework.stereotype.Component;

@Component
public class CardLock {

    private final WebUtils webUtils;
    private final CardResource cardResource;
    private final CardLockRepository cardLockRepository;

    public CardLock(WebUtils webUtils, CardResource cardResource, CardLockRepository cardLockRepository) {
        this.webUtils = webUtils;
        this.cardResource = cardResource;
        this.cardLockRepository = cardLockRepository;
    }

    public void lock(Card card) {
        LockSolicitationResponse lockSolicitationResponse = null;

        try {
            lockSolicitationResponse = cardResource.blockCard(
                    card.getCardNumber(), new LockSolicitation(webUtils.getUserAgent())
            );
        } catch (FeignException.FeignClientException ignored) { }

        if (lockSolicitationResponse != null) {
            cardLockRepository.save(new com.mikkaeru.request.card.model.CardLock(card.getCardCode(), webUtils.getClientIp(), webUtils.getUserAgent()));
        }
    }
}
