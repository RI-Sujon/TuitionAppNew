package com.example.tuitionapp_nadim.verified_tutor;

public class ReportInfo {

    private String guardianMobileNumber, tutorUid, message;

    public ReportInfo(){}

    public ReportInfo(String guardianMobileNumber, String tutorUid, String message) {
        this.guardianMobileNumber = guardianMobileNumber;
        this.tutorUid = tutorUid;
        this.message = message;
    }

    public String getGuardianMobileNumber() {
        return guardianMobileNumber;
    }

    public void setGuardianMobileNumber(String guardianMobileNumber) {
        this.guardianMobileNumber = guardianMobileNumber;
    }

    public String getTutorUid() {
        return tutorUid;
    }

    public void setTutorUid(String tutorUid) {
        this.tutorUid = tutorUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


