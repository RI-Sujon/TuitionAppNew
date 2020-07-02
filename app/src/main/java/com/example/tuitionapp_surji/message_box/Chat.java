package com.example.tuitionapp_surji.message_box;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String message_time;
    private String message_type;
    private boolean isSeen;


    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String message_time, String message_type,boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.message_time = message_time;
        this.message_type = message_type;
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

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }
}
