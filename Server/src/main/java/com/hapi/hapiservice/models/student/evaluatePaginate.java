package com.hapi.hapiservice.models.student;

import com.fasterxml.jackson.annotation.JsonProperty;

public class evaluatePaginate {
    private evaluatePaginateAllData number_of_all_data;

    private evaluatePaginateResultItem[] result;

    public evaluatePaginateAllData getNumberOfAllData() {
        return this.number_of_all_data;
    }

    public evaluatePaginate setNumberOfAllData(evaluatePaginateAllData number_of_all_data) {
        this.number_of_all_data = number_of_all_data;
        return this;
    }

    public evaluatePaginateResultItem[] getResult() {
        return this.result;
    }

    public evaluatePaginate setResult(evaluatePaginateResultItem[] result) {
        this.result = result;
        return this;
    }
}
