package com.hapi.hapiservice.models.bot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Element {

    private String title;
    private String subtitle;
    private Integer quantity;
    private Double price;
    private String currency;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("item_url")
    private String itemUrl;
    @JsonProperty("default_action")
    private Button defaultAction;
    private Button[] buttons;
    @JsonProperty("media_type")
    private String mediaType;
    private String url;

    public String getTitle() {
        return title;
    }

    public Element setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Element setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Element setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Element setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Element setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Element setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public Element setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
        return this;
    }

    public Button getDefaultAction() {
        return defaultAction;
    }

    public Element setDefaultAction(Button defaultAction) {
        this.defaultAction = defaultAction;
        return this;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public Element setButtons(Button[] buttons) {
        this.buttons = buttons;
        return this;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Element setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Element setUrl(String url) {
        this.url = url;
        return this;
    }
}
