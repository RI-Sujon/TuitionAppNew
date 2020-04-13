package com.example.tuitionapp.Group;

public class GroupInfo {
    private String groupName, address, fullAddress, groupAdminEmail ;

    public GroupInfo(){}

    public GroupInfo(String groupName, String address, String fullAddress, String groupAdminEmail) {
        this.groupName = groupName;
        this.address = address;
        this.fullAddress = fullAddress;
        this.groupAdminEmail = groupAdminEmail;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getGroupAdminEmail() {
        return groupAdminEmail;
    }

    public void setGroupAdminEmail(String groupAdminEmail) {
        this.groupAdminEmail = groupAdminEmail;
    }
}
