package com.example.tuitionapp_surji.admin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ApproveAndBlockInfo {
    private String adminEmail ;
    private String status ;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss") ;
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy:MM:dd") ;
    private String time ;
    private String date ;

    public ApproveAndBlockInfo() {

    }

    public ApproveAndBlockInfo(String status) {
        this.status = status;
        time = simpleDateFormat.format(calendar.getTime());
        date = simpleDateFormat2.format(calendar.getTime());
    }

    public ApproveAndBlockInfo(String adminEmail, String status) {
        this.adminEmail = adminEmail;
        this.status = status;

        time = simpleDateFormat.format(calendar.getTime());
        date = simpleDateFormat2.format(calendar.getTime());
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}