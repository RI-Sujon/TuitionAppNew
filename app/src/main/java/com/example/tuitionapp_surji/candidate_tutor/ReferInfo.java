package com.example.tuitionapp_surji.candidate_tutor;

public class ReferInfo {
    private String verifiedTutorEmail, verifiedTutorUid ;
    private String referApprove ;

    public ReferInfo(){}

    public ReferInfo(String verifiedTutorEmail, String verifiedTutorUid) {
        this.verifiedTutorEmail = verifiedTutorEmail ;
        this.verifiedTutorUid = verifiedTutorUid ;
        referApprove = "waiting" ;
    }

    public String getVerifiedTutorEmail() {
        return verifiedTutorEmail;
    }

    public void setVerifiedTutorEmail(String verifiedTutorEmail) {
        this.verifiedTutorEmail = verifiedTutorEmail;
    }

    public String getVerifiedTutorUid() {
        return verifiedTutorUid;
    }

    public void setVerifiedTutorUid(String verifiedTutorUid) {
        this.verifiedTutorUid = verifiedTutorUid;
    }

    public String getReferApprove() {
        return referApprove;
    }

    public void setReferApprove(String referApprove) {
        this.referApprove = referApprove;
    }
}
