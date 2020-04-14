package com.example.tuitionappv1.admin;

public class BlockInfo {
    private String adminEmail ;
    private String verifiedTutorEmail ;
    private boolean block ;

    public  BlockInfo(){

    }

    public BlockInfo(String adminEmail, String verifiedTutorEmail, boolean block) {
        this.adminEmail = adminEmail;
        this.verifiedTutorEmail = verifiedTutorEmail;
        this.block = block;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getVerifiedTutorEmail() {
        return verifiedTutorEmail;
    }

    public void setVerifiedTutorEmail(String verifiedTutorEmail) {
        this.verifiedTutorEmail = verifiedTutorEmail;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}
