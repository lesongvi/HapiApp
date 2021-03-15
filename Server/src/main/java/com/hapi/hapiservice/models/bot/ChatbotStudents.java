package com.hapi.hapiservice.models.bot;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChatbotStudents {
    @Id
    private String fid;

    private int sid;

    private String fusername;

    private String currentSemesterId;

    private String currentWeekId;

    public String getFid () {
        return fid;
    }

    public void setFid (String fid) {
        this.fid = fid;
    }

    public int getSid () {
        return sid;
    }

    public void setSid (int sid) {
        this.sid = sid;
    }

    public String getFusername () {
        return fusername;
    }

    public void setFusername (String fusername) {
        this.fusername = fusername;
    }

    public String getCurrentSemesterId () {
        return currentSemesterId;
    }

    public void setCurrentSemesterId (String currentSemesterId) {
        this.currentSemesterId = currentSemesterId;
    }

    public String getCurrentWeekId () {
        return currentWeekId;
    }

    public void setCurrentWeekId (String currentWeekId) {
        this.currentWeekId = currentWeekId;
    }
}
