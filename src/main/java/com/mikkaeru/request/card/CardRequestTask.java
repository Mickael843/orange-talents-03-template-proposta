package com.mikkaeru.request.card;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.repository.CardRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CardRequestTask {

    private final CardResource cardResource;
    private final CardRepository cardRepository;
    private final Set<Proposal> acceptProposals = new HashSet<>();

    public CardRequestTask(CardResource cardResource, CardRepository cardRepository) {
        this.cardResource = cardResource;
        this.cardRepository = cardRepository;
    }

    @Scheduled(fixedDelayString = "${frequency.dummy-task}")
    private void execute() {

        if (acceptProposals.size() > 0) {
            Proposal proposal = acceptProposals.iterator().next();

            CardResponse cardResponse = cardResource.processCard(
                    new CardRequest(proposal.getDocument(), proposal.getName(), proposal.getCode().toString())
            );

            acceptProposals.remove(proposal);

            Card card = cardResponse.toModel();

            card.addProposal(proposal);

            cardRepository.save(card);

            System.out.println("PILHA DE PROPOSTAS NÃO ESTÁ NULA");
        }

        System.out.println("PILHA NULA...");
    }

    public void addAcceptProposal(Proposal proposal) {
        boolean contains = acceptProposals.contains(proposal);

        if (!contains) {
            acceptProposals.add(proposal);
        }
    }
}
