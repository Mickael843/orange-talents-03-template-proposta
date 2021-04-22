package com.mikkaeru.proposal.controller;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.proposal.dto.ProposalRequest;
import com.mikkaeru.proposal.dto.ProposalResponse;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.proposal.utils.ProcessProposal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    private final ProcessProposal processProposal;
    private final ProposalRepository proposalRepository;

    public ProposalController(ProcessProposal processProposal, ProposalRepository proposalRepository) {
        this.processProposal = processProposal;
        this.proposalRepository = proposalRepository;
    }

    @PostMapping
    public ResponseEntity<?> createProposal(@RequestBody @Valid ProposalRequest proposalRequest) {
        boolean existsDocument = proposalRepository.existsByDocument(proposalRequest.getDocument());

        if (existsDocument) {
            return ResponseEntity.unprocessableEntity().body(
                    new Problem("Documento invalido!", UNPROCESSABLE_ENTITY.value(), LocalDateTime.now()));
        }

        Proposal proposal = processProposal.process(proposalRepository, proposalRequest.toModel());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(proposal.getProposalCode())
                .toUri()
        ).build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getProposal(@PathVariable String code) {
        Optional<Proposal> proposalOptional = proposalRepository.findByProposalCode(code);

        if (proposalOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ProposalResponse(proposalOptional.get()));
    }
}
