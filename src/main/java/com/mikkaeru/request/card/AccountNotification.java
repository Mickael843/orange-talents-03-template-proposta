package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.NotificationCardRequest;
import com.mikkaeru.request.card.dto.NotificationCardResponse;
import com.mikkaeru.request.card.dto.TravelNotificationRequest;
import feign.FeignException;
import org.springframework.stereotype.Component;

@Component
public class AccountNotification {

    private final CardResource cardResource;

    public AccountNotification(CardResource cardResource) {
        this.cardResource = cardResource;
    }

    public boolean notify(String cardId, TravelNotificationRequest notificationRequest) {
        NotificationCardResponse response;

        try {
            response = cardResource.notify(cardId, new NotificationCardRequest(
                    notificationRequest.getTravelDestination(), notificationRequest.getEndDateTravel()
            ));
        } catch (FeignException.FeignClientException e) {
            return false;
        }

        return response != null && response.getResultado().equalsIgnoreCase("CRIADO");
    }
}
