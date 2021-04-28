package com.mikkaeru.request.card;

import com.mikkaeru.helper.TestHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.mockito.Mockito.*;

class CardRequestTaskTest extends TestHelper {

    @Mock
    private Span span;
    @Mock
    private Tracer tracer;
    @Mock
    private CardResource cardResource;
    @Mock
    private ProposalRepository proposalRepository;

    private Proposal proposalOne;
    private CardResponse cardResponseOne;

    @BeforeEach
    void setUp() {
        String proposalName = "Proposta 01";
        String proposalDocument = "92554025797";

        proposalOne = new Proposal(
                proposalName,
                "proposta1@gmail.com",
                proposalDocument,
                new BigDecimal("1000"),
                "Travessa das castanheiras",
                UUID.randomUUID().toString());

        cardResponseOne = new CardResponse(
                "5555-6666-7777-8884", proposalOne.getName(), new BigDecimal("6000"), proposalOne.getProposalCode(), null, now(), null, null);

        when(tracer.activeSpan()).thenReturn(span);
    }

    @Test
    @DisplayName("Ao fornecer uma proposta aceita deve executar a requisição ao serviço de cartão")
    void GIVEN_ValidProposal_MUST_ExecuteFeignRequestAndUpdateProposal() {
        // Given
        when(cardResource.processCard(any(CardRequest.class))).thenReturn(cardResponseOne);

        CardRequestTask cardRequestTask = new CardRequestTask(tracer, cardResource, proposalRepository);
        cardRequestTask.addAcceptProposal(proposalOne);

        // When
        cardRequestTask.execute();

        // Then
        verify(cardResource, atLeastOnce()).processCard(any(CardRequest.class));
        verify(proposalRepository, atLeastOnce()).save(proposalOne);
    }

    @Test
    @DisplayName("Quando a requisição lançar uma exceção, não deve atualizar a proposta")
    void WHEN_FeignRequestThrowException_MUST_NotUpdateProposal() {
        // Given
        when(cardResource.processCard(any(CardRequest.class))).thenThrow(FeignException.FeignClientException.NotFound.class);

        CardRequestTask cardRequestTask = new CardRequestTask(tracer, cardResource, proposalRepository);
        cardRequestTask.addAcceptProposal(proposalOne);

        // When
        cardRequestTask.execute();

        // Then
        verify(cardResource, atLeastOnce()).processCard(any(CardRequest.class));
        verify(proposalRepository, times(0)).save(proposalOne);
    }

    @Test
    @DisplayName("Não deve adicionar propostas repetidas")
    void MUST_NotAddRepeatedProposals() {
        // Given
        Proposal proposalTwo = new Proposal(
                "Proposta 02",
                "proposta2@gmail.com",
                "83525826150",
                new BigDecimal("4000"),
                "Travessa das castanheiras",
                UUID.randomUUID().toString());

        CardResponse cardResponseTwo = new CardResponse(
                "5555-6666-7777-8884", proposalTwo.getName(), new BigDecimal("6000"), proposalTwo.getProposalCode(), null, now(), null, null);

        when(cardResource.processCard(any(CardRequest.class))).thenReturn(cardResponseOne, cardResponseTwo);

        CardRequestTask cardRequestTask = new CardRequestTask(tracer, cardResource, proposalRepository);
        cardRequestTask.addAcceptProposal(proposalOne);
        cardRequestTask.addAcceptProposal(proposalOne);
        cardRequestTask.addAcceptProposal(proposalTwo);
        cardRequestTask.addAcceptProposal(proposalTwo);

        // When
        for (int i = 0; i <= 4; i++) {
            cardRequestTask.execute();
        }

        // Then
        verify(cardResource, times(2)).processCard(any(CardRequest.class));
        verify(proposalRepository, times(2)).save(any(Proposal.class));
    }
}