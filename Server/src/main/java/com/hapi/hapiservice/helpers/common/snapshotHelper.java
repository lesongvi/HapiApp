package com.hapi.hapiservice.helpers.common;

import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.models.notification.NotificationResponse;
import com.hapi.hapiservice.models.notification.Notifications;
import com.hapi.hapiservice.services.NotificationService;

import java.io.IOException;
import java.time.Instant;
import java.util.*;



public class snapshotHelper extends browserHelper {
    private NotificationRespository notificationRespository;
    private NotificationService notificationService;

    public snapshotHelper (
            NotificationRespository notificationRespository,
            NotificationService notificationService
    ) {
        super(0, null, null, null);
        this.notificationRespository = notificationRespository;
        this.notificationService = notificationService;
    }

    public String snapshotNotification() throws IOException {
        Gson gson = new Gson();

        return gson.toJson(this.snapshotNotificationNotify());
    }

    public String snapshotAllNotification() {
        Gson gson = new Gson();

        return gson.toJson(this.snapshotAllNotificationNotify());
    }

    public NotificationResponse snapshotNotificationNotify() throws IOException {
        String init = this.loginAndPattern(this.definedStr.notificationPattern_PRODUCTION(), 1);

        if (init != "")
            init.replaceAll("\\\\n", "");

        Notifications testNotify = this.notificationService.findTopByOrderByIDDesc();
        NotificationResponse response;

        if (init != "" && (testNotify == null || testNotify != null && !testNotify.getTHONGBAO().equals(init))) {
            response = new NotificationResponse(init, Instant.now().getEpochSecond());
            Notifications ntfcation = new Notifications();
            ntfcation.setID(0);
            ntfcation.setTHONGBAO(init);
            ntfcation.setUNIXTIME(Instant.now().getEpochSecond());

            this.notificationService.save(ntfcation);
        } else {
            response = new NotificationResponse(testNotify.getTHONGBAO(), testNotify.getUNIXTIME());
        }

        return response;
    }

    public ArrayList<NotificationResponse> snapshotAllNotificationNotify() {
        ArrayList<NotificationResponse> allNtfy = new ArrayList<>();

        List<Notifications> allNotify = this.notificationService.getAllNotify();

        if (allNotify.size() != 0)
            for (Notifications notify : allNotify) {
                allNtfy.add(new NotificationResponse(notify.getTHONGBAO(), notify.getUNIXTIME()));
            }

        Collections.reverse(allNtfy);
        return allNtfy;
    }
}
