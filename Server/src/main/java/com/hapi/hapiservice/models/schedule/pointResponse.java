package com.hapi.hapiservice.models.schedule;

public class pointResponse {
    private String mamon;

    private String tenmon;

    private String tinchi;

    private String ptkt;

    private String ptthi;

    private String diemkt1;

    private String diemkt2;

    private String thil1;

    private String tkch;

    private String tk4;

    public pointResponse(
            String mamon,
            String tenmon,
            String tinchi,
            String ptkt,
            String ptthi,
            String diemkt1,
            String diemkt2,
            String thil1,
            String tkch,
            String tk4
    ) {
        this.mamon = mamon;
        this.tenmon = tenmon;
        this.tinchi = tinchi;
        this.ptkt = ptkt;
        this.ptthi = ptthi;
        this.diemkt1 = diemkt1;
        this.diemkt2 = diemkt2;
        this.thil1 = thil1;
        this.tkch = tkch;
        this.tk4 = tk4;
    }

    public void setMamon (String mamon) {
        this.mamon = mamon;
    }

    public String getMamon() {
        return this.mamon;
    }

    public void setTenmon (String tenmon) {
        this.tenmon = tenmon;
    }

    public String getTenmon() {
        return this.tenmon;
    }

    public void setTinchi (String tinchi) {
        this.tinchi = tinchi;
    }

    public String getTinchi() {
        return this.tinchi;
    }

    public void setPtkt (String ptkt) {
        this.ptkt = ptkt;
    }

    public String getPtkt() {
        return this.ptkt;
    }

    public void setPtthi (String ptthi) {
        this.ptthi = ptthi;
    }

    public String getPtthi() {
        return this.ptthi;
    }

    public void setDiemkt1 (String diemkt1) {
        this.diemkt1 = diemkt1;
    }

    public String getDiemkt1() {
        return this.diemkt1;
    }

    public void setDiemkt2 (String diemkt2) {
        this.diemkt2 = diemkt2;
    }

    public String getDiemkt2() {
        return this.diemkt2;
    }

    public void setThil1 (String thil1) {
        this.thil1 = thil1;
    }

    public String getThil1() {
        return this.thil1;
    }

    public void setTkch (String tkch) {
        this.tkch = tkch;
    }

    public String getTkch() {
        return this.tkch;
    }

    public void setTk4 (String tk4) {
        this.tk4 = tk4;
    }

    public String getTk4() {
        return this.tk4;
    }
}
