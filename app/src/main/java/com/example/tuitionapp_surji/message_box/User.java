package com.example.tuitionapp_surji.message_box;

public class User {
    private String guardianMobileNumber ;
    private String guardianUid ;
    private String tutorUid ;
    private String tutorEmail ;
    private boolean messageFromGuardianSide ;
    private boolean messageFromTutorSide ;

    public User() {
    }


    public User(String guardianMobileNumber, String guardianUid, String tutorUid, String tutorEmail, boolean messageFromGuardianSide, boolean messageFromTutorSide) {
        this.guardianMobileNumber = guardianMobileNumber;
        this.guardianUid = guardianUid;
        this.tutorUid = tutorUid;
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

    public String getGuardianUid() {
        return guardianUid;
    }

    public void setGuardianUid(String guardianUid) {
        this.guardianUid = guardianUid;
    }

    public String getTutorUid() {
        return tutorUid;
    }

    public void setTutorUid(String tutorUid) {
        this.tutorUid = tutorUid;
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
}