package com.example.tuitionapp_surji.group;

public class AddTutorInfo {
    private String tutorID ;
    private String tutorEmail ;

    public AddTutorInfo(){

    }

    public AddTutorInfo(String tutorID, String tutorEmail) {
        this.tutorID = tutorID;
        this.tutorEmail = tutorEmail;
    }

    public String getGroupID() {
        return tutorID;
    }

    public void setGroupID(String tutorID) {
        this.tutorID = tutorID;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }

}
