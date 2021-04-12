package com.hapi.hapiservice.models.student;

public class studentRestList {
    private String badge_trang_thai;

    private String[] buoi;

    private String date_ngay_nghi;

    private String day_of_week;

    private String giang_vien;

    private String ly_do;

    private String ly_do_khong_duyet;

    private String ngay_nghi;

    private String ngay_tao_text;

    private int npsv_phieu_yeu_cau_id;

    private String ten_mon;

    private String text_buoi;

    private String text_trang_thai;

    private String thoi_gian_phan_hoi_text;

    private int trang_thai;

    public studentRestList setBadgeTrangThai (String badge_trang_thai) {
        this.badge_trang_thai = badge_trang_thai;
        return this;
    }

    public String getBadgeTrangThai () {
        return this.badge_trang_thai;
    }

    public studentRestList setBuoi (String[] buoi) {
        this.buoi = buoi;
        return this;
    }

    public String[] getBuoi () {
        return this.buoi;
    }

    public studentRestList setDateNgayNghi (String date_ngay_nghi) {
        this.date_ngay_nghi = date_ngay_nghi;
        return this;
    }

    public String getDateNgayNghi () {
        return this.date_ngay_nghi;
    }

    public studentRestList setDayOfWeek (String day_of_week) {
        this.day_of_week = day_of_week;
        return this;
    }

    public String getDayOfWeek () {
        return this.day_of_week;
    }

    public studentRestList setGiangVien (String giang_vien) {
        this.giang_vien = giang_vien;
        return this;
    }

    public String getGiangVien () {
        return this.giang_vien;
    }

    public studentRestList setLyDo (String ly_do) {
        this.ly_do = ly_do;
        return this;
    }

    public String getLyDo () {
        return this.ly_do;
    }

    public studentRestList setLyDoKhongDuyet (String ly_do_khong_duyet) {
        this.ly_do_khong_duyet = ly_do_khong_duyet;
        return this;
    }

    public String getLyDoKhongDuyet () {
        return this.ly_do_khong_duyet;
    }

    public studentRestList setNgayNghi (String ngay_nghi) {
        this.ngay_nghi = ngay_nghi;
        return this;
    }

    public String getNgayNghi () {
        return this.ngay_nghi;
    }

    public studentRestList setNgayTaoText (String ngay_tao_text) {
        this.ngay_tao_text = ngay_tao_text;
        return this;
    }

    public String getNgayTaoText () {
        return this.ngay_tao_text;
    }

    public studentRestList setNPSVPhieuYeuCauId (int npsv_phieu_yeu_cau_id) {
        this.npsv_phieu_yeu_cau_id = npsv_phieu_yeu_cau_id;
        return this;
    }

    public int getNPSVPhieuYeuCauId () {
        return this.npsv_phieu_yeu_cau_id;
    }

    public studentRestList setTenMon (String ten_mon) {
        this.ten_mon = ten_mon;
        return this;
    }

    public String getTenMon () {
        return this.ten_mon;
    }

    public studentRestList setTextBuoi (String text_buoi) {
        this.text_buoi = text_buoi;
        return this;
    }

    public String getTextBuoi () {
        return this.text_buoi;
    }

    public studentRestList setTextTrangThai (String text_trang_thai) {
        this.text_trang_thai = text_trang_thai;
        return this;
    }

    public String getTextTrangThai () {
        return this.text_trang_thai;
    }

    public studentRestList setThoiGianPhanHoiText (String thoi_gian_phan_hoi_text) {
        this.thoi_gian_phan_hoi_text = thoi_gian_phan_hoi_text;
        return this;
    }

    public String getThoiGianPhanHoiText () {
        return this.thoi_gian_phan_hoi_text;
    }

    public studentRestList setTrangThai (int trang_thai) {
        this.trang_thai = trang_thai;
        return this;
    }

    public int getTrangThai () {
        return this.trang_thai;
    }
}
