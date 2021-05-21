package com.hapi.hapiservice.models.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Students {
    @Id
    private int sid;

    private String name;

    private String email;

    private String sdt1;

    private String sdt2;

    private String token;

    private String avatar;

    @Column(nullable = false)
    private String pwd;

    public int getSid() {
        return sid;
    }

    public void setId(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt(int id) {
        if (id == 1)
            return this.sdt1;
        return this.sdt2;
    }

    public void setSdt(int id, String sdt) {
        if (id == 1)
            this.sdt1 = sdt;
        else
            this.sdt2 = sdt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
