package com.hapi.hapiservice.helpers.common;

import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.models.notification.NotificationResponse;
import com.hapi.hapiservice.models.notification.Notifications;
import com.hapi.hapiservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;

public class snapshotHelper extends browserHelper {
    final WebClient webClient = this.initial();
    private NotificationRespository notificationRespository;
    private NotificationService notificationService;

    public snapshotHelper (
            NotificationRespository notificationRespository,
            NotificationService notificationService
    ) {
        super(1811061712, "Lesongvi123321", null, null);
        this.notificationRespository = notificationRespository;
        this.notificationService = notificationService;
    }

    public String snapshotNotification() throws IOException {
        Gson gson = new Gson();

        return gson.toJson(this.snapshotNotificationNotify());
    }

    public NotificationResponse snapshotNotificationNotify() throws IOException {

        String init = this.loginAndPattern(this.definedStr.notificationPattern_PRODUCTION(), 1);

        if (init != "")
            init.replaceAll("\\\\n", "");

        Notifications testNotify = this.notificationService.findTopByOrderByIDDesc();
        NotificationResponse response;

        if (init != "" && (testNotify == null || testNotify.getUNIXTIME() < Instant.now().getEpochSecond() - 86400)) {
            response = new NotificationResponse(init);
            Notifications ntfcation = new Notifications();
            ntfcation.setID(0);
            ntfcation.setTHONGBAO(init);
            ntfcation.setUNIXTIME(Instant.now().getEpochSecond());

            this.notificationService.save(ntfcation);
        } else {
            response = new NotificationResponse(testNotify.getTHONGBAO());
        }

        return response;
    }
}
