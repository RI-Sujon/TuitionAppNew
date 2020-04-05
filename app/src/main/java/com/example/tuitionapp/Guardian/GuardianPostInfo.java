package com.example.tuitionapp.Guardian;

public class GuardianPostInfo {
    private String phoneNumberPrimaryKey,postTitle, studentInstitute, studentClass, studentGroup, studentMedium, studentSubjectList,
            tutorGenderPreference, daysPerWeekOrMonth, studentAreaAddress, studentFullAddress, studentContactNo, salary ;

    public GuardianPostInfo() {
    }

    public GuardianPostInfo(String phoneNumberPrimaryKey,String postTitle, String studentInstitute, String studentClass, String studentGroup, String studentMedium, String studentSubjectList, String tutorGenderPreference, String daysPerWeekOrMonth, String studentAreaAddress, String studentFullAddress, String studentContactNo, String salary) {
        this.phoneNumberPrimaryKey = phoneNumberPrimaryKey ;
        this.postTitle = postTitle;
        this.studentInstitute = studentInstitute;
        this.studentClass = studentClass;
        this.studentGroup = studentGroup;
        this.studentMedium = studentMedium;
        this.studentSubjectList = studentSubjectList;
        this.tutorGenderPreference = tutorGenderPreference;
        this.daysPerWeekOrMonth = daysPerWeekOrMonth;
        this.studentAreaAddress = studentAreaAddress;
        this.studentFullAddress = studentFullAddress;
        this.studentContactNo = studentContactNo;
        this.salary = salary;
    }

    public String getPhoneNumberPrimaryKey() {
        return phoneNumberPrimaryKey;
    }

    public void setPhoneNumberPrimaryKey(String phoneNumberPrimaryKey) {
        this.phoneNumberPrimaryKey = phoneNumberPrimaryKey;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getStudentInstitute() {
        return studentInstitute;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public String getStudentMedium() {
        return studentMedium;
    }

    public String getStudentSubjectList() {
        return studentSubjectList;
    }

    public String getTutorGenderPreference() {
        return tutorGenderPreference;
    }

    public String getDaysPerWeekOrMonth() {
        return daysPerWeekOrMonth;
    }

    public String getStudentAreaAddress() {
        return studentAreaAddress;
    }

    public String getStudentFullAddress() {
        return studentFullAddress;
    }

    public String getStudentContactNo() {
        return studentContactNo;
    }

    public String getSalary() {
        return salary;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setStudentInstitute(String studentInstitute) {
        this.studentInstitute = studentInstitute;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public void setStudentMedium(String studentMedium) {
        this.studentMedium = studentMedium;
    }

    public void setStudentSubjectList(String studentSubjectList) {
        this.studentSubjectList = studentSubjectList;
    }

    public void setTutorGenderPreference(String tutorGenderPreference) {
        this.tutorGenderPreference = tutorGenderPreference;
    }

    public void setDaysPerWeekOrMonth(String daysPerWeekOrMonth) {
        this.daysPerWeekOrMonth = daysPerWeekOrMonth;
    }

    @Override
    public String toString() {
        String strSub = studentSubjectList + "";
        if(strSub.length()>0) {
            strSub = strSub.replace("_", " || ");
            strSub = strSub.substring(0, strSub.length() - 4);
        }

        return "GuardianPostInfo{" +
                " Post Title = '" + postTitle + '\'' +
                ", Student Institute Name = '" + studentInstitute + '\'' +
                ", Student Class = '" + studentClass + '\'' +
                ", Student Group = '" + studentGroup + '\'' +
                ", Student Medium = '" + studentMedium + '\'' +
                ", Student Subject List = '" + strSub + '\'' +
                ", Tutor Gender Preference = '" + tutorGenderPreference + '\'' +
                ", Days Per Week = '" + daysPerWeekOrMonth + '\'' +
                ", Student Area Address = '" + studentAreaAddress + '\'' +
                ", Student Full Address = '" + studentFullAddress + '\'' +
                ", Student ContactNo = '" + studentContactNo + '\'' +
                ", Salary = '" + salary + '\'' +
                '}';
    }

    public void setStudentAreaAddress(String studentAreaAddress) {
        this.studentAreaAddress = studentAreaAddress;
    }

    public void setStudentFullAddress(String studentFullAddress) {
        this.studentFullAddress = studentFullAddress;
    }

    public void setStudentContactNo(String studentContactNo) {
        this.studentContactNo = studentContactNo;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
