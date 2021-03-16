package com.hapi.hapiservice.models.notification;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer ID;

    private String THONGBAO;

    private long UNIXTIME;

    public void setID (int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setTHONGBAO (String THONGBAO) {
        this.THONGBAO = THONGBAO;
    }

    public String getTHONGBAO() {
        return this.THONGBAO;
    }

    public void setUNIXTIME (long UNIXTIME) {
        this.UNIXTIME = UNIXTIME;
    }

    public long getUNIXTIME() {
        return this.UNIXTIME;
    }
}
