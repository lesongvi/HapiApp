package com.hapi.hapiservice.models.student;

public class routeNationQh {
    private String code;
    private String ma_huyen_edu;
    private String name;
    private String name_with_type;
    private int parent_code;
    private String path;
    private String path_with_type;
    private String slug;
    private String type;

    public routeNationQh(String code, String ma_huyen_edu, String name, String name_with_type, int parent_code, String path, String path_with_type, String slug, String type) {
        this.code = code;
        this.ma_huyen_edu = ma_huyen_edu;
        this.name = name;
        this.name_with_type = name_with_type;
        this.parent_code = parent_code;
        this.path = path;
        this.path_with_type = path_with_type;
        this.slug = slug;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMa_huyen_edu() {
        return ma_huyen_edu;
    }

    public void setMa_huyen_edu(String ma_huyen_edu) {
        this.ma_huyen_edu = ma_huyen_edu;
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

    public int getParent_code() {
        return parent_code;
    }

    public void setParent_code(int parent_code) {
        this.parent_code = parent_code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath_with_type() {
        return path_with_type;
    }

    public void setPath_with_type(String path_with_type) {
        this.path_with_type = path_with_type;
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
