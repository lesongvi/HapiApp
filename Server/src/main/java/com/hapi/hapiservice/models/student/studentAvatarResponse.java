package com.hapi.hapiservice.models.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class studentAvatarResponse {
    @JsonProperty("120x60")
    private String a120x60;

    @JsonProperty("default")
    private String defaultProperty;

    private String hq;

    private String mq;
}
