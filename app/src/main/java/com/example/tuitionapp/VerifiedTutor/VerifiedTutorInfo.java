package com.example.tuitionapp.VerifiedTutor;

public class VerifiedTutorInfo {
    private String emailPK, preferredMediumOrVersion, preferredClasses, preferredGroup , preferredSubjects ,preferredAreas, experienceStatus, preferredDaysPerWeekOrMonth ,
            minimumSalary , profilePicture, DemoVideo, groupIdFK;

    public VerifiedTutorInfo() {
    }

    public VerifiedTutorInfo(String emailPK, String preferredMediumOrVersion, String preferredClasses, String preferredGroup, String preferredSubjects, String preferredAreas,
                             String experienceStatus, String preferredDaysPerWeekOrMonth, String minimumSalary, String profilePicture, String demoVideo, String groupIdFK) {
        this.emailPK = emailPK;
        this.preferredMediumOrVersion = preferredMediumOrVersion;
        this.preferredClasses = preferredClasses;
        this.preferredGroup = preferredGroup;
        this.preferredSubjects = preferredSubjects;
        this.preferredAreas = preferredAreas;
        this.experienceStatus = experienceStatus;
        this.preferredDaysPerWeekOrMonth = preferredDaysPerWeekOrMonth;
        this.minimumSalary = minimumSalary;
        this.profilePicture = profilePicture;
        DemoVideo = demoVideo;
        this.groupIdFK = groupIdFK;
    }

    public String getEmailPK() {
        return emailPK;
    }

    public void setEmailPK(String emailPK) {
        this.emailPK = emailPK;
    }

    public String getPreferredMediumOrVersion() {
        return preferredMediumOrVersion;
    }

    public void setPreferredMediumOrVersion(String preferredMediumOrVersion) {
        this.preferredMediumOrVersion = preferredMediumOrVersion;
    }

    public String getPreferredClasses() {
        return preferredClasses;
    }

    public void setPreferredClasses(String preferredClasses) {
        this.preferredClasses = preferredClasses;
    }

    public String getPreferredGroup() {
        return preferredGroup;
    }

    public void setPreferredGroup(String preferredGroup) {
        this.preferredGroup = preferredGroup;
    }

    public String getPreferredSubjects() {
        return preferredSubjects;
    }

    public void setPreferredSubjects(String preferredSubjects) {
        this.preferredSubjects = preferredSubjects;
    }

    public String getPreferredAreas() {
        return preferredAreas;
    }

    public void setPreferredAreas(String preferredAreas) {
        this.preferredAreas = preferredAreas;
    }

    public String getExperienceStatus() {
        return experienceStatus;
    }

    public void setExperienceStatus(String experienceStatus) {
        this.experienceStatus = experienceStatus;
    }

    public String getPreferredDaysPerWeekOrMonth() {
        return preferredDaysPerWeekOrMonth;
    }

    public void setPreferredDaysPerWeekOrMonth(String preferredDaysPerWeekOrMonth) {
        this.preferredDaysPerWeekOrMonth = preferredDaysPerWeekOrMonth;
    }

    public String getMinimumSalary() {
        return minimumSalary;
    }

    public void setMinimumSalary(String minimumSalary) {
        this.minimumSalary = minimumSalary;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDemoVideo() {
        return DemoVideo;
    }

    public void setDemoVideo(String demoVideo) {
        DemoVideo = demoVideo;
    }

    public String getGroupIdFK() {
        return groupIdFK;
    }

    public void setGroupIdFK(String groupIdFK) {
        this.groupIdFK = groupIdFK;
    }

    @Override
    public String toString() {
        String strClass = preferredClasses;
        if(strClass.length()>0) {
            strClass = strClass.replace("_","|") ;
            strClass = strClass.substring(0, strClass.length() - 1);
        }

        String strSub = preferredSubjects ;
        if(strSub.length()>0) {
            strSub = strSub.replace("_", "|");
            strSub = strSub.substring(0, strSub.length() - 1);
        }

        return
                " Medium = '" + preferredMediumOrVersion + '\'' +
                ", Preferred Class = '" + strClass +"" + '\'' +
                ", Preferred Group = '" + preferredGroup + '\'' +
                ", Preferred Subject = '" + strSub + "" + '\'' +
                ", Days Per Week ='" + preferredDaysPerWeekOrMonth + '\'' +
                ", Salary Upto = '" + minimumSalary + '\'' ;
    }
}
