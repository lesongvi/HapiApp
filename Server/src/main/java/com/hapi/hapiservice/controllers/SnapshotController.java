package com.hapi.hapiservice.controllers;

import com.hapi.hapiservice.helpers.common.routeHelper;
import com.hapi.hapiservice.helpers.common.snapshotHelper;
import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SnapshotController {
    @Autowired
    private NotificationRespository notificationRespository;
    @Autowired
    private NotificationService notificationService;

    private snapshotHelper studentBasicSnapshot;

    @RequestMapping(value = {routeHelper.snapshotNotification}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String snapshotNotification () throws IOException {
        this.studentBasicSnapshot = new snapshotHelper(this.notificationRespository, this.notificationService);

        return this.studentBasicSnapshot.snapshotNotification();
    }
}
