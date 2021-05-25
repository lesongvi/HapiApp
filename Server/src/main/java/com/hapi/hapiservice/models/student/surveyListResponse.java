package com.hapi.hapiservice.models.student;

import java.util.List;

public class surveyListResponse{
    public String dot_khao_sat_id;
    public String ten_dot;
    public List<surveyListDetail> list_phieu;

    public surveyListResponse(String dot_khao_sat_id, String ten_dot, List<surveyListDetail> list_phieu) {
        this.dot_khao_sat_id = dot_khao_sat_id;
        this.ten_dot = ten_dot;
        this.list_phieu = list_phieu;
    }

    public String getDot_khao_sat_id() {
        return dot_khao_sat_id;
    }

    public void setDot_khao_sat_id(String dot_khao_sat_id) {
        this.dot_khao_sat_id = dot_khao_sat_id;
    }

    public String getTen_dot() {
        return ten_dot;
    }

    public void setTen_dot(String ten_dot) {
        this.ten_dot = ten_dot;
    }

    public List<surveyListDetail> getList_phieu() {
        return list_phieu;
    }

    public void setList_phieu(List<surveyListDetail> list_phieu) {
        this.list_phieu = list_phieu;
    }
}
