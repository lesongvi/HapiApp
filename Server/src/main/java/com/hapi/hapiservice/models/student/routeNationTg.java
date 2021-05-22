package com.hapi.hapiservice.models.student;

public class routeNationTg {
    private int id;
    private String ma_ton_giao_edu;
    private String ten;

    public routeNationTg(int id, String ma_ton_giao_edu, String ten) {
        this.id = id;
        this.ma_ton_giao_edu = ma_ton_giao_edu;
        this.ten = ten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMa_ton_giao_edu() {
        return ma_ton_giao_edu;
    }

    public void setMa_ton_giao_edu(String ma_ton_giao_edu) {
        this.ma_ton_giao_edu = ma_ton_giao_edu;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
