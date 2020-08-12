package com.example.tuitionapp_surji.tuition_post;

public class ResponsePost {
    private String postUid ;

    public ResponsePost(){

    }

    public ResponsePost(String postUid) {
        this.postUid = postUid;
    }


    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }
}
