package com.mikkaeru.proposal.model;

import org.springframework.util.StringUtils;

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
    @Column(nullable = false, unique = true, updatable = false)
    private UUID code;

    /**
     * @deprecated hibernate only
     */
    public Proposal() { }

    public Proposal(@NotBlank String name, @Email @NotBlank String email, @NotBlank String document,
                    @NotNull @Positive BigDecimal salary, @NotBlank String address, UUID code) {
        assertTrue(StringUtils.hasLength(name), "O campo (nome) não pode estar em branco!");
        assertTrue(StringUtils.hasLength(email), "O campo (email) não pode estar em branco!");
        assertTrue(StringUtils.hasLength(address), "O campo (address) não pode estar em branco!");
        assertTrue(StringUtils.hasLength(document), "O campo (document) não pode estar em branco!");

        assertTrue(salary != null, "O campo (salary) não pode ser nulo!");
        assertTrue(code != null, "O campo (code) não pode ser nulo!");

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
