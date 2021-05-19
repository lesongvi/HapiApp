package com.hapi.hapiservice.models.schedule;

public class authSuccess {
    final private boolean error;

    final private String tensv;

    final private String token;

    final private String email;

    final private String sid;

    final private String sdt1;

    final private String sdt2;

    public authSuccess(
            boolean error,
            String tensv,
            String token,
            String email,
            String sid,
            String sdt1,
            String sdt2
    ) {
        this.error = error;
        this.tensv = tensv;
        this.token = token;
        this.email = email;
        this.sid = sid;
        this.sdt1 = sdt1;
        this.sdt2 = sdt2;
    }
}
