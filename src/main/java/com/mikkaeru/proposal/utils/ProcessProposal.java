package com.mikkaeru.proposal.utils;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.SolicitationReview;
import com.mikkaeru.request.dto.ReviewRequest;
import com.mikkaeru.request.dto.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ProcessProposal {

    private final SolicitationReview solicitationReview;

    public ProcessProposal(SolicitationReview solicitationReview) {
        this.solicitationReview = solicitationReview;
    }

    public Proposal process(ProposalRepository proposalRepository, Proposal proposal) {
        ReviewResponse reviewResponse = solicitationReview.solicitation(
                new ReviewRequest(proposal.getDocument(), proposal.getName(), proposal.getCode().toString()));

        proposal.addState(reviewResponse.getResultadoSolicitacao().getEquivalentStatus());

        return proposalRepository.save(proposal);
    }
}
