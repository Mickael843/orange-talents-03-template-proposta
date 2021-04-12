package com.mikkaeru.request.card.repository;

import com.mikkaeru.request.card.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
}
