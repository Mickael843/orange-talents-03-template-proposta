package com.mikkaeru.proposal.repository;

import com.mikkaeru.proposal.model.Proposal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProposalRepository extends CrudRepository<Proposal, Long> {
    Boolean existsByDocument(String document);
    Optional<Proposal> findByProposalCode(String code);
}
