package com.hapi.hapiservice.models.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Postback {

    private String payload;
    private Referral referral;

    public String getPayload() {
        return payload;
    }

    public Postback setPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public Referral getReferral() {
        return referral;
    }

    public Postback setReferral(Referral referral) {
        this.referral = referral;
        return this;
    }
}
