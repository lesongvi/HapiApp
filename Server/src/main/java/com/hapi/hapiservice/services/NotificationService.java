package com.hapi.hapiservice.services;

import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.models.notification.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    protected NotificationRespository notificationRespository;

    public void save(Notifications notify) {
        notificationRespository.save(notify);
    }

    public Optional<Notifications> findById(int id) {
        return this.notificationRespository.findById(id);
    }

    public Notifications findTopByOrderByIDDesc() {
        return this.notificationRespository.findTopByOrderByIDDesc();
    }
    public List<Notifications> getAllNotify() {
        return (List<Notifications>) this.notificationRespository.findAll();
    }
    public List<Notifications> getAllNotifyButThis(String notify) {
        return this.notificationRespository.getAllNotifyButThis(notify);
    }
}
