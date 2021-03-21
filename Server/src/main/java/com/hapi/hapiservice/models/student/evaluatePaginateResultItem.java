package com.hapi.hapiservice.models.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class evaluatePaginateResultItem {
    private String diem_ca_nhan;

    private String diem_khoa;

    private String diem_lop;

    private String dot_khao_sat_id;

    private boolean hienThiDiemTong;

    private String hoc_ky;

    private String nam_hoc;

    private String ngay_bd_sinhvien;

    private String ngay_kt_sinhvien;

    private String phieu_danh_gia_id;

    private int trang_thai_duyet;

    private String xep_loai;

    public evaluatePaginateResultItem setDiemCaNhan(String diem_ca_nhan) {
        this.diem_ca_nhan = diem_ca_nhan;
        return this;
    }

    public String getDiemCaNhan() {
        return diem_ca_nhan;
    }

    public evaluatePaginateResultItem setDiemKhoa(String diem_khoa) {
        this.diem_khoa = diem_khoa;
        return this;
    }

    public String getDiemKhoa() {
        return diem_khoa;
    }

    public evaluatePaginateResultItem setDiemLop(String diem_lop) {
        this.diem_lop = diem_lop;
        return this;
    }

    public String getDiemLop() {
        return diem_lop;
    }

    public evaluatePaginateResultItem setDotKhaoSatId(String dot_khao_sat_id) {
        this.dot_khao_sat_id = dot_khao_sat_id;
        return this;
    }

    public String getDotKhaoSatId() {
        return dot_khao_sat_id;
    }

    public evaluatePaginateResultItem setHienThiDiemTong(boolean hienThiDiemTong) {
        this.hienThiDiemTong = hienThiDiemTong;
        return this;
    }

    @JsonProperty("hien_thi_diem_tong")
    public boolean getHienThiDiemTong() {
        return this.hienThiDiemTong;
    }

    public evaluatePaginateResultItem setHocKy(String hoc_ky) {
        this.hoc_ky = hoc_ky;
        return this;
    }

    public String getHocKy() {
        return hoc_ky;
    }

    public evaluatePaginateResultItem setNamHoc(String nam_hoc) {
        this.nam_hoc = nam_hoc;
        return this;
    }

    public String getNamHoc() {
        return nam_hoc;
    }

    public evaluatePaginateResultItem setNgayBdSinhVien(String ngay_bd_sinhvien) {
        this.ngay_bd_sinhvien = ngay_bd_sinhvien;
        return this;
    }

    public String getNgayBdSinhVien() {
        return ngay_bd_sinhvien;
    }

    public evaluatePaginateResultItem setNgayKtSinhVien(String ngay_kt_sinhvien) {
        this.ngay_kt_sinhvien = ngay_kt_sinhvien;
        return this;
    }

    public String getNgayKtSinhVien() {
        return ngay_kt_sinhvien;
    }

    public evaluatePaginateResultItem setPhieuDanhGiaId(String phieu_danh_gia_id) {
        this.phieu_danh_gia_id = phieu_danh_gia_id;
        return this;
    }

    public String getPhieuDanhGiaId() {
        return phieu_danh_gia_id;
    }

    public evaluatePaginateResultItem setTrangThaiDuyet(int trang_thai_duyet) {
        this.trang_thai_duyet = trang_thai_duyet;
        return this;
    }

    public int getTrangThaiDuyet() {
        return trang_thai_duyet;
    }

    public evaluatePaginateResultItem setXepLoai(String xep_loai) {
        this.xep_loai = xep_loai;
        return this;
    }

    public String getXepLoai() {
        return xep_loai;
    }
}
