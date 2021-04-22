package com.mikkaeru.request.card.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class CardLock {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String cardCode;
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

    public CardLock(String cardCode, String clientIp, String userAgent) {
        this.cardCode = cardCode;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }
}
