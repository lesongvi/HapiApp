package com.hapi.hapiservice.models.student;

public class routeNationDt {
    private int id;
    private String ma_dan_toc_edu;
    private String ten;
    private String ten_goi_khac;

    public routeNationDt(int id, String ma_dan_toc_edu, String ten, String ten_goi_khac) {
        this.id = id;
        this.ma_dan_toc_edu = ma_dan_toc_edu;
        this.ten = ten;
        this.ten_goi_khac = ten_goi_khac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMa_dan_toc_edu() {
        return ma_dan_toc_edu;
    }

    public void setMa_dan_toc_edu(String ma_dan_toc_edu) {
        this.ma_dan_toc_edu = ma_dan_toc_edu;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTen_goi_khac() {
        return ten_goi_khac;
    }

    public void setTen_goi_khac(String ten_goi_khac) {
        this.ten_goi_khac = ten_goi_khac;
    }
}
