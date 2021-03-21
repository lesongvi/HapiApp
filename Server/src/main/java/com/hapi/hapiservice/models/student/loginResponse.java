package com.hapi.hapiservice.models.student;

public class loginResponse {
    private int[] listmenu_active;

    private studentLoginResponse result;

    private String token;

    public loginResponse setListMenuActive(int[] listmenu_active) {
        this.listmenu_active = listmenu_active;
        return this;
    }

    public int[] getListMenuActive() {
        return this.listmenu_active;
    }

    public loginResponse setResult(studentLoginResponse result) {
        this.result = result;
        return this;
    }

    public studentLoginResponse getResult() {
        return this.result;
    }

    public loginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public String getToken() {
        return this.token;
    }
}
