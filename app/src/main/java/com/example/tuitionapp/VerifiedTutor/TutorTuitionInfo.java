package com.example.tuitionapp.VerifiedTutor;

public class TutorTuitionInfo {
    private String medium, preferredClass , preferredGroup , preferredSubject , daysPerWeekOrMonth , salaryUpto, emailPrimaryKey;

    public TutorTuitionInfo() {
    }

    public TutorTuitionInfo(String medium, String preferredClass, String preferredGroup, String preferredSubject, String daysPerWeekOrMonth, String salaryUpto, String emailPrimaryKey) {
        this.medium = medium;
        this.preferredClass = preferredClass;
        this.preferredGroup = preferredGroup;
        this.preferredSubject = preferredSubject;
        this.daysPerWeekOrMonth = daysPerWeekOrMonth;
        this.salaryUpto = salaryUpto;
        this.emailPrimaryKey = emailPrimaryKey ;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getPreferredClass() {
        return preferredClass;
    }

    public void setPreferredClass(String preferredClass) {
        this.preferredClass = preferredClass;
    }

    public String getPreferredGroup() {
        return preferredGroup;
    }

    public void setPreferredGroup(String preferredGroup) {
        this.preferredGroup = preferredGroup;
    }

    public String getPreferredSubject() {
        return preferredSubject;
    }

    public void setPreferredSubject(String preferredSubject) {
        this.preferredSubject = preferredSubject;
    }

    public String getDaysPerWeekOrMonth() {
        return daysPerWeekOrMonth;
    }

    public void setDaysPerWeekOrMonth(String daysPerWeekOrMonth) {
        this.daysPerWeekOrMonth = daysPerWeekOrMonth;
    }

    public String getSalaryUpto() {
        return salaryUpto;
    }

    public void setSalaryUpto(String salaryUpto) {
        this.salaryUpto = salaryUpto;
    }

    public String getEmailPrimaryKey() {
        return emailPrimaryKey;
    }

    public void setEmailPrimaryKey(String emailPrimaryKey) {
        this.emailPrimaryKey = emailPrimaryKey;
    }

    @Override
    public String toString() {
        String strClass = preferredClass;
        if(strClass.length()>0) {
            strClass = strClass.replace("_","|") ;
            strClass = strClass.substring(0, strClass.length() - 1);
        }

        String strSub = preferredSubject ;
        if(strSub.length()>0) {
            strSub = strSub.replace("_", "|");
            strSub = strSub.substring(0, strSub.length() - 1);
        }

        return
                " Medium = '" + medium + '\'' +
                ", Preferred Class = '" + strClass +"" + '\'' +
                ", Preferred Group = '" + preferredGroup + '\'' +
                ", Preferred Subject = '" + strSub + "" + '\'' +
                ", Days Per Week ='" + daysPerWeekOrMonth + '\'' +
                ", Salary Upto = '" + salaryUpto + '\'' ;
    }
}
