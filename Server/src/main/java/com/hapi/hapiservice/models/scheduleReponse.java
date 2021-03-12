package com.hapi.hapiservice.models;

public class scheduleReponse {
    public String lop;
    public String mon;
    public String ngaybd;
    public String ngaykt;
    public String nhom;
    public String phong;
    public String sotiet;
    public String thu;
    public String tietbatdau;
    public String tinchi;
    public String giangvien;
    public String un_c1;
    public String un_c2;

    public scheduleReponse(
        String lop,
        String mon,
        String ngaybd,
        String ngaykt,
        String nhom,
        String phong,
        String sotiet,
        String thu,
        String tietbatdau,
        String tinchi,
        String giangvien,
        String un_c1,
        String un_c2
    ) {
        this.lop = lop;
        this.mon = mon;
        this.ngaybd = ngaybd;
        this.ngaykt = ngaykt;
        this.nhom = nhom;
        this.phong = phong;
        this.sotiet = sotiet;
        this.thu = thu;
        this.tietbatdau = tietbatdau;
        this.tinchi = tinchi;
        this.giangvien = giangvien;
        this.un_c1 = un_c1;
        this.un_c2 = un_c2;
    }
}
