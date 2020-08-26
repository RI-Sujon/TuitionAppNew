package com.example.tuitionapp_surji.batch;

public class StudentInfo {
    private String serialNo, column1, column2, column3 ;

    public StudentInfo(){

    }

    public StudentInfo(String serialNo, String column1, String column2, String column3) {
        this.serialNo = serialNo;
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }
}
