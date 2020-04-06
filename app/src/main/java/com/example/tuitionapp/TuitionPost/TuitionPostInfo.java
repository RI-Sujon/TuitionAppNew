package com.example.tuitionapp.TuitionPost;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class TuitionPostInfo {
    private String postIdPK,postTitle, studentInstitute, studentClass, studentGroup, studentMedium, studentSubjectList,
            tutorGenderPreference, daysPerWeekOrMonth, studentAreaAddress, studentFullAddress, studentContactNo, salary , extra, guardianMobileNumberFK;
    private String postDate ;
    private String postTime ;

    public TuitionPostInfo() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TuitionPostInfo(String postIdPK, String postTitle, String studentInstitute, String studentClass, String studentGroup, String studentMedium, String studentSubjectList, String tutorGenderPreference, String daysPerWeekOrMonth,
                           String studentAreaAddress, String studentFullAddress, String studentContactNo, String salary, String extra, String guardianMobileNumberFK) {
        this.postIdPK = postIdPK;
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
        this.guardianMobileNumberFK = guardianMobileNumberFK;
        postDate = LocalDate.now().toString() ;
        postTime = LocalTime.now().toString() ;
    }

    public String getPostIdPK() {
        return postIdPK;
    }

    public void setPostIdPK(String postIdPK) {
        this.postIdPK = postIdPK;
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

    public String getGuardianMobileNumberFK() {
        return guardianMobileNumberFK;
    }

    public void setGuardianMobileNumberFK(String guardianMobileNumberFK) {
        this.guardianMobileNumberFK = guardianMobileNumberFK;
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


}
