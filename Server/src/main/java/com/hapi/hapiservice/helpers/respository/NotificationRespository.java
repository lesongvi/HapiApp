package com.hapi.hapiservice.helpers.respository;

import com.hapi.hapiservice.models.notification.Notifications;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRespository extends CrudRepository<Notifications, Integer> {
    Notifications findTopByOrderByIDDesc();

    @Query("SELECT n FROM Notifications n WHERE n.THONGBAO <> :notify GROUP BY n.THONGBAO, n.UNIXTIME, n.ID ORDER BY n.UNIXTIME DESC")
    List<Notifications> getAllNotifyButThis(@Param("notify") String notify);
}
