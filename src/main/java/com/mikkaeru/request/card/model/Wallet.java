package com.mikkaeru.request.card.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.now;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String walletCode;
    @Enumerated(STRING)
    @Column(nullable = false)
    private WalletType walletType;
    @Column(nullable = false)
    private String cardId;
    @CreationTimestamp
    private OffsetDateTime createAt = now();

    /**
     * @deprecated hibernate only
     */
    public Wallet() { }

    public Wallet(String email, String walletCode, WalletType walletType, String cardId) {
        this.email = email;
        this.cardId = cardId;
        this.walletCode = walletCode;
        this.walletType = walletType;
    }

    public String getWalletCode() {
        return walletCode;
    }
}
