package com.hapi.hapiservice.models.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class evaluateTicketResultItem {
    private boolean isDuocDanhGia;

    private boolean isMine;

    private String ma_so_sinh_vien;

    private evaluateTicketTemplate mau_phieu;

    private String ngay_bd_sinhvien;

    private String ngay_kt_sinhvien;

    public evaluateTicketResultItem setIsDuocDanhGia(boolean isDuocDanhGia) {
        this.isDuocDanhGia = isDuocDanhGia;
        return this;
    }

    @JsonProperty("is_duoc_danh_gia")
    public boolean getIsDuocDanhGia() {
        return this.isDuocDanhGia;
    }

    public evaluateTicketResultItem setIsMine(boolean isMine) {
        this.isMine = isMine;
        return this;
    }

    public boolean getIsMine() {
        return this.isMine;
    }

    public evaluateTicketResultItem setMaSoSinhVien(String ma_so_sinh_vien) {
        this.ma_so_sinh_vien = ma_so_sinh_vien;
        return this;
    }

    public String getMaSoSinhVien() {
        return this.ma_so_sinh_vien;
    }

    public evaluateTicketResultItem setMauPhieu(evaluateTicketTemplate mau_phieu) {
        this.mau_phieu = mau_phieu;
        return this;
    }

    public evaluateTicketTemplate getMauPhieu() {
        return this.mau_phieu;
    }

    public evaluateTicketResultItem setNgayBdSinhVien(String ngay_bd_sinhvien) {
        this.ngay_bd_sinhvien = ngay_bd_sinhvien;
        return this;
    }

    public String getNgayBdSinhVien() {
        return this.ngay_bd_sinhvien;
    }

    public evaluateTicketResultItem setNgayKtSinhVien(String ngay_kt_sinhvien) {
        this.ngay_kt_sinhvien = ngay_kt_sinhvien;
        return this;
    }

    public String getNgayKtSinhVien() {
        return this.ngay_kt_sinhvien;
    }
}
