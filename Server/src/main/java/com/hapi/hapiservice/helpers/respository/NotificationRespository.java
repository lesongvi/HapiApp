package com.hapi.hapiservice.helpers.respository;

import com.hapi.hapiservice.models.notification.Notifications;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRespository extends CrudRepository<Notifications, Integer> {
    Notifications findTopByOrderByIDDesc();
}
