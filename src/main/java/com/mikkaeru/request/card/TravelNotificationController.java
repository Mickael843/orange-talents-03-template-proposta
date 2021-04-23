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
    private final TravelNotificationRepository notificationRepository;

    public TravelNotificationController(WebUtils webUtils, CardRepository cardRepository, TravelNotificationRepository notificationRepository) {
        this.webUtils = webUtils;
        this.cardRepository = cardRepository;
        this.notificationRepository = notificationRepository;
    }

    @PostMapping
    public ResponseEntity<?> notification(@PathVariable String cardId, @RequestBody @Valid TravelNotificationRequest notificationRequest) {

        Optional<Card> cardOptional = cardRepository.findByCardCode(cardId);

        if (cardOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        notificationRepository.save(notificationRequest.toModel(cardId, webUtils.getUserAgent(), webUtils.getClientIp()));

        return ResponseEntity.ok().build();
    }
}
