package com.mikkaeru.proposal.controller;

import com.mikkaeru.proposal.dto.ProposalRequest;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    private final ProposalRepository proposalRepository;

    public ProposalController(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createProposal(@RequestBody @Valid ProposalRequest proposalRequest) {
        Proposal proposal = proposalRepository.save(proposalRequest.toModel());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(proposal.getCode())
                .toUri()
        ).build();
    }
}
