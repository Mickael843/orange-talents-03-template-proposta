package com.mikkaeru.proposal.utils;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.CardRequestTask;
import com.mikkaeru.request.solicitation.SolicitationReview;
import com.mikkaeru.request.solicitation.dto.ReviewRequest;
import com.mikkaeru.request.solicitation.dto.ReviewResponse;
import org.springframework.stereotype.Component;

import static com.mikkaeru.proposal.model.ProposalState.ELIGIBLE;

@Component
public class ProcessProposal {

    private final CardRequestTask cardRequestTask;
    private final SolicitationReview solicitationReview;

    public ProcessProposal(CardRequestTask cardRequestTask, SolicitationReview solicitationReview) {
        this.cardRequestTask = cardRequestTask;
        this.solicitationReview = solicitationReview;
    }

    public Proposal process(ProposalRepository proposalRepository, Proposal proposal) {
        ReviewResponse reviewResponse = solicitationReview.solicitation(
                new ReviewRequest(proposal.getDocument(), proposal.getName(), proposal.getCode().toString()));

        proposal.addState(reviewResponse.getResultadoSolicitacao().getEquivalentStatus());

        proposal = proposalRepository.save(proposal);

        if (proposal.getState().equals(ELIGIBLE)) {
            cardRequestTask.addAcceptProposal(proposal);
        }

        return proposal;
    }
}
