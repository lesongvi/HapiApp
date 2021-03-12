package com.hapi.hapiservice.models;

public class errorResponse {
    final private boolean error;

    final private String message;

    private String token = null;

    private int code = 500;

    public errorResponse (
            boolean error,
            String message,
            int code
    ) {
        this.error = error;
        this.message = message;
        this.code = code;
    }
}
