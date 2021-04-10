package com.mikkaeru.proposal.controller;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.proposal.dto.ProposalRequest;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.model.ProposalState;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.SolicitationReview;
import com.mikkaeru.request.dto.ReviewRequest;
import com.mikkaeru.request.dto.ReviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/proposals")
public class ProposalController {

    private final ProposalRepository proposalRepository;
    private SolicitationReview solicitationReview;

    public ProposalController(ProposalRepository proposalRepository, SolicitationReview solicitationReview) {
        this.proposalRepository = proposalRepository;
        this.solicitationReview = solicitationReview;
    }


    @PostMapping
    public ResponseEntity<?> createProposal(@RequestBody @Valid ProposalRequest proposalRequest) {
        boolean existsDocument = proposalRepository.existsByDocument(proposalRequest.getDocument());

        if (existsDocument) {
            return ResponseEntity.unprocessableEntity().body(
                    new Problem("Documento invalido!", UNPROCESSABLE_ENTITY.value(), LocalDateTime.now()));
        }

        Proposal proposal = proposalRepository.save(proposalRequest.toModel());

        ReviewResponse reviewResponse = solicitationReview.solicitation(
                new ReviewRequest(proposal.getDocument(), proposal.getName(), proposal.getCode().toString())
        );

        if (reviewResponse.getResultadoSolicitacao().equals("COM_RESTRICAO")) {
            proposal.addState(ProposalState.valueOf("NOT_ELIGIBLE"));
        } else if (reviewResponse.getResultadoSolicitacao().equals("SEM_RESTRICAO")) {
            proposal.addState(ProposalState.valueOf("ELIGIBLE"));
        }

        // TODO Pensar em uma melhor solução para esse trecho de codigo
        // TODO em relação a ter uma ou mais transações abertas.
        proposal = proposalRepository.save(proposal);

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(proposal.getCode())
                .toUri()
        ).build();
    }
}
