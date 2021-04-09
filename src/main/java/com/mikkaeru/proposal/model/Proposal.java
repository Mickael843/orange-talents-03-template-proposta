package com.mikkaeru.proposal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String document;
    @Column(nullable = false)
    private BigDecimal salary;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, unique = true, updatable = false)
    private UUID code;

    /**
     * @deprecated hibernate only
     */
    public Proposal() { }

    public Proposal(@NotBlank String name, @Email @NotBlank String email, @NotBlank String document,
                    @NotNull @Positive BigDecimal salary, @NotBlank String address, UUID code) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.address = address;
        this.document = document;
    }

    public UUID getCode() {
        return code;
    }
}
