package com.mikkaeru.request.card;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CardRequestTask {

    private final Tracer tracer;
    private final CardResource cardResource;
    private final ProposalRepository proposalRepository;
    private final Set<Proposal> acceptProposals = new HashSet<>();

    public CardRequestTask(Tracer tracer, CardResource cardResource, ProposalRepository proposalRepository) {
        this.tracer = tracer;
        this.cardResource = cardResource;
        this.proposalRepository = proposalRepository;
    }

    @Scheduled(fixedDelayString = "${frequency.request-task}")
    public void execute() {

        if (acceptProposals.size() > 0) {
            CardResponse cardResponse = null;
            Span activeSpan = tracer.activeSpan();
            Proposal proposal = acceptProposals.iterator().next();

            try {
                activeSpan.log("Realizando requisição para o sistema de cartão - recurso de criação de cartão!");
                cardResponse = cardResource.processCard(new CardRequest(
                        proposal.getDocument(), proposal.getName(), proposal.getProposalCode().toString()
                ));
            } catch (FeignException.FeignClientException.NotFound ignored) { }

            if (cardResponse != null) {
                acceptProposals.remove(proposal);
                proposal.addCard(cardResponse.toModel());
                proposalRepository.save(proposal);
            }
        }
    }

    public void addAcceptProposal(Proposal proposal) {
        boolean contains = acceptProposals.contains(proposal);

        if (!contains) {
            acceptProposals.add(proposal);
            Span activeSpan = tracer.activeSpan();
            activeSpan.log("Adicionando proposta aceita!");
        }
    }
}
