package com.mikkaeru.request.card.model;

import com.mikkaeru.biometry.model.Biometry;
import com.mikkaeru.proposal.model.Proposal;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String cardNumber;
    @Column(nullable = false)
    private String owner;
    @Column(nullable = false)
    private BigDecimal limitValue;
    @Column(nullable = false)
    private UUID code;
    private String renegotiation;
    @Column(nullable = false)
    private LocalDateTime issuedOn;
    @OneToMany(mappedBy = "card")
    private Set<Proposal> proposals = new HashSet<>();
    @OneToMany(mappedBy = "card", cascade = MERGE)
    private Set<Biometry> biometrics = new HashSet<>();

    /**
     * @deprecated hibernate only
     */
    public Card() { }

    public Card(String cardNumber, String titular, BigDecimal limite, UUID idProposta, LocalDateTime emitidoEm, String renegociacao) {

        this.cardNumber = cardNumber;
        this.owner = titular;
        this.limitValue = limite;
        this.code = idProposta;
        this.issuedOn = emitidoEm;
        this.renegotiation = renegociacao;
    }
}
