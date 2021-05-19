package com.hapi.hapiservice.models.schedule;

public class ExamResponse {
    private String mamon;

    private String tenmon;

    private String nhomthi;

    private String tothi;

    private String ngaythi;

    private String giobd;

    private String sophut;

    private String phong;

    private String ghichu;

    public ExamResponse(
            String mamon,
            String tenmon,
            String nhomthi,
            String tothi,
            String ngaythi,
            String giobd,
            String sophut,
            String phong,
            String ghichu
    ) {
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.nhomthi = nhomthi;
        this.tothi = tothi;
        this.ngaythi = ngaythi;
        this.giobd = giobd;
        this.sophut = sophut;
        this.phong = phong;
        this.ghichu = ghichu;
    }

    public void setMamon(String mamon) {
        this.mamon = mamon;
    }

    public String getMamon() {
        return this.mamon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getTenmon() {
        return this.tenmon;
    }

    public void setNhomthi(String nhomthi) {
        this.nhomthi = nhomthi;
    }

    public String getNhomthi() {
        return this.nhomthi;
    }

    public void setTothi(String tothi) {
        this.tothi = tothi;
    }

    public String getTothi() {
        return this.tothi;
    }

    public void setNgaythi(String ngaythi) {
        this.ngaythi = ngaythi;
    }

    public String getNgaythi() {
        return this.ngaythi;
    }

    public void setGiobd(String giobd) {
        this.giobd = giobd;
    }

    public String getGiobd() {
        return this.giobd;
    }

    public void setSophut(String sophut) {
        this.sophut = sophut;
    }

    public String getSophut() {
        return this.sophut;
    }

    public void setPhong(String phong) {
        this.phong = phong;
    }

    public String getPhong() {
        return this.phong;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getGhichu() {
        return this.ghichu;
    }
}
