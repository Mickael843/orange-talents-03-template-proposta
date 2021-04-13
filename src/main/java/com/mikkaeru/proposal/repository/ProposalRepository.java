package com.mikkaeru.proposal.repository;

import com.mikkaeru.proposal.model.Proposal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProposalRepository extends CrudRepository<Proposal, Long> {
    Boolean existsByDocument(String document);
    Optional<Proposal> findByCode(UUID code);
}
