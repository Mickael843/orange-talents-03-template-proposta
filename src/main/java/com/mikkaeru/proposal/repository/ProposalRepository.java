package com.mikkaeru.proposal.repository;

import com.mikkaeru.proposal.model.Proposal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends CrudRepository<Proposal, Long> {
}
