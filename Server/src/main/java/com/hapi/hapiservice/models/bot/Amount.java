package com.hapi.hapiservice.models.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Amount {

    private String name;
    private String currency;
    private String amount;
    private String title;
    private String label;

    public String getName() {
        return name;
    }

    public Amount setName(String name) {
        this.name = name;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Amount setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Amount setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Amount setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Amount setLabel(String label) {
        this.label = label;
        return this;
    }
}
