package com.mikkaeru.request.card.repository;

import com.mikkaeru.request.card.model.CardLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardLockRepository extends JpaRepository<CardLock, Long> {
}
