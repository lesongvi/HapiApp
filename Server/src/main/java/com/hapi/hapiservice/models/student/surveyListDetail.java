package com.hapi.hapiservice.models.student;

public class surveyListDetail {
    public String ma_don_vi;
    public String ten_don_vi;
    public String loai_khao_sat;
    public String lop_sinh_hoat;
    public String ten_sinh_vien;
    public String trinh_do_dao_tao;
    public int phieu_khao_sat_id;
    public boolean lop_dem;
    public String ma_mon_hoc;
    public String ten_mon_hoc;
    public String ma_giang_vien;
    public String loai_giang_day;
    public String ten_giang_vien;
    public String lop_thuc_te_ghep;

    public surveyListDetail(String ma_don_vi, String ten_don_vi, String loai_khao_sat, String lop_sinh_hoat, String ten_sinh_vien, String trinh_do_dao_tao, int phieu_khao_sat_id, boolean lop_dem, String ma_mon_hoc, String ten_mon_hoc, String ma_giang_vien, String loai_giang_day, String ten_giang_vien, String lop_thuc_te_ghep) {
        this.ma_don_vi = ma_don_vi;
        this.ten_don_vi = ten_don_vi;
        this.loai_khao_sat = loai_khao_sat;
        this.lop_sinh_hoat = lop_sinh_hoat;
        this.ten_sinh_vien = ten_sinh_vien;
        this.trinh_do_dao_tao = trinh_do_dao_tao;
        this.phieu_khao_sat_id = phieu_khao_sat_id;
        this.lop_dem = lop_dem;
        this.ma_mon_hoc = ma_mon_hoc;
        this.ten_mon_hoc = ten_mon_hoc;
        this.ma_giang_vien = ma_giang_vien;
        this.loai_giang_day = loai_giang_day;
        this.ten_giang_vien = ten_giang_vien;
        this.lop_thuc_te_ghep = lop_thuc_te_ghep;
    }

    public String getMa_don_vi() {
        return ma_don_vi;
    }

    public void setMa_don_vi(String ma_don_vi) {
        this.ma_don_vi = ma_don_vi;
    }

    public String getTen_don_vi() {
        return ten_don_vi;
    }

    public void setTen_don_vi(String ten_don_vi) {
        this.ten_don_vi = ten_don_vi;
    }

    public String getLoai_khao_sat() {
        return loai_khao_sat;
    }

    public void setLoai_khao_sat(String loai_khao_sat) {
        this.loai_khao_sat = loai_khao_sat;
    }

    public String getLop_sinh_hoat() {
        return lop_sinh_hoat;
    }

    public void setLop_sinh_hoat(String lop_sinh_hoat) {
        this.lop_sinh_hoat = lop_sinh_hoat;
    }

    public String getTen_sinh_vien() {
        return ten_sinh_vien;
    }

    public void setTen_sinh_vien(String ten_sinh_vien) {
        this.ten_sinh_vien = ten_sinh_vien;
    }

    public String getTrinh_do_dao_tao() {
        return trinh_do_dao_tao;
    }

    public void setTrinh_do_dao_tao(String trinh_do_dao_tao) {
        this.trinh_do_dao_tao = trinh_do_dao_tao;
    }

    public int getPhieu_khao_sat_id() {
        return phieu_khao_sat_id;
    }

    public void setPhieu_khao_sat_id(int phieu_khao_sat_id) {
        this.phieu_khao_sat_id = phieu_khao_sat_id;
    }

    public boolean isLop_dem() {
        return lop_dem;
    }

    public void setLop_dem(boolean lop_dem) {
        this.lop_dem = lop_dem;
    }

    public String getMa_mon_hoc() {
        return ma_mon_hoc;
    }

    public void setMa_mon_hoc(String ma_mon_hoc) {
        this.ma_mon_hoc = ma_mon_hoc;
    }

    public String getTen_mon_hoc() {
        return ten_mon_hoc;
    }

    public void setTen_mon_hoc(String ten_mon_hoc) {
        this.ten_mon_hoc = ten_mon_hoc;
    }

    public String getMa_giang_vien() {
        return ma_giang_vien;
    }

    public void setMa_giang_vien(String ma_giang_vien) {
        this.ma_giang_vien = ma_giang_vien;
    }

    public String getLoai_giang_day() {
        return loai_giang_day;
    }

    public void setLoai_giang_day(String loai_giang_day) {
        this.loai_giang_day = loai_giang_day;
    }

    public String getTen_giang_vien() {
        return ten_giang_vien;
    }

    public void setTen_giang_vien(String ten_giang_vien) {
        this.ten_giang_vien = ten_giang_vien;
    }

    public String getLop_thuc_te_ghep() {
        return lop_thuc_te_ghep;
    }

    public void setLop_thuc_te_ghep(String lop_thuc_te_ghep) {
        this.lop_thuc_te_ghep = lop_thuc_te_ghep;
    }
}
