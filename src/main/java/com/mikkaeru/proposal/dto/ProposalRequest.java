package com.mikkaeru.proposal.dto;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.validator.CnpjGroup;
import com.mikkaeru.proposal.validator.CpfGroup;
import com.mikkaeru.proposal.validator.ProposalGroupSequenceProvider;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

import static com.mikkaeru.proposal.utils.DocumentReplace.replaceAll;

@GroupSequenceProvider(ProposalGroupSequenceProvider.class)
public class ProposalRequest {

    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    private @NotBlank String document;
    private @NotBlank String name;
    private @NotBlank String address;
    private @NotBlank @Email String email;
    private @NotNull @Positive BigDecimal salary;

    public ProposalRequest(@NotBlank String name, @NotBlank @Email String email, @NotBlank String document,
                           @NotNull @Positive BigDecimal salary, @NotNull String address) {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.address = address;
        this.document = replaceAll(document);
    }

    public Proposal toModel() {
        return new Proposal(name, email, document, salary, address, UUID.randomUUID().toString());
    }

    public String getDocument() {
        return document;
    }
}
