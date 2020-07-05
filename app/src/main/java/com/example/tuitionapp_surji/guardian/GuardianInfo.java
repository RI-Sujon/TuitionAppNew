package com.example.tuitionapp_surji.guardian;

public class GuardianInfo {
    private String name, address, phoneNumber, status, profilePicUri ;

    public GuardianInfo() {

    }

    public GuardianInfo(String name, String address, String phoneNumber, String profilePicUri) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profilePicUri = profilePicUri ;
        status = "offline" ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePicUri() {
        return profilePicUri;
    }

    public void setProfilePicUri(String profilePicUri) {
        this.profilePicUri = profilePicUri;
    }
}
