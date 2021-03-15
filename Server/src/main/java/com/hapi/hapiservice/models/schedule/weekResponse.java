package com.hapi.hapiservice.models.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class weekResponse {
    public String ngaybd;

    public String ngaykt;

    public String sotuan;

    public String tenxacdinh;

    public long unixtimebd;

    public long unixtimekt;

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
}
