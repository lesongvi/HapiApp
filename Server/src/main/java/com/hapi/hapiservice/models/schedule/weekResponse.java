package com.hapi.hapiservice.models.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class weekResponse {
    private String ngaybd;

    private String ngaykt;

    private String sotuan;

    private String tenxacdinh;

    private long unixtimebd;

    private long unixtimekt;

    public weekResponse(
            String ngaybd,
            String ngaykt,
            String sotuan,
            String tenxacdinh
    ) throws ParseException {
        this.ngaybd = ngaybd;
        this.ngaykt = ngaykt;
        this.sotuan = sotuan;
        this.tenxacdinh = tenxacdinh;
        this.unixtimebd = this.getUnixTimeFromDate(ngaybd);
        this.unixtimekt = this.getUnixTimeFromDate(ngaykt);
    }

    public long getUnixTimeFromDate (String dString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse(dString);
        return date.getTime()/1000;
    }

    public void setNgaybd(String ngaybd) {
        this.ngaybd = ngaybd;
    }

    public String getNgaybd() {
        return this.ngaybd;
    }

    public void setNgaykt(String ngaykt) {
        this.ngaykt = ngaykt;
    }

    public String getNgaykt() {
        return this.ngaykt;
    }

    public void setSotuan(String sotuan) {
        this.sotuan = sotuan;
    }

    public String getSotuan() {
        return this.sotuan;
    }

    public void setTenxacdinh(String tenxacdinh) {
        this.tenxacdinh = tenxacdinh;
    }

    public String getTenxacdinh() {
        return this.tenxacdinh;
    }

    public void setUnixtimebd(long unixtimebd) {
        this.unixtimebd = unixtimebd;
    }

    public long getUnixtimebd() {
        return this.unixtimebd;
    }

    public void setUnixtimekt(long unixtimekt) {
        this.unixtimekt = unixtimekt;
    }

    public long getUnixtimekt() {
        return this.unixtimekt;
    }
}
