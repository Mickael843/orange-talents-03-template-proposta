package com.mikkaeru.request.card.repository;

import com.mikkaeru.request.card.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    Optional<Card> findByCardCode(String cardCode);
}
