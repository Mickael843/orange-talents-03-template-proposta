package com.mikkaeru.request.card.dto;

import com.mikkaeru.request.card.model.TravelNotification;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TravelNotificationRequest {

    @NotBlank
    private final String travelDestination;
    @Future
    @NotNull
    private final LocalDate endDateTravel;

    public TravelNotificationRequest(@NotBlank String travelDestination, @Future @NotNull LocalDate endDateTravel) {
        this.travelDestination = travelDestination;
        this.endDateTravel = endDateTravel;
    }

    public TravelNotification toModel(String cardId, String userAgent, String clientIp) {
        return new TravelNotification(this.travelDestination, this.endDateTravel, cardId, userAgent, clientIp);
    }
}
