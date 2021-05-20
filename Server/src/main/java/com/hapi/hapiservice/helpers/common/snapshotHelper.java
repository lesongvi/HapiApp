package com.hapi.hapiservice.helpers.common;

import com.gargoylesoftware.htmlunit.WebClient;
import com.google.gson.Gson;
import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.models.notification.NotificationResponse;
import com.hapi.hapiservice.models.notification.Notifications;
import com.hapi.hapiservice.services.NotificationService;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


class ASCByUnixtime implements Comparator<NotificationResponse>
{
    public int compare(NotificationResponse a, NotificationResponse b)
    {
        return (int) (a.getUnixtime() - b.getUnixtime());
    }
}

public class snapshotHelper extends browserHelper {
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

    public String snapshotAllNotification() throws IOException {
        Gson gson = new Gson();

        return gson.toJson(this.snapshotAllNotificationNotify());
    }

    public NotificationResponse snapshotNotificationNotify() throws IOException {
        String init = this.loginAndPattern(this.definedStr.notificationPattern_PRODUCTION(), 1);

        if (init != "")
            init.replaceAll("\\\\n", "");

        Notifications testNotify = this.notificationService.findTopByOrderByIDDesc();
        NotificationResponse response;

        if (init != "" && (testNotify == null && !testNotify.getTHONGBAO().equals(init))) {
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
