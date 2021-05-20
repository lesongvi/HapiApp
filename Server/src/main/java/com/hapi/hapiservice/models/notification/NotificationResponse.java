package com.hapi.hapiservice.models.notification;

public class NotificationResponse {
    private String notification;
    private Long unixtime;

    public NotificationResponse(
            String notification,
            Long unixtime
    ) {
        this.notification = notification;
        this.unixtime = unixtime;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return this.notification;
    }

    public void setUnixtime(Long unixtime) {
        this.unixtime = unixtime;
    }

    public Long getUnixtime() {
        return this.unixtime;
    }
}
