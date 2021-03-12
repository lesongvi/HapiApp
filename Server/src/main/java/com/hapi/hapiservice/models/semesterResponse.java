package com.hapi.hapiservice.models;

public class semesterResponse {
    public String semesterId;

    public String semesterDetail;

    public semesterResponse(
            String semesterId,
            String semesterDetail
    ) {
        this.semesterId = semesterId;
        this.semesterDetail = semesterDetail;
    }
}
