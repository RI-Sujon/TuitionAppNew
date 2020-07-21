package com.example.tuitionapp_surji.notification_pack;

public class NotificationSender {
    private Data data ;
    private String token ;

    public NotificationSender() {
    }

    public NotificationSender(Data data, String token) {
        this.data = data;
        this.token = token;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
