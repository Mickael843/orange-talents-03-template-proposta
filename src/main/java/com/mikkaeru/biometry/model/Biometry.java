package com.mikkaeru.biometry.model;

import com.mikkaeru.request.card.model.Card;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Biometry {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Lob
    @Column(nullable = false, unique = true)
    private String fingerprint;
    @Column(nullable = false, unique = true)
    private UUID biometryCode;
    @ManyToOne(optional = false)
    private Card card;
    @CreationTimestamp
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /**
     * @deprecated hibernate only
     */
    public Biometry() { }

    public Biometry(String fingerprint, Card card, UUID biometryCode) {
        this.card = card;
        this.fingerprint = fingerprint;
        this.biometryCode = biometryCode;
    }

    public UUID getBiometryCode() {
        return biometryCode;
    }
}
