package com.mikkaeru.proposal.utils;

import com.mikkaeru.helper.TestHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.CardRequestTask;
import com.mikkaeru.request.card.CardResource;
import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.solicitation.SolicitationReview;
import com.mikkaeru.request.solicitation.dto.ReviewRequest;
import com.mikkaeru.request.solicitation.dto.ReviewResponse;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.mikkaeru.proposal.model.ProposalState.NOT_ELIGIBLE;
import static com.mikkaeru.request.solicitation.model.SolicitationReviewStatus.SEM_RESTRICAO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProcessProposalTest extends TestHelper {

    private CardResource cardResource;
    private SolicitationReview solicitationReview;
    private ProposalRepository proposalRepository;

    private Proposal proposal;

    private final UUID proposalId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        cardResource = mock(CardResource.class);
        solicitationReview = mock(SolicitationReview.class);
        proposalRepository = mock(ProposalRepository.class);

        String proposalName = "Proposta 01";
        String proposalDocument = "92554025797";

        proposal = new Proposal(
                proposalName,
                "proposta@gmail.com",
                proposalDocument,
                new BigDecimal("422"),
                "Travessa das castanheiras",
                proposalId.toString());

        CardRequest cardRequest = new CardRequest(proposalDocument, proposalName, proposalId.toString());

        when(cardResource.processCard(cardRequest))
                .thenReturn(new CardResponse(
                        "1559-5659-5571-5788",
                        proposalName,
                        new BigDecimal("4905"),
                        proposalId.toString(),
                        null,
                        LocalDateTime.now()
        ));

        when(proposalRepository.save(proposal)).thenReturn(proposal);
    }

    @Test
    @DisplayName("Quando chamar o método process a proposta deve ser processada e deve ser retornado a proposta persistida!")
    void WHEN_CallProcessMethod_MUST_ProcessProposalAndReturnProposalSaved() {
        // Given
        when(solicitationReview.solicitation(any(ReviewRequest.class)))
                .thenReturn(new ReviewResponse(proposal.getDocument(), proposal.getName(), proposal.getProposalCode().toString(), SEM_RESTRICAO.toString()));

        CardRequestTask cardRequestTask = new CardRequestTask(cardResource, proposalRepository);
        ProcessProposal processProposal = new ProcessProposal(cardRequestTask, solicitationReview);

        // When
        Proposal result = processProposal.process(proposalRepository, proposal);

        // Then
        assertEquals(proposal.getName(), result.getName());
        assertEquals(proposal.getProposalCode(), result.getProposalCode());
        assertEquals(proposal.getEmail(), result.getEmail());
        assertEquals(proposal.getState(), result.getState());
        assertEquals(proposal.getDocument(), result.getDocument());
    }

    @Test
    @DisplayName("Ao ser lançada uma exceção do tipos UnprocessableEntity o status da proposta deve ser alterado para NOT_ELIGIBLE")
    void WHEN_CallProcessMethod_MUST_ThrowFeignClientException() {
        // Given
        when(solicitationReview.solicitation(any(ReviewRequest.class)))
                .thenThrow(FeignException.FeignClientException.UnprocessableEntity.class);

        CardRequestTask cardRequestTask = new CardRequestTask(cardResource, proposalRepository);
        ProcessProposal processProposal = new ProcessProposal(cardRequestTask, solicitationReview);

        // When
        Proposal result = processProposal.process(proposalRepository, proposal);

        // Then
        assertEquals(NOT_ELIGIBLE, result.getState());
    }
}