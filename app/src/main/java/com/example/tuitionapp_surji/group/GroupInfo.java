package com.example.tuitionapp_surji.group;

public class GroupInfo {
    private String groupName, address, fullAddress, classRange, extraInfo, groupImageUri, groupAdminEmail, groupAdminUid ;

    public GroupInfo(){}

    public GroupInfo(String groupName, String address, String fullAddress, String classRange, String extraInfo, String groupImageUri, String groupAdminEmail, String groupAdminUid) {
        this.groupName = groupName;
        this.address = address;
        this.fullAddress = fullAddress;
        this.classRange = classRange;
        this.extraInfo = extraInfo;
        this.groupImageUri = groupImageUri;
        this.groupAdminEmail = groupAdminEmail;
        this.groupAdminUid = groupAdminUid;
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

    public String getClassRange() {
        return classRange;
    }

    public void setClassRange(String classRange) {
        this.classRange = classRange;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getGroupImageUri() {
        return groupImageUri;
    }

    public void setGroupImageUri(String groupImageUri) {
        this.groupImageUri = groupImageUri;
    }

    public String getGroupAdminEmail() {
        return groupAdminEmail;
    }

    public void setGroupAdminEmail(String groupAdminEmail) {
        this.groupAdminEmail = groupAdminEmail;
    }

    public String getGroupAdminUid() {
        return groupAdminUid;
    }

    public void setGroupAdminUid(String groupAdminUid) {
        this.groupAdminUid = groupAdminUid;
    }
}
