package com.hapi.hapiservice.models.notification;

public class NotificationResponse {
    private String notification;

    public NotificationResponse(
            String notification
    ) {
        this.notification = notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return this.notification;
    }
}
