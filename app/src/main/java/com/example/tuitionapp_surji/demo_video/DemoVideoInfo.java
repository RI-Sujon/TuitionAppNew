package com.example.tuitionapp_surji.demo_video;

public class DemoVideoInfo {
    private String videoName;
    private String videoUri;
    private  String emailPrimaryKey;


    public DemoVideoInfo() {
    }

    public DemoVideoInfo(String videoName, String videoUri, String emailPrimaryKey){
        if (videoName.equals("")){
            this.videoName = " not available";
        }
       this.videoName = videoName;
        this.videoUri = videoUri;
        this.emailPrimaryKey = emailPrimaryKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getEmailPrimaryKey() {
        return emailPrimaryKey;
    }

    public void setEmailPrimaryKey(String emailPrimaryKey) {
        this.emailPrimaryKey = emailPrimaryKey;
    }
}


