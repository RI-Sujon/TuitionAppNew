package com.example.tuitionapp_sujon.group;

public class GroupInfo {
    private String groupName, address, fullAddress, groupAdminUid ;

    public GroupInfo(){}

    public GroupInfo(String groupName, String address, String fullAddress, String groupAdminUid) {
        this.groupName = groupName;
        this.address = address;
        this.fullAddress = fullAddress;
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

    public String getGroupAdminUid() {
        return groupAdminUid;
    }

    public void setGroupAdminUid(String groupAdminUid) {
        this.groupAdminUid = groupAdminUid;
    }
}
