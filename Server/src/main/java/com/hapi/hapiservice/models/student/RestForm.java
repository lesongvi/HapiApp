package com.hapi.hapiservice.models.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestForm {
    @JsonProperty("mon_hoc")
    private String monHoc;
    @JsonProperty("giang_vien")
    private String giangVien;
    @JsonProperty("ngay_nghi")
    private String ngayNghi;
    @JsonProperty("ly_do")
    private String lyDo;
    //"Ca 1", "Ca 2", "Ca 3", "Ca 4", "Ca 5", "Ca sáng", "Ca chiều"
    @JsonProperty("ca")
    private String[] caHoc;

    public RestForm setMonHoc(String monHoc) {
        this.monHoc = monHoc;
        return this;
    }

    public String getMonHoc() {
        return this.monHoc;
    }

    public RestForm setGiangVien(String giangVien) {
        this.giangVien = giangVien;
        return this;
    }

    public String getGiangVien() {
        return this.giangVien;
    }

    public RestForm setNgayNghi(String ngayNghi) {
        this.ngayNghi = ngayNghi;
        return this;
    }

    public String getNgayNghi() {
        return this.ngayNghi;
    }

    public RestForm setLyDo(String lyDo) {
        this.lyDo = lyDo;
        return this;
    }

    public String getLyDo() {
        return this.lyDo;
    }

    public RestForm setCa(String[] caHoc) {
        this.caHoc = caHoc;
        return this;
    }

    public String[] getCaHoc() {
        return this.caHoc;
    }
}
