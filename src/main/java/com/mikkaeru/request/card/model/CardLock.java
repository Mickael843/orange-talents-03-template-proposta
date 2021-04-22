package com.mikkaeru.request.card.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class CardLock {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    private Card card;
    @Column(nullable = false)
    private String clientIp;
    @Column(nullable = false)
    private String userAgent;
    @CreationTimestamp
    private OffsetDateTime blockedAt = OffsetDateTime.now();

    /**
     * @deprecated hibernate only
     */
    public CardLock() { }

    public CardLock(Card card, String clientIp, String userAgent) {
        this.card = card;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
}
