package com.hapi.hapiservice.models.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountLinking {

    private String status;
    @JsonProperty("authorization_code")
    private String authorizationCode;

    public String getStatus() {
        return status;
    }

    public AccountLinking setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public AccountLinking setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
        return this;
    }
}
