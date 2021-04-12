package com.mikkaeru.proposal.controller;

import com.mikkaeru.exception.Problem;
import com.mikkaeru.proposal.dto.ProposalRequest;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.proposal.utils.ProcessProposal;
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

    // TODO Encontrar uma forma de controlar o momento em que o método execute é chamado

    // TODO Adicionar as propostas que foram geradas com sucesso em uma pilha de execução.
    // TODO Realizar a consulta em tempo periódico o sistema de cartão.
    // TODO Associar o número do cartão na proposta previamente criada com sucesso.
    // TODO O cartão deve ser persistido de acordo com as informações recebidas do sistema externo.

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
                .buildAndExpand(proposal.getCode())
                .toUri()
        ).build();
    }
}
