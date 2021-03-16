package com.hapi.hapiservice.models.schedule;

public class PSListResponse {
    //private String tenxacdinh;

    private String hocky;

    private String namhoc;

    public PSListResponse(
            //String tenxacdinh,
            String hocky,
            String namhoc
    ) {
        //this.tenxacdinh = tenxacdinh;
        this.hocky = hocky;
        this.namhoc = namhoc;
    }

    /*public void setTenxacdinh(String tenxacdinh) {
        this.tenxacdinh = tenxacdinh;
    }

    public String getTenxacdinh() {
        return this.tenxacdinh;
    }*/

    public void setHocky(String hocky) {
        this.hocky = hocky;
    }

    public String getHocky() {
        return this.hocky;
    }

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
    }

    public String getNamhoc() {
        return this.namhoc;
    }


}
