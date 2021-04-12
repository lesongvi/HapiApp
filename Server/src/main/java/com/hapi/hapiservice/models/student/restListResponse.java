package com.hapi.hapiservice.models.student;

public class restListResponse {
    private evaluatePaginateAllData number_of_all_data;

    private studentRestList[] result;

    public restListResponse setNumberOfAllData(evaluatePaginateAllData number_of_all_data) {
        this.number_of_all_data = number_of_all_data;
        return this;
    }

    public evaluatePaginateAllData getNumberOfAllData() {
        return this.number_of_all_data;
    }

    public restListResponse setResult(studentRestList[] result) {
        this.result = result;
        return this;
    }

    public studentRestList[] getResult() {
        return this.result;
    }
}
