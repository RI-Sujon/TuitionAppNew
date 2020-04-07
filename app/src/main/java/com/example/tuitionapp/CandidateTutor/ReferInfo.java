package com.example.tuitionapp.CandidateTutor;

public class ReferInfo {
    private String candidateTutorEmail, verifiedTutorEmail ;
    private boolean referApprove ;

    public ReferInfo(String candidateTutorEmail, String verifiedTutorEmail, boolean referApprove) {
        this.candidateTutorEmail = candidateTutorEmail;
        this.verifiedTutorEmail = verifiedTutorEmail;
        this.referApprove = referApprove;
    }

    public String getCandidateTutorEmail() {
        return candidateTutorEmail;
    }

    public void setCandidateTutorEmail(String candidateTutorEmail) {
        this.candidateTutorEmail = candidateTutorEmail;
    }

    public String getVerifiedTutorEmail() {
        return verifiedTutorEmail;
    }

    public void setVerifiedTutorEmail(String verifiedTutorEmail) {
        this.verifiedTutorEmail = verifiedTutorEmail;
    }

    public boolean isReferApprove() {
        return referApprove;
    }

    public void setReferApprove(boolean referApprove) {
        this.referApprove = referApprove;
    }
}
