package com.hapi.hapiservice.models.student;

public class evaluateTicket {
    private evaluatePaginateAllData number_of_all_data;

    private evaluateTicketResultItem[] result;

    public evaluateTicket setNumberOfAllData(evaluatePaginateAllData number_of_all_data) {
        this.number_of_all_data = number_of_all_data;
        return this;
    }

    public evaluatePaginateAllData getNumberOfAllData() {
        return this.number_of_all_data;
    }

    public evaluateTicket setResult(evaluateTicketResultItem[] result) {
        this.result = result;
        return this;
    }

    public evaluateTicketResultItem[] getResult() {
        return this.result;
    }
}
