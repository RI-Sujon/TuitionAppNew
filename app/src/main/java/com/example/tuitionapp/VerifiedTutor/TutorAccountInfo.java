package com.example.tuitionapp.VerifiedTutor;

public class TutorAccountInfo {

    private String firstName,lastName,email,mobileNumber, gender,areaAddress,currentPosition , edu_instituteName, edu_tutorSubject  ;

    public TutorAccountInfo(){

    }

    public TutorAccountInfo(String firstName, String lastName, String email, String mobileNumber, String gender, String areaAddress, String currentPosition, String edu_instituteName, String edu_tutorSubject) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.areaAddress = areaAddress;
        this.currentPosition = currentPosition;
        this.edu_instituteName = edu_instituteName;
        this.edu_tutorSubject = edu_tutorSubject;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAreaAddress() {
        return areaAddress;
    }

    public void setAreaAddress(String areaAddress) {
        this.areaAddress = areaAddress;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getEdu_instituteName() {
        return edu_instituteName;
    }

    public void setEdu_instituteName(String edu_instituteName) {
        this.edu_instituteName = edu_instituteName;
    }

    public String getEdu_tutorSubject() {
        return edu_tutorSubject;
    }

    public void setEdu_tutorSubject(String edu_tutorSubject) {
        this.edu_tutorSubject = edu_tutorSubject;
    }

    @Override
    public String toString() {
        return
                " Name = " + firstName +
                " " + lastName + '\'' +
                ", Email = '" + email + '\'' +
                ", Mobile Number = '" + mobileNumber + '\'' +
                ", Gender = '" + gender + '\'' +
                ", AreaAddress = '" + areaAddress + '\'' +
                ", Current Position = '" + currentPosition + '\'' +
                ", Tutor's Institute Name = '" + edu_instituteName + '\'' +
                ", Tutor's Subject/Department = '" + edu_tutorSubject + '\'' ;
    }
}
