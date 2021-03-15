package com.hapi.hapiservice.models.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.swing.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bot {
    private String id;
    private String name;
    private Icon icons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Icon getIcons() {
        return icons;
    }

    public void setIcons(Icon icons) {
        this.icons = icons;
    }
}
