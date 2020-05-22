package com.example.tuitionapp.message_box;

public class MessageBoxInfo {
    private String guardianMobileNumber ;
    private String guardianUid ;

    private String tutorEmail ;
    private String tutorUid ;

    private boolean messageFromGuardianSide ;
    private boolean messageFromTutorSide ;

    public MessageBoxInfo(){

    }

    public MessageBoxInfo(String guardianMobileNumber, String guardianUid, String tutorEmail, String tutorUid, boolean messageFromGuardianSide, boolean messageFromTutorSide) {
        this.guardianMobileNumber = guardianMobileNumber;
        this.guardianUid = guardianUid;
        this.tutorEmail = tutorEmail;
        this.tutorUid = tutorUid;
        this.messageFromGuardianSide = messageFromGuardianSide;
        this.messageFromTutorSide = messageFromTutorSide;
    }

    public String getGuardianMobileNumber() {
        return guardianMobileNumber;
    }

    public void setGuardianMobileNumber(String guardianMobileNumber) {
        this.guardianMobileNumber = guardianMobileNumber;
    }

    public String getGuardianUid() {
        return guardianUid;
    }

    public void setGuardianUid(String guardianUid) {
        this.guardianUid = guardianUid;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }

    public String getTutorUid() {
        return tutorUid;
    }

    public void setTutorUid(String tutorUid) {
        this.tutorUid = tutorUid;
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
}
