package com.example.tuitionapp_surji.candidate_tutor;

public class ReferInfo {
    private String verifiedTutorEmail ;
    private String referApprove ;

    public ReferInfo(){}

    public ReferInfo(String verifiedTutorEmail) {
        this.verifiedTutorEmail = verifiedTutorEmail;
        referApprove = "waiting" ;
    }

    public String getVerifiedTutorEmail() {
        return verifiedTutorEmail;
    }

    public void setVerifiedTutorEmail(String verifiedTutorEmail) {
        this.verifiedTutorEmail = verifiedTutorEmail;
    }

    public String getReferApprove() {
        return referApprove;
    }

    public void setReferApprove(String referApprove) {
        this.referApprove = referApprove;
    }
}
