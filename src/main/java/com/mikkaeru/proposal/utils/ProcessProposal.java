package com.mikkaeru.proposal.utils;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.CardRequestTask;
import com.mikkaeru.request.solicitation.SolicitationReview;
import com.mikkaeru.request.solicitation.dto.ReviewRequest;
import com.mikkaeru.request.solicitation.dto.ReviewResponse;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.mikkaeru.proposal.model.ProposalState.ELIGIBLE;
import static com.mikkaeru.proposal.model.ProposalState.NOT_ELIGIBLE;

@Component
public class ProcessProposal {

    private final Tracer tracer;
    private final CardRequestTask cardRequestTask;
    private final SolicitationReview solicitationReview;

    public ProcessProposal(Tracer tracer, CardRequestTask cardRequestTask, SolicitationReview solicitationReview) {
        this.tracer = tracer;
        this.cardRequestTask = cardRequestTask;
        this.solicitationReview = solicitationReview;
    }

    public Proposal process(ProposalRepository proposalRepository, final Proposal proposal) {
        Span activeSpan = tracer.activeSpan();
        Optional<ReviewResponse> reviewResponse = Optional.empty();

        try {
            activeSpan.log("Realizando requisição ao sistema de solicitações!");
            reviewResponse = Optional.ofNullable(solicitationReview.solicitation(
                    new ReviewRequest(proposal.getDocument(), proposal.getName(), proposal.getProposalCode())));
        } catch (FeignException.FeignClientException.UnprocessableEntity e) {
            proposal.addState(NOT_ELIGIBLE);
        }

        reviewResponse.ifPresent(r -> proposal.addState(r.getResultadoSolicitacao().getEquivalentStatus()));

        Proposal proposalSaved = proposalRepository.save(proposal);

        if (proposalSaved.getState().equals(ELIGIBLE)) {
            activeSpan.setBaggageItem("eligible.proposal", proposalSaved.getProposalCode());
            cardRequestTask.addAcceptProposal(proposalSaved);
        }

        return proposalSaved;
    }
}
