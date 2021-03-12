package com.hapi.hapiservice.models;

public class semesterRequest {
    public String __EVENTTARGET;

    public String __EVENTARGUMENT;

    public String __VIEWSTATE;

    public String ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK;

    public int ctl00$ContentPlaceHolder1$ctl00$ddlLoai = 0;

    public String ctl00$ContentPlaceHolder1$ctl00$rad_MonHoc = "rad_MonHoc";

    public semesterRequest(
            String __EVENTTARGET,
            String __EVENTARGUMENT,
            String __VIEWSTATE,
            String ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK
    ) {
        this.__EVENTTARGET = __EVENTTARGET;
        this.__EVENTARGUMENT = __EVENTARGUMENT;
        this.__VIEWSTATE = __VIEWSTATE;
        this.ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK = ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK;
    }
}
