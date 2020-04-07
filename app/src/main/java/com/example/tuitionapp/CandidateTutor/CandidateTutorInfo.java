package com.example.tuitionapp.CandidateTutor;

import java.util.ArrayList;

public class CandidateTutorInfo {

    private String firstName,lastName,emailPK,mobileNumber, gender,areaAddress,currentPosition , edu_instituteName, edu_tutorSubject, idCardImageName  ;
    private ArrayList<String> verifiedTutorReferences ;

    public CandidateTutorInfo(){

    }

    public CandidateTutorInfo(String firstName, String lastName, String emailPK, String mobileNumber, String gender, String areaAddress, String currentPosition, String edu_instituteName, String edu_tutorSubject) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailPK = emailPK;
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

    public String getEmailPK() {
        return emailPK;
    }

    public void setEmailPK(String emailPK) {
        this.emailPK = emailPK;
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

    public String getIdCardImageName() {
        return idCardImageName;
    }

    public void setIdCardImageName(String idCardImageName) {
        this.idCardImageName = idCardImageName;
    }

    public ArrayList<String> getVerifiedTutorReferences() {
        return verifiedTutorReferences;
    }

    public void setVerifiedTutorReferences(ArrayList<String> verifiedTutorReferences) {
        this.verifiedTutorReferences = verifiedTutorReferences;
    }

    @Override
    public String toString() {
        return
                " Name = " + firstName +
                " " + lastName + '\'' +
                ", Email = '" + emailPK + '\'' +
                ", Mobile Number = '" + mobileNumber + '\'' +
                ", Gender = '" + gender + '\'' +
                ", AreaAddress = '" + areaAddress + '\'' +
                ", Current Position = '" + currentPosition + '\'' +
                ", Tutor's Institute Name = '" + edu_instituteName + '\'' +
                ", Tutor's Subject/Department = '" + edu_tutorSubject + '\'' ;
    }
}
