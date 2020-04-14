package com.example.tuitionapp.verified_tutor;

public class ReportInfo {

    private String guardianMobileNumber, tutorEmail , message;

    public ReportInfo(){}

    public ReportInfo(String guardianMobileNumber, String tutorEmail, String message) {
        this.guardianMobileNumber = guardianMobileNumber;
        this.tutorEmail = tutorEmail;
        this.message = message;
    }

    public String getGuardianMobileNumber() {
        return guardianMobileNumber;
    }

    public void setGuardianMobileNumber(String guardianMobileNumber) {
        this.guardianMobileNumber = guardianMobileNumber;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


