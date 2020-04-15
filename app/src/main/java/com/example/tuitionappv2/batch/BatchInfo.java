package com.example.tuitionappv2.batch;

import java.util.ArrayList;

public class BatchInfo {
    private String batchName, numberOfAvailableSeat, payment ;
    private ArrayList<BatchScheduleInfo> batchScheduleInfoList ;
    private String groupIDFK ;

    public BatchInfo(){}

    public BatchInfo(String batchName, String numberOfAvailableSeat, String payment, ArrayList<BatchScheduleInfo> batchScheduleInfoList, String groupIDFK) {
        this.batchName = batchName;
        this.numberOfAvailableSeat = numberOfAvailableSeat;
        this.payment = payment;
        this.batchScheduleInfoList = batchScheduleInfoList;
        this.groupIDFK = groupIDFK ;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getNumberOfAvailableSeat() {
        return numberOfAvailableSeat;
    }

    public void setNumberOfAvailableSeat(String numberOfAvailableSeat) {
        this.numberOfAvailableSeat = numberOfAvailableSeat;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public ArrayList<BatchScheduleInfo> getBatchScheduleInfoList() {
        return batchScheduleInfoList;
    }

    public void setBatchScheduleInfoList(ArrayList<BatchScheduleInfo> batchScheduleInfoList) {
        this.batchScheduleInfoList = batchScheduleInfoList;
    }

    public String getGroupIDFK() {
        return groupIDFK;
    }

    public void setGroupIDFK(String groupIDFK) {
        this.groupIDFK = groupIDFK;
    }
}
