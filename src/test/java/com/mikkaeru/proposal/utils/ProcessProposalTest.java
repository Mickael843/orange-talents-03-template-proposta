package com.mikkaeru.proposal.utils;

import com.mikkaeru.helper.TestHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.CardRequestTask;
import com.mikkaeru.request.card.CardResource;
import com.mikkaeru.request.solicitation.SolicitationReview;
import com.mikkaeru.request.solicitation.dto.ReviewRequest;
import com.mikkaeru.request.solicitation.dto.ReviewResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static com.mikkaeru.proposal.model.ProposalState.NOT_ELIGIBLE;
import static com.mikkaeru.request.solicitation.model.SolicitationReviewStatus.SEM_RESTRICAO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProcessProposalTest extends TestHelper {

    @Mock
    private Span span;
    @Mock
    private Tracer tracer;
    @Mock
    private CardResource cardResource;
    @Mock
    private SolicitationReview solicitationReview;
    @Mock
    private ProposalRepository proposalRepository;

    private Proposal proposal;

    private final UUID proposalId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        String proposalName = "Proposta 01";
        String proposalDocument = "92554025797";

        proposal = new Proposal(
                proposalName,
                "proposta@gmail.com",
                proposalDocument,
                new BigDecimal("422"),
                "Travessa das castanheiras",
                proposalId.toString());

        when(tracer.activeSpan()).thenReturn(span);
        when(proposalRepository.save(proposal)).thenReturn(proposal);
    }

    @Test
    @DisplayName("Quando chamar o método process a proposta deve ser processada e deve ser retornado a proposta persistida!")
    void WHEN_CallProcessMethod_MUST_ProcessProposalAndReturnProposalSaved() {
        // Given
        when(solicitationReview.solicitation(any(ReviewRequest.class)))
                .thenReturn(new ReviewResponse(proposal.getDocument(), proposal.getName(), proposal.getProposalCode(), SEM_RESTRICAO.toString()));

        CardRequestTask cardRequestTask = new CardRequestTask(tracer, cardResource, proposalRepository);
        ProcessProposal processProposal = new ProcessProposal(tracer, cardRequestTask, solicitationReview);

        // When
        Proposal result = processProposal.process(proposalRepository, proposal);

        // Then
        assertThat(result.getName(), equalTo(proposal.getName()));
        assertThat(result.getEmail(), equalTo(proposal.getEmail()));
        assertThat(result.getState(), equalTo(proposal.getState()));
        assertThat(result.getDocument(), equalTo(proposal.getDocument()));
        assertThat(result.getProposalCode(), equalTo(proposal.getProposalCode()));
    }

    @Test
    @DisplayName("Ao ser lançada uma exceção do tipos UnprocessableEntity o status da proposta deve ser alterado para NOT_ELIGIBLE")
    void WHEN_CallProcessMethod_MUST_ThrowFeignClientException() {
        // Given
        when(solicitationReview.solicitation(any(ReviewRequest.class)))
                .thenThrow(FeignException.FeignClientException.UnprocessableEntity.class);

        CardRequestTask cardRequestTask = new CardRequestTask(tracer, cardResource, proposalRepository);
        ProcessProposal processProposal = new ProcessProposal(tracer, cardRequestTask, solicitationReview);

        // When
        Proposal result = processProposal.process(proposalRepository, proposal);

        // Then
        assertThat(result.getState(), equalTo(NOT_ELIGIBLE));
    }
}