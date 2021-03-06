package com.hapi.hapiservice.models.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Referral {

    private String ref;
    private String source;
    private String type;

    public String getRef() {
        return ref;
    }

    public Referral setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Referral setSource(String source) {
        this.source = source;
        return this;
    }

    public String getType() {
        return type;
    }

    public Referral setType(String type) {
        this.type = type;
        return this;
    }
}
