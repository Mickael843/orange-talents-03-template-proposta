package com.mikkaeru.proposal.dto;

import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.model.ProposalState;

import java.util.UUID;

public class ProposalResponse {

    private UUID code;
    private String name;
    private String email;
    private ProposalState state;

    public ProposalResponse(Proposal proposal) {
        this.code = proposal.getCode();
        this.name = proposal.getName();
        this.email = proposal.getEmail();
        this.state = proposal.getState();
    }

    public UUID getCode() {
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
