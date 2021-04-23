package com.mikkaeru.request.card.repository;

import com.mikkaeru.request.card.model.TravelNotification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelNotificationRepository extends CrudRepository<TravelNotification, Long> {
}
