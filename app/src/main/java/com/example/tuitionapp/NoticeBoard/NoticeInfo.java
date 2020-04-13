package com.example.tuitionapp.NoticeBoard;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;

public class NoticeInfo {
    private String groupID;
    private String post;
    private String pdfUri;
    private String pdfName;
    private String noticeDate;
    private String noticeTime;

    public NoticeInfo() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NoticeInfo(String groupID, String post, String pdfUri,String pdfName) {
        this.groupID = groupID;
        this.post = post;
        this.pdfUri = pdfUri;
        this.pdfName = pdfName ;

        noticeDate = LocalDate.now().toString() ;
        noticeTime = LocalTime.now().toString() ;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
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