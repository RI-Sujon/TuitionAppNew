package com.example.tuitionapp_nadim.candidate_tutor;

import java.util.ArrayList;

public class CandidateTutorInfo {

    private String userName, emailPK, mobileNumber, gender, areaAddress, currentPosition, edu_instituteName, attachedHall, edu_tutorSubject, idCardImageUri  ;
    private ArrayList<String> verifiedTutorReferences ;
    private String profilePictureUri ;

    public CandidateTutorInfo(){

    }

    public CandidateTutorInfo(String userName, String emailPK, String mobileNumber) {
        this.userName = userName;
        this.emailPK = emailPK;
        this.mobileNumber = mobileNumber;
    }

    public CandidateTutorInfo(String gender, String areaAddress, String currentPosition, String edu_instituteName, String edu_tutorSubject, String attachedHall) {
        this.gender = gender;
        this.areaAddress = areaAddress;
        this.currentPosition = currentPosition;
        this.edu_instituteName = edu_instituteName;
        this.edu_tutorSubject = edu_tutorSubject;
        this.attachedHall =attachedHall ;
    }

    public CandidateTutorInfo(String userName, String emailPK, String mobileNumber, String gender, String areaAddress, String currentPosition, String edu_instituteName, String edu_tutorSubject, String attachedHall) {
        this.userName = userName;
        this.emailPK = emailPK;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.areaAddress = areaAddress;
        this.currentPosition = currentPosition;
        this.edu_instituteName = edu_instituteName;
        this.edu_tutorSubject = edu_tutorSubject;
        this.attachedHall = attachedHall;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getAttachedHall() {
        return attachedHall;
    }

    public void setAttachedHall(String attachedHall) {
        this.attachedHall = attachedHall;
    }

    public String getIdCardImageUri() {
        return idCardImageUri;
    }

    public void setIdCardImageUri(String idCardImageUri) {
        this.idCardImageUri = idCardImageUri;
    }

    public ArrayList<String> getVerifiedTutorReferences() {
        return verifiedTutorReferences;
    }

    public void setVerifiedTutorReferences(ArrayList<String> verifiedTutorReferences) {
        this.verifiedTutorReferences = verifiedTutorReferences;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }
}
