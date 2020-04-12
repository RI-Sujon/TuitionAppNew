package com.example.tuitionapp.Group;

public class AddTutorInfo {
    private String groupID ;
    private String tutorEmail ;

    public AddTutorInfo(){

    }

    public AddTutorInfo(String groupID, String tutorEmail) {
        this.groupID = groupID;
        this.tutorEmail = tutorEmail;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }

}
