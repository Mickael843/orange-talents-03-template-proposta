package com.mikkaeru.proposal.validator;

import com.mikkaeru.proposal.dto.ProposalRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;

class ProposalGroupSequenceProviderTest {

    @Test
    void WHEN_ProvideValidCpf_MUST_ReturnCpfGroup() {
        ProposalGroupSequenceProvider provider = new ProposalGroupSequenceProvider();

        List<Class<?>> groups = provider.getValidationGroups(new ProposalRequest(
                "Proposta 01",
                "proposta01@gmail.com",
                "925.540.257-97",
                new BigDecimal("4000"),
                "Travessa das castanheiras"
        ));

        assertThat(groups.size(), equalTo(2));
        assertThat(groups.get(groups.size() - 1).getName(), containsStringIgnoringCase("CpfGroup"));
    }

    @Test
    void WHEN_ProvideValidCnpj_MUST_ReturnCnpjGroup() {
        ProposalGroupSequenceProvider provider = new ProposalGroupSequenceProvider();

        List<Class<?>> groups = provider.getValidationGroups(new ProposalRequest(
                "Proposta 01",
                "ecommerce01@gmail.com",
                "23.767.530/0001-65",
                new BigDecimal("10000"),
                "Travessa das castanheiras"
        ));

        assertThat(groups.size(), equalTo(2));
        assertThat(groups.get(groups.size() - 1).getName(), containsStringIgnoringCase("CnpjGroup"));
    }
}