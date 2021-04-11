package com.mikkaeru.request.model;

import com.mikkaeru.proposal.model.ProposalState;

import static com.mikkaeru.proposal.model.ProposalState.ELIGIBLE;
import static com.mikkaeru.proposal.model.ProposalState.NOT_ELIGIBLE;

public enum SolicitationReviewStatus {
    SEM_RESTRICAO(ELIGIBLE),
    COM_RESTRICAO(NOT_ELIGIBLE);

    ProposalState equivalentStatus;

    SolicitationReviewStatus(ProposalState equivalentStatus) {
        this.equivalentStatus = equivalentStatus;
    }

    public ProposalState getEquivalentStatus() {
        return equivalentStatus;
    }
}
