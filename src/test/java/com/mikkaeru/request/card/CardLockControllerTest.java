package com.mikkaeru.request.card;

import com.mikkaeru.helper.IntegrationHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.request.card.dto.Blocked;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import com.mikkaeru.request.card.model.CardLock;
import com.mikkaeru.request.card.repository.CardLockRepository;
import com.mikkaeru.utils.WebUtils;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardLockControllerTest extends IntegrationHelper {

    @MockBean
    private CardResource cardResource;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private CardLockRepository cardLockRepository;

    private Proposal proposal;

    private static final String ENDPOINT = "/cards/{cardId}/blocks";

    @BeforeEach
    void setUp() {
        proposal = new Proposal(
                "Proposta 02",
                "proposta2@gmail.com",
                "83525826150",
                new BigDecimal("4000"),
                "Travessa das castanheiras",
                UUID.randomUUID().toString());
    }

    @Test
    @DisplayName("Ao fornecer um id valido deve bloquear o cartão")
    void GIVEN_ValidCardId_MUST_LockTheCard() throws Exception {
        when(cardResource.getCard(any()))
                .thenReturn(new CardResponse("5555-6666-7777-8884", proposal.getName(), new BigDecimal("6000"),
                                proposal.getProposalCode(), null, now(), new ArrayList<>(), null));

        when(cardResource.blockCard(any(), any(LockSolicitation.class)))
                .thenReturn(new LockSolicitationResponse("BLOQUEADO"));

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT, proposal.getProposalCode())).andExpect(status().isOk());

        List<CardLock> cardLockList = cardLockRepository.findAll();

        assertThat(cardLockList, hasSize(1));

        verify(cardResource).getCard(proposal.getProposalCode());
        verify(cardResource).blockCard(any(), any(LockSolicitation.class));
    }

    @Test
    @DisplayName("Ao fornecer um id invalido deve retornar status code 404")
    void GIVEN_InvalidCardId_MUST_ReturnNotFound() throws Exception {
        when(cardResource.getCard(any())).thenThrow(FeignException.FeignClientException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT, proposal.getProposalCode())).andExpect(status().isNotFound());

        verify(cardResource).getCard(proposal.getProposalCode());
    }

    @Test
    @DisplayName("Quando o cartão já está bloqueado deve retornar status code 422")
    void WHEN_CardIsLock_MUST_ReturnUnprocessableEntity() throws Exception {
        List<Blocked> locks = List.of(
                new Blocked(UUID.randomUUID().toString(), false, LocalDateTime.now(), webUtils.getUserAgent())
        );

        when(cardResource.getCard(any()))
                .thenReturn(new CardResponse("5555-6666-7777-8884", proposal.getName(), new BigDecimal("6000"),
                        proposal.getProposalCode(), null, now(), locks, null));

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT, proposal.getProposalCode())).andExpect(status().isUnprocessableEntity());

        verify(cardResource).getCard(proposal.getProposalCode());
    }
}