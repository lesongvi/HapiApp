package com.hapi.hapiservice.models.student;

public class routeNationPx {
    private String code;
    private String ma_huyen_code;
    private String ma_huyen_edu;
    private String ma_xa_edu;
    private String name;
    private String slug;

    public routeNationPx(String code, String ma_huyen_code, String ma_huyen_edu, String ma_xa_edu, String name, String slug) {
        this.code = code;
        this.ma_huyen_code = ma_huyen_code;
        this.ma_huyen_edu = ma_huyen_edu;
        this.ma_xa_edu = ma_xa_edu;
        this.name = name;
        this.slug = slug;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMa_huyen_code() {
        return ma_huyen_code;
    }

    public void setMa_huyen_code(String ma_huyen_code) {
        this.ma_huyen_code = ma_huyen_code;
    }

    public String getMa_huyen_edu() {
        return ma_huyen_edu;
    }

    public void setMa_huyen_edu(String ma_huyen_edu) {
        this.ma_huyen_edu = ma_huyen_edu;
    }

    public String getMa_xa_edu() {
        return ma_xa_edu;
    }

    public void setMa_xa_edu(String ma_xa_edu) {
        this.ma_xa_edu = ma_xa_edu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
