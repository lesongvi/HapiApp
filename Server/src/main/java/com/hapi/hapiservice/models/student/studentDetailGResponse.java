package com.hapi.hapiservice.models.student;

public class studentDetailGResponse {
    private studentDetailResponse result;

    public studentDetailGResponse(studentDetailResponse result) {
        this.result = result;
    }

    public studentDetailResponse getResult() {
        return result;
    }

    public void setResult(studentDetailResponse result) {
        this.result = result;
    }
}
