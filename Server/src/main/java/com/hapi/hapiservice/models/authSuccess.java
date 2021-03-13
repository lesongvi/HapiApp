package com.hapi.hapiservice.models;

public class authSuccess {
    final private boolean error;

    final private String tensv;

    final private String token;

    public authSuccess(
            boolean error,
            String tensv,
            String token
    ) {
        this.error = error;
        this.tensv = tensv;
        this.token = token;
    }
}
