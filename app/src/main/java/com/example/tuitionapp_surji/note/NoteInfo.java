package com.example.tuitionapp_surji.note;


public class NoteInfo {
    private String note_title;
    private String note_post;
    private String notePdfUri;
    private String notePdfName;
    private String noteTime;
    private String noteDate;
    private String noteId;



    public NoteInfo() {
    }

    public NoteInfo(String note_title, String note_post,String noteTime, String noteDate) {
        this.note_title = note_title;
        this.note_post = note_post;
        this.noteTime = noteTime;
        this.noteDate = noteDate;
        this.noteId = noteId;


    }

    public NoteInfo(String note_title, String notePdfUri, String notePdfName) {
        this.note_title = note_title;
        this.notePdfUri = notePdfUri;
        this.notePdfName = notePdfName;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
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

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
