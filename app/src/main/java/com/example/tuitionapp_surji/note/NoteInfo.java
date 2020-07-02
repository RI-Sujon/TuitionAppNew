package com.example.tuitionapp_surji.note;


public class NoteInfo {
    private String note_post;
    private String noteTime;
    private String noteDate;
    private String userId;



    public NoteInfo() {
    }

    public NoteInfo(String note_title, String note_post, String noteTime, String noteDate, String userId) {
        this.note_post = note_post;
        this.noteTime = noteTime;
        this.noteDate = noteDate;
        this.userId = userId;
    }



    public String getNote_post() {
        return note_post;
    }

    public void setNote_post(String note_post) {
        this.note_post = note_post;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
