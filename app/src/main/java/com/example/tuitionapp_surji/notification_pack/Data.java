package com.example.tuitionapp_surji.notification_pack;

public class Data {
    public String Title, Message ;

    public Data() {
    }

    public Data(String title, String message) {
        Title = title;
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
