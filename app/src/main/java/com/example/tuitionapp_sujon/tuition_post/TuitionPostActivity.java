package com.example.tuitionapp_sujon.tuition_post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_sujon.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_sujon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TuitionPostActivity extends AppCompatActivity {

    private EditText postTitleBox, studentInstituteBox, studentFullAreaAddressBox, studentContactNoBox, extraNotesBox;
    private MultiAutoCompleteTextView subjectBox ;
    private List<String> scienceSubjectList, commerceSubjectList, artsSubjectList, salaryList ;
    private ArrayAdapter subjectAdapter, salaryAdapter ;
    private Spinner mediumBox, classBox, groupBox, tutorGenderPreferenceBox, daysPerWeekOrMonthBox , areaAddressBox ,salaryBox, salaryBox2 ;
    private String postTitle="", studentInstitute="", studentClass="", studentGroup="", studentMedium="", studentSubjectList="",
            tutorGenderPreference="", daysPerWeekOrMonth="", studentAreaAddress="", studentFullAddress="", studentContactNo="", salary="",salary2="", extra="" ;

    private FirebaseDatabase database ;
    private DatabaseReference databaseReference ;
    private FirebaseUser firebaseUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post_create_new);

        database = FirebaseDatabase.getInstance() ;
        databaseReference = database.getReference("TuitionPost") ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    }



    protected void onStart() {
        super.onStart();

        postTitleBox = findViewById(R.id.postTitle) ;
        studentInstituteBox = findViewById(R.id.studentInstitute) ;
        classBox = (Spinner) findViewById(R.id.classSpinner);
        groupBox = (Spinner) findViewById(R.id.groupSpinner);
        mediumBox = (Spinner) findViewById(R.id.mediumSpinner);
        subjectBox = findViewById(R.id.subjectBox) ;
        tutorGenderPreferenceBox = (Spinner) findViewById(R.id.genderSpinner) ;
        daysPerWeekOrMonthBox = (Spinner) findViewById(R.id.daysPerWeekOrMonthSpinner) ;
        areaAddressBox = (Spinner) findViewById(R.id.areaAddressSpinner) ;
        studentFullAreaAddressBox = findViewById(R.id.fullAddress) ;
        studentContactNoBox = findViewById(R.id.phoneNumber) ;
        salaryBox = (Spinner) findViewById(R.id.salarySpinner);
        extraNotesBox = findViewById(R.id.extraNotes) ;
        salaryBox2 = findViewById(R.id.salarySpinner2) ;

        scienceSubjectList = Arrays.asList(getResources().getStringArray(R.array.scienceSubject_array)) ;
        commerceSubjectList = Arrays.asList(getResources().getStringArray(R.array.commerceSubject_array)) ;
        artsSubjectList = Arrays.asList(getResources().getStringArray(R.array.artsSubject_array)) ;
        salaryList = Arrays.asList(getResources().getStringArray(R.array.salary_array)) ;

        selectStudentClass();
        selectStudentGroup() ;
        selectStudentMedium();
        selectStudentSubjectList();
        selectTutorGenderPreference();
        selectDaysPerWeekOrMonth() ;
        selectAreaAddress();
        selectSalary() ;
    }

    public void createPostOperation(View view) {
        //progressBar = findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        postTitle = postTitleBox.getText().toString().trim() ;
        studentClass = classBox.getSelectedItem().toString();
        studentMedium = mediumBox.getSelectedItem().toString();
        studentSubjectList = subjectBox.getText().toString() ;
        tutorGenderPreference = tutorGenderPreferenceBox.getSelectedItem().toString();
        studentInstitute = studentInstituteBox.getText().toString().trim() ;
        studentFullAddress = studentFullAreaAddressBox.getText().toString().trim() ;
        studentContactNo = studentContactNoBox.getText().toString().trim() ;
        studentAreaAddress = areaAddressBox.getSelectedItem().toString();
        daysPerWeekOrMonth = daysPerWeekOrMonthBox.getSelectedItem().toString().trim() ;
        extra = extraNotesBox.getText().toString() ;

        if(postTitle.equals("")){
            postTitle = "NEED A TUTOR" ;
        }

        if(studentClass.equals("CLASS")){
            TextView textView = (TextView) classBox.getSelectedView() ;
            textView.setError("");
            return;
        }

        if(studentGroup.equals("GROUP")){
            studentGroup = "NULL" ;
        }

        if(studentMedium.equals("MEDIUM")){
            studentMedium = "Bangla" ;
        }

        if(studentSubjectList.equals("")){
            subjectBox.setError("");
            return;
        }else if(studentSubjectList.charAt(studentSubjectList.length()-1) == ',' || studentSubjectList.charAt(studentSubjectList.length()-1) == ' '){
            studentSubjectList = studentSubjectList.substring(0,studentSubjectList.length()-2) ;

            while(studentSubjectList.charAt(studentSubjectList.length()-1) == ',' || studentSubjectList.charAt(studentSubjectList.length()-1) == ' '){
                studentSubjectList = studentSubjectList.substring(0,studentSubjectList.length()-2) ;
            }
        }

        if(tutorGenderPreference.equals("GENDER")){
            tutorGenderPreference = "Both Male And Female" ;
        }

        if(studentAreaAddress.equals("AREA")){
            TextView textView = (TextView) areaAddressBox.getSelectedView() ;
            textView.setError("");
            return;
        }

        if(!salary.equals("Negotiable")) {
            salary2 = salaryBox2.getSelectedItem().toString();
        }
        if(!salary.equals(salary2)&&!salary.equals("Negotiable")){
            salary = salary + " to " + salary2 ;
        }

        String guardianMobileNumberFK = firebaseUser.getPhoneNumber() ;
        TuitionPostInfo guardianPostInfo = new TuitionPostInfo(postTitle, studentInstitute ,studentClass , studentGroup, studentMedium, studentSubjectList,
                tutorGenderPreference, daysPerWeekOrMonth, studentAreaAddress, studentFullAddress, studentContactNo, salary, extra, "Available", guardianMobileNumberFK,firebaseUser.getUid()) ;

        databaseReference.push().setValue(guardianPostInfo) ;
        Toast.makeText(getApplicationContext(),"successfully post",Toast.LENGTH_SHORT).show();
        goToGuardianTuitionPostViewActivity();
    }

    public void goToGuardianTuitionPostViewActivity(){
        Intent intent = new Intent(this, TuitionPostViewActivity.class);
        intent.putExtra("user","guardian") ;
        startActivity(intent);
        finish();
    }

    public void goToBackPage(View view){
        Intent intent = new Intent(this, TuitionPostViewActivity.class);
        intent.putExtra("user","guardian") ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPage(null);
    }

    public void selectStudentClass(){

    }

    public void selectStudentGroup() {
        groupBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (groupBox.getSelectedItemPosition() == 0) {
                    studentGroup = "" ;
                }
                else {
                    studentGroup = groupBox.getSelectedItem().toString();
                    if(studentGroup.equals("Science")){
                        subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,scienceSubjectList);
                    }
                    else if(studentGroup.equals("Commerce")){
                        subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,commerceSubjectList);
                    }
                    else if(studentGroup.equals("Arts")){
                        subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,artsSubjectList);
                    }
                    else if(studentGroup.equals("Science")){
                        subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,scienceSubjectList);
                    }

                    subjectBox.setAdapter(subjectAdapter);
                    subjectBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }


    public void selectStudentMedium() {

    }


    public void selectStudentSubjectList(){

        subjectBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectBox.showDropDown();
            }
        });

    }

    public void selectTutorGenderPreference() {
    }

    public void selectDaysPerWeekOrMonth() {

    }

    public void selectAreaAddress() {

    }

    public void selectSalary() {
        salaryBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                salary = salaryBox.getSelectedItem().toString();
                if(salary.equals("Negotiable")){
                    salaryBox2.setEnabled(false);
                }
                else {
                    salaryBox2.setEnabled(true);
                    List<String> salaryList2 = new ArrayList<>() ;
                    for(int i=position; i<salaryList.size() ;i++){
                        salaryList2.add(salaryList.get(i)) ;
                    }
                    salaryAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,salaryList2);
                    salaryBox2.setAdapter(salaryAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }
}
