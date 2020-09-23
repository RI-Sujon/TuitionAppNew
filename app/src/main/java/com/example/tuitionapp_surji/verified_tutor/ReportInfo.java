package com.example.tuitionapp_surji.verified_tutor;

public class ReportInfo {

    private String guardianMobileNumber, message;

    public ReportInfo(){}

    public ReportInfo(String guardianMobileNumber, String message) {
        this.guardianMobileNumber = guardianMobileNumber;
        this.message = message;
    }

    public String getGuardianMobileNumber() {
        return guardianMobileNumber;
    }

    public void setGuardianMobileNumber(String guardianMobileNumber) {
        this.guardianMobileNumber = guardianMobileNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


