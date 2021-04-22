package com.mikkaeru.request.card.repository;

import com.mikkaeru.request.card.model.CardLock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardLockRepository extends CrudRepository<CardLock, Long> {
}
