package com.example.tuitionapp_surji.verified_tutor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotificationInfo {
    private String types;
    private String message1, message2, message3, message4;

    private String postDate;
    private String postTime;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy");

    public NotificationInfo() {
    }

    public NotificationInfo(String types, String message1) {
        this.types = types;
        this.message1 = message1;

        postTime = simpleDateFormat.format(calendar.getTime());
        postDate = simpleDateFormat2.format(calendar.getTime());
    }

    public NotificationInfo(String types, String message1, String message2) {
        this.types = types;
        this.message1 = message1;
        this.message2 = message2;

        postTime = simpleDateFormat.format(calendar.getTime());
        postDate = simpleDateFormat2.format(calendar.getTime());
    }

    public NotificationInfo(String types, String message1, String message2, String message3) {
        this.types = types;
        this.message1 = message1;
        this.message2 = message2;
        this.message3 = message3;

        postTime = simpleDateFormat.format(calendar.getTime());
        postDate = simpleDateFormat2.format(calendar.getTime());
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public String getMessage3() {
        return message3;
    }

    public void setMessage3(String message3) {
        this.message3 = message3;
    }

    public String getMessage4() {
        return message4;
    }

    public void setMessage4(String message4) {
        this.message4 = message4;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}