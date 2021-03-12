package com.hapi.hapiservice.models;

public class authSuccess {
    final private String status;

    final private String tensv;

    final private String token;

    public authSuccess(
            String status,
            String tensv,
            String token
    ) {
        this.status = status;
        this.tensv = tensv;
        this.token = token;
    }
}
