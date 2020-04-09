package com.example.tuitionapp.Admin;

public class ApproveInfo {
    private String adminEmail ;
    private String candidateTutorEmail ;
    private boolean approve ;

    public ApproveInfo(){

    }

    public ApproveInfo(String adminEmail, String candidateTutorEmail) {
        this.adminEmail = adminEmail;
        this.candidateTutorEmail = candidateTutorEmail;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getCandidateTutorEmail() {
        return candidateTutorEmail;
    }

    public void setCandidateTutorEmail(String candidateTutorEmail) {
        this.candidateTutorEmail = candidateTutorEmail;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }
}
