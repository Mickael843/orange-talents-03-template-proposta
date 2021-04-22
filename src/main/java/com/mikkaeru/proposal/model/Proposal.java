package com.mikkaeru.proposal.model;

import com.mikkaeru.request.card.model.Card;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String document;
    @Column(nullable = false)
    private BigDecimal salary;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, unique = true)
    private String proposalCode;
    @Enumerated(STRING)
    private ProposalState state;
    @ManyToOne(cascade = MERGE)
    private Card card;

    /**
     * @deprecated hibernate only
     */
    public Proposal() { }

    public Proposal(@NotBlank String name, @Email @NotBlank String email, @NotBlank String document,
                    @NotNull @Positive BigDecimal salary, @NotBlank String address, String proposalCode) {
        assertTrue(StringUtils.hasLength(name), "O campo (nome) não pode estar em branco!");
        assertTrue(StringUtils.hasLength(email), "O campo (email) não pode estar em branco!");
        assertTrue(StringUtils.hasLength(address), "O campo (address) não pode estar em branco!");
        assertTrue(StringUtils.hasLength(document), "O campo (document) não pode estar em branco!");

        assertTrue(salary != null, "O campo (salary) não pode ser nulo!");
        assertTrue(proposalCode != null, "O campo (code) não pode ser nulo!");

        this.proposalCode = proposalCode;
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.address = address;
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    public String getProposalCode() {
        return proposalCode;
    }

    public String getEmail() {
        return email;
    }

    public ProposalState getState() {
        return state;
    }

    public void addState(ProposalState state) {
        this.state = state;
    }

    public void addCard(Card card) {
        this.card = card;
    }
}
