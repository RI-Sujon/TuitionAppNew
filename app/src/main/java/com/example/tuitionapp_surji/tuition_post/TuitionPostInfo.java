package com.example.tuitionapp_surji.tuition_post;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TuitionPostInfo {
    private String postTitle, studentInstitute, studentClass, studentGroup, studentMedium, studentSubjectList,
            tutorGenderPreference, daysPerWeekOrMonth, studentAreaAddress, studentFullAddress, studentContactNo,
            salary , extra, availability, guardianMobileNumberFK,guardianUidFK;
    private String postDate ;
    private String postTime ;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a") ;
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy") ;

    public TuitionPostInfo() {
    }

    public TuitionPostInfo(String postTitle, String studentInstitute, String studentClass, String studentGroup, String studentMedium, String studentSubjectList, String tutorGenderPreference, String daysPerWeekOrMonth, String studentAreaAddress, String studentFullAddress, String studentContactNo, String salary, String extra, String availability, String guardianMobileNumberFK, String guardianUidFK) {
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
        this.extra = extra;
        this.availability = availability;
        this.guardianMobileNumberFK = guardianMobileNumberFK;
        this.guardianUidFK = guardianUidFK;
        postTime = simpleDateFormat.format(calendar.getTime());
        postDate = simpleDateFormat2.format(calendar.getTime());
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getStudentInstitute() {
        return studentInstitute;
    }

    public void setStudentInstitute(String studentInstitute) {
        this.studentInstitute = studentInstitute;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getStudentMedium() {
        return studentMedium;
    }

    public void setStudentMedium(String studentMedium) {
        this.studentMedium = studentMedium;
    }

    public String getStudentSubjectList() {
        return studentSubjectList;
    }

    public void setStudentSubjectList(String studentSubjectList) {
        this.studentSubjectList = studentSubjectList;
    }

    public String getTutorGenderPreference() {
        return tutorGenderPreference;
    }

    public void setTutorGenderPreference(String tutorGenderPreference) {
        this.tutorGenderPreference = tutorGenderPreference;
    }

    public String getDaysPerWeekOrMonth() {
        return daysPerWeekOrMonth;
    }

    public void setDaysPerWeekOrMonth(String daysPerWeekOrMonth) {
        this.daysPerWeekOrMonth = daysPerWeekOrMonth;
    }

    public String getStudentAreaAddress() {
        return studentAreaAddress;
    }

    public void setStudentAreaAddress(String studentAreaAddress) {
        this.studentAreaAddress = studentAreaAddress;
    }

    public String getStudentFullAddress() {
        return studentFullAddress;
    }

    public void setStudentFullAddress(String studentFullAddress) {
        this.studentFullAddress = studentFullAddress;
    }

    public String getStudentContactNo() {
        return studentContactNo;
    }

    public void setStudentContactNo(String studentContactNo) {
        this.studentContactNo = studentContactNo;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getGuardianMobileNumberFK() {
        return guardianMobileNumberFK;
    }

    public void setGuardianMobileNumberFK(String guardianMobileNumberFK) {
        this.guardianMobileNumberFK = guardianMobileNumberFK;
    }

    public String getGuardianUidFK() {
        return guardianUidFK;
    }

    public void setGuardianUidFK(String guardianUidFK) {
        this.guardianUidFK = guardianUidFK;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}
