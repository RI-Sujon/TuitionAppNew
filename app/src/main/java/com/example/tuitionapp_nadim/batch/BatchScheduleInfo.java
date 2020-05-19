package com.example.tuitionapp_nadim.batch;

public class BatchScheduleInfo {
    private String time, saturdaySubject, sundaySubject, mondaySubject, tuesdaySubject, wednesdaySubject, thursdaySubject, fridaySubject;

    public BatchScheduleInfo(){

    }

    public BatchScheduleInfo(String time, String saturdaySubject, String sundaySubject, String mondaySubject, String tuesdaySubject, String wednesdaySubject, String thursdaySubject, String fridaySubject) {
        this.time = time;
        this.saturdaySubject = saturdaySubject;
        this.sundaySubject = sundaySubject;
        this.mondaySubject = mondaySubject;
        this.tuesdaySubject = tuesdaySubject;
        this.wednesdaySubject = wednesdaySubject;
        this.thursdaySubject = thursdaySubject;
        this.fridaySubject = fridaySubject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSaturdaySubject() {
        return saturdaySubject;
    }

    public void setSaturdaySubject(String saturdaySubject) {
        this.saturdaySubject = saturdaySubject;
    }

    public String getSundaySubject() {
        return sundaySubject;
    }

    public void setSundaySubject(String sundaySubject) {
        this.sundaySubject = sundaySubject;
    }

    public String getMondaySubject() {
        return mondaySubject;
    }

    public void setMondaySubject(String mondaySubject) {
        this.mondaySubject = mondaySubject;
    }

    public String getTuesdaySubject() {
        return tuesdaySubject;
    }

    public void setTuesdaySubject(String tuesdaySubject) {
        this.tuesdaySubject = tuesdaySubject;
    }

    public String getWednesdaySubject() {
        return wednesdaySubject;
    }

    public void setWednesdaySubject(String wednesdaySubject) {
        this.wednesdaySubject = wednesdaySubject;
    }

    public String getThursdaySubject() {
        return thursdaySubject;
    }

    public void setThursdaySubject(String thursdaySubject) {
        this.thursdaySubject = thursdaySubject;
    }

    public String getFridaySubject() {
        return fridaySubject;
    }

    public void setFridaySubject(String fridaySubject) {
        this.fridaySubject = fridaySubject;
    }
}
