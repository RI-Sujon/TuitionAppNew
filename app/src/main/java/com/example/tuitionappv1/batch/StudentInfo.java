package com.example.tuitionappv1.batch;

public class StudentInfo {
    private String serialNo, studentName, studentAddress, studentMobileNumber ;

    public StudentInfo(){

    }

    public StudentInfo(String serialNo, String studentName, String studentAddress, String studentMobileNumber) {
        this.serialNo = serialNo;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.studentMobileNumber = studentMobileNumber;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getStudentMobileNumber() {
        return studentMobileNumber;
    }

    public void setStudentMobileNumber(String studentMobileNumber) {
        this.studentMobileNumber = studentMobileNumber;
    }
}
