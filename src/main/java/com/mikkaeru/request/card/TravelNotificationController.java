package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.TravelNotificationRequest;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.repository.CardRepository;
import com.mikkaeru.request.card.repository.TravelNotificationRepository;
import com.mikkaeru.utils.WebUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/cards/{cardId}")
public class TravelNotificationController {

    private final WebUtils webUtils;
    private final CardRepository cardRepository;
    private final AccountNotification accountNotification;
    private final TravelNotificationRepository notificationRepository;

    public TravelNotificationController(WebUtils webUtils, AccountNotification accountNotification, CardRepository cardRepository, TravelNotificationRepository notificationRepository) {
        this.webUtils = webUtils;
        this.cardRepository = cardRepository;
        this.accountNotification = accountNotification;
        this.notificationRepository = notificationRepository;
    }

    @PostMapping
    public ResponseEntity<?> notification(@PathVariable String cardId, @RequestBody @Valid TravelNotificationRequest notificationRequest) {

        Optional<Card> cardOptional = cardRepository.findByCardCode(cardId);

        if (cardOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean successfullyNotified = accountNotification.notify(cardOptional.get().getCardNumber(), notificationRequest);

        if (successfullyNotified) {
            notificationRepository.save(
                    notificationRequest.toModel(cardId, webUtils.getUserAgent(), webUtils.getClientIp()));
        }

        return ResponseEntity.ok().build();
    }
}
