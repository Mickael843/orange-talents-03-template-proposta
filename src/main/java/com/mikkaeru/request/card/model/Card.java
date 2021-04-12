package com.mikkaeru.request.card.model;

import com.mikkaeru.proposal.model.Proposal;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String cardNumber;
    @OneToMany(mappedBy = "card", cascade = MERGE)
    private Set<Proposal> proposals = new HashSet<>();

    /**
     * @deprecated hibernate only
     */
    public Card() {
    }

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void addProposal(Proposal proposal) {
        this.proposals.add(proposal);
    }
}
