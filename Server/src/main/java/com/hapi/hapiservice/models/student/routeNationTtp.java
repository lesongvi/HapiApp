package com.hapi.hapiservice.models.student;

public class routeNationTtp {
    private int code;
    private String ma_tinh_edu;
    private String name;
    private String name_with_type;
    private String slug;
    private String type;

    public routeNationTtp(int code, String ma_tinh_edu, String name, String name_with_type, String slug, String type) {
        this.code = code;
        this.ma_tinh_edu = ma_tinh_edu;
        this.name = name;
        this.name_with_type = name_with_type;
        this.slug = slug;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMa_tinh_edu() {
        return ma_tinh_edu;
    }

    public void setMa_tinh_edu(String ma_tinh_edu) {
        this.ma_tinh_edu = ma_tinh_edu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_with_type() {
        return name_with_type;
    }

    public void setName_with_type(String name_with_type) {
        this.name_with_type = name_with_type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
