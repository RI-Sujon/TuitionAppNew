package com.example.tuitionappv1.message_box;

public class MessageBoxInfo {
    private String guardianMobileNumber ;
    private String tutorEmail ;
    private boolean messageFromGuardianSide ;
    private boolean messageFromTutorSide ;
    private String message ;

    public MessageBoxInfo(){

    }

    public MessageBoxInfo(String guardianMobileNumber, String tutorEmail, boolean messageFromGuardianSide, boolean messageFromTutorSide) {
        this.guardianMobileNumber = guardianMobileNumber;
        this.tutorEmail = tutorEmail;
        this.messageFromGuardianSide = messageFromGuardianSide;
        this.messageFromTutorSide = messageFromTutorSide;
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

    public boolean isMessageFromGuardianSide() {
        return messageFromGuardianSide;
    }

    public void setMessageFromGuardianSide(boolean messageFromGuardianSide) {
        this.messageFromGuardianSide = messageFromGuardianSide;
    }

    public boolean isMessageFromTutorSide() {
        return messageFromTutorSide;
    }

    public void setMessageFromTutorSide(boolean messageFromTutorSide) {
        this.messageFromTutorSide = messageFromTutorSide;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
