package com.mikkaeru.proposal.controller;

import com.mikkaeru.proposal.helper.IntegrationHelper;
import com.mikkaeru.proposal.utils.ProcessProposal;
import com.mikkaeru.request.card.CardRequestTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class ProposalControllerTest extends IntegrationHelper {

    private static final String ENDPOINT = "/proposals";

    @MockBean
    ProcessProposal processProposal;

    @MockBean
    CardRequestTask cardRequestTask;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void dummy() throws Exception {
//        var document = "19516577300";
//        var name = faker.name().fullName();
//        var address = faker.address().city();
//        var email = faker.name().firstName() + "@gmail.com";
//        var salary = new BigDecimal(faker.number().numberBetween(500, 10000));
//
//        JSONObject json = new JSONObject()
//                .put("name", name)
//                .put("email", email)
//                .put("salary", salary)
//                .put("address", address)
//                .put("document", document);
//
//        mockMvc.perform(post(ENDPOINT)
//                .contentType(APPLICATION_JSON)
//                .content(json.toString()))
//                .andExpect(status().isCreated());
    }
}