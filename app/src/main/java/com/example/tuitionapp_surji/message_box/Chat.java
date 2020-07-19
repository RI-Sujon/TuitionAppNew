package com.example.tuitionapp_surji.message_box;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String message_time;
    private String messageDate;
    private String message_type;
    private String messageDay;
    private String  isSeen;


    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String message_time, String messageDate, String message_type, String messageDay, String isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.message_time = message_time;
        this.messageDate = messageDate;
        this.message_type = message_type;
        this.messageDay = messageDay;
        this.isSeen = isSeen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessageDay() {
        return messageDay;
    }

    public void setMessageDay(String messageDay) {
        this.messageDay = messageDay;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }
}
