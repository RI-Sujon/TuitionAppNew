package com.example.tuitionapp.notice_board;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoticeInfo {
    private String groupID;
    private String title;
    private String post;
    private String pdfUri;
    private String pdfName;
    private String noticeDate;
    private String noticeTime;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a") ;
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy") ;

    public NoticeInfo() {
    }

    public NoticeInfo(String groupID, String title, String post) {
        this.groupID = groupID;
        this.title = title ;
        this.post = post;

        noticeTime = simpleDateFormat.format(calendar.getTime());
        noticeDate = simpleDateFormat2.format(calendar.getTime());
    }

    public NoticeInfo(String groupID, String title, String pdfUri,String pdfName) {
        this.groupID = groupID;
        this.title = title ;
        this.pdfUri = pdfUri;
        this.pdfName = pdfName ;

        noticeTime = simpleDateFormat.format(calendar.getTime());
        noticeDate = simpleDateFormat2.format(calendar.getTime());
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(String pdfUri) {
        this.pdfUri = pdfUri;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }
}