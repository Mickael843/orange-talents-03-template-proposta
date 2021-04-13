package com.mikkaeru.request.card;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CardRequestTask {

    private final CardResource cardResource;
    private final ProposalRepository proposalRepository;
    private final Set<Proposal> acceptProposals = new HashSet<>();

    public CardRequestTask(CardResource cardResource, ProposalRepository proposalRepository) {
        this.cardResource = cardResource;
        this.proposalRepository = proposalRepository;
    }

    @Scheduled(fixedDelayString = "${frequency.dummy-task}")
    private void execute() {

        if (acceptProposals.size() > 0) {
            Proposal proposal = acceptProposals.iterator().next();

            CardResponse cardResponse = cardResource.processCard(
                    new CardRequest(proposal.getDocument(), proposal.getName(), proposal.getCode().toString())
            );

            acceptProposals.remove(proposal);

            proposal.addCard(cardResponse.toModel());

            proposalRepository.save(proposal);
        }
    }

    public void addAcceptProposal(Proposal proposal) {
        boolean contains = acceptProposals.contains(proposal);

        if (!contains) {
            acceptProposals.add(proposal);
        }
    }
}
