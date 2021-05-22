package com.hapi.hapiservice.models.student;

public class routeNationResponse {
    private routeNationDt[] dan_tocs;
    private routeNationPx[] phuong_xas;
    private routeNationQh[] quan_huyens;
    private routeNationTtp[] tinh_tps;
    private routeNationTg[] ton_giaos;

    public routeNationResponse(routeNationDt[] dan_tocs, routeNationPx[] phuong_xas, routeNationQh[] quan_huyens, routeNationTtp[] tinh_tps, routeNationTg[] ton_giaos) {
        this.dan_tocs = dan_tocs;
        this.phuong_xas = phuong_xas;
        this.quan_huyens = quan_huyens;
        this.tinh_tps = tinh_tps;
        this.ton_giaos = ton_giaos;
    }

    public routeNationDt[] getDan_tocs() {
        return dan_tocs;
    }

    public void setDan_tocs(routeNationDt[] dan_tocs) {
        this.dan_tocs = dan_tocs;
    }

    public routeNationPx[] getPhuong_xas() {
        return phuong_xas;
    }

    public void setPhuong_xas(routeNationPx[] phuong_xas) {
        this.phuong_xas = phuong_xas;
    }

    public routeNationQh[] getQuan_huyens() {
        return quan_huyens;
    }

    public void setQuan_huyens(routeNationQh[] quan_huyens) {
        this.quan_huyens = quan_huyens;
    }

    public routeNationTtp[] getTinh_tps() {
        return tinh_tps;
    }

    public void setTinh_tps(routeNationTtp[] tinh_tps) {
        this.tinh_tps = tinh_tps;
    }

    public routeNationTg[] getTon_giaos() {
        return ton_giaos;
    }

    public void setTon_giaos(routeNationTg[] ton_giaos) {
        this.ton_giaos = ton_giaos;
    }
}

