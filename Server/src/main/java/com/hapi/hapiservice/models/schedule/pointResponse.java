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
}
