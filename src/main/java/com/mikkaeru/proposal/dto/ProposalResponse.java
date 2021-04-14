package com.mikkaeru.proposal.dto;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.model.ProposalState;

public class ProposalResponse {

    private String code;
    private String name;
    private String email;
    private ProposalState state;

    public ProposalResponse(Proposal proposal) {
        this.name = proposal.getName();
        this.email = proposal.getEmail();
        this.state = proposal.getState();
        this.code = proposal.getCode().toString();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ProposalState getState() {
        return state;
    }
}
