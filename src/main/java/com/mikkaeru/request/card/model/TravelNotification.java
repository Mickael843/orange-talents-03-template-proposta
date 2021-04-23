package com.mikkaeru.request.card.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.now;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class TravelNotification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String cardId;
    @Column(nullable = false)
    private String travelDestination;
    @Column(nullable = false)
    private LocalDate endDateTravel;
    @Column(nullable = false)
    private String userAgent;
    @Column(nullable = false)
    private String clientIp;
    @CreationTimestamp
    private OffsetDateTime createdAt = now();

    /**
     * @deprecated hibernate only
     */
    public TravelNotification() { }

    public TravelNotification(@NotBlank String travelDestination, @Future @NotNull LocalDate endDateTravel,
                              @NotBlank String cardId, @NotBlank String userAgent, @NotBlank String clientIp) {
        this.travelDestination = travelDestination;
        this.endDateTravel = endDateTravel;
        this.cardId = cardId;
        this.userAgent = userAgent;
        this.clientIp = clientIp;
    }
}
