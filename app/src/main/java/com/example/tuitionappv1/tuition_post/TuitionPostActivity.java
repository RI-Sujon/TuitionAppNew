package com.example.tuitionappv1.tuition_post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionappv1.guardian.GuardianHomePageActivity;
import com.example.tuitionappv1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;

public class TuitionPostActivity extends AppCompatActivity {

    private EditText postTitleBox, studentInstituteBox, studentFullAreaAddressBox, studentContactNoBox;
    private TextView [] preferredSubjectTextView = new TextView[6];
    private TextView daysPerWeekOrMonthTextView ;

    private Spinner mediumBox, classBox, groupBox, subjectBox, tutorGenderPreferenceBox, daysPerWeekOrMonthBox , areaAddressBox ,salaryBox ;

    private String postId="", postTitle="", studentInstitute="", studentClass="", studentGroup="", studentMedium="", studentSubjectList="",
            tutorGenderPreference="", daysPerWeekOrMonth="", studentAreaAddress="", studentFullAddress="", studentContactNo="", salary="", extra="" ;
    private LocalDate postDate ;
    private LocalTime postTime ;

    private Button removeSubjectButton;

    private FirebaseDatabase database ;
    private DatabaseReference databaseReference ;
    private FirebaseUser firebaseUser ;
    private ProgressBar progressBar ;

    private  int iSubject = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post);

        getSupportActionBar().hide();

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
        subjectBox = (Spinner) findViewById(R.id.subjectSpinner) ;
        tutorGenderPreferenceBox = (Spinner) findViewById(R.id.genderSpinner) ;
        daysPerWeekOrMonthBox = (Spinner) findViewById(R.id.daysPerWeekOrMonthSpinner) ;
        areaAddressBox = (Spinner) findViewById(R.id.areaAddressSpinner) ;
        studentFullAreaAddressBox = findViewById(R.id.fullAddress) ;
        studentContactNoBox = findViewById(R.id.phoneNumber) ;
        salaryBox = (Spinner) findViewById(R.id.salarySpinner);

        preferredSubjectTextView[0] = findViewById(R.id.subjectTextView1) ;
        preferredSubjectTextView[1] = findViewById(R.id.subjectTextView2) ;
        preferredSubjectTextView[2] = findViewById(R.id.subjectTextView3) ;
        preferredSubjectTextView[3] = findViewById(R.id.subjectTextView4) ;
        preferredSubjectTextView[4] = findViewById(R.id.subjectTextView5) ;
        preferredSubjectTextView[5] = findViewById(R.id.subjectTextView6) ;
        removeSubjectButton = findViewById(R.id.removeSubject) ;
        daysPerWeekOrMonthTextView = findViewById(R.id.daysPerWeekOrMonthTextView) ;


        selectStudentClass();
        selectStudentGroup() ;
        selectStudentMedium();
        selectStudentSubjectList();
        selectTutorGenderPreference();
        selectDaysPerWeekOrMonth() ;
        selectAreaAddress();
        selectSalary() ;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void signUpCompletion(View view) {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        for(int i=0 ; i<iSubject ; i++){
            studentSubjectList = studentSubjectList + preferredSubjectTextView[i].getText().toString() + "_" ;
        }

        postTitle = postTitleBox.getText().toString().trim() ;
        studentInstitute = studentInstituteBox.getText().toString().trim() ;
        studentFullAddress = studentFullAreaAddressBox.getText().toString().trim() ;
        studentContactNo = studentContactNoBox.getText().toString().trim() ;
        daysPerWeekOrMonth = daysPerWeekOrMonthTextView.getText().toString().trim() ;

        studentFullAddress = studentFullAddress.replace(",","_") ;

        String guardianMobileNumberFK = firebaseUser.getPhoneNumber() ;
        TuitionPostInfo guardianPostInfo = new TuitionPostInfo(postTitle, studentInstitute ,studentClass , studentGroup, studentMedium, studentSubjectList,
                tutorGenderPreference,daysPerWeekOrMonth,studentAreaAddress,studentFullAddress,studentContactNo,salary,extra,guardianMobileNumberFK,firebaseUser.getUid()) ;

        databaseReference.push().setValue(guardianPostInfo) ;
        Toast.makeText(getApplicationContext(),"successfully post",Toast.LENGTH_SHORT).show();
        goToGuardianHomePageActivity();
    }

    public void goToGuardianHomePageActivity(){
        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void backFromSignUpT_Activity(View view){
        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        backFromSignUpT_Activity(null);
    }

    public void selectStudentClass(){

        classBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(classBox.getSelectedItemPosition()==0){
                    studentClass = "" ;
                }
                else {
                    studentClass = classBox.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }


    public void selectStudentMedium() {
        mediumBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mediumBox.getSelectedItemPosition() == 0) {
                }
                else {
                    studentMedium = mediumBox.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }


    public void selectStudentSubjectList(){

        subjectBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(subjectBox.getSelectedItemPosition()==0){
                }
                else if (iSubject < 6){
                    String str = subjectBox.getSelectedItem().toString();
                    int flag = -1 ;
                    for(int i=0 ; i<iSubject ; i++){
                        if(str.equals(preferredSubjectTextView[i].getText().toString())){
                            flag = 1 ;
                            break;
                        }
                    }
                    if(flag==-1) {
                        preferredSubjectTextView[iSubject].setText(str);
                        preferredSubjectTextView[iSubject].setVisibility(View.VISIBLE);
                        iSubject++;
                    }
                }
                subjectBox.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        removeSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSubject-- ;
                preferredSubjectTextView[iSubject].setText("");
                preferredSubjectTextView[iSubject].setVisibility(View.GONE);
            }
        });
    }

    public void selectTutorGenderPreference() {
        tutorGenderPreferenceBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tutorGenderPreference = tutorGenderPreferenceBox.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectDaysPerWeekOrMonth() {
        daysPerWeekOrMonthBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (daysPerWeekOrMonthBox.getSelectedItemPosition() == 0) {
                }
                else {
                    String str = daysPerWeekOrMonthBox.getSelectedItem().toString();
                    daysPerWeekOrMonthTextView.setText(str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectAreaAddress() {
        areaAddressBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (areaAddressBox.getSelectedItemPosition() == 0) {
                }
                else {
                    studentAreaAddress = areaAddressBox.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectSalary() {
        salaryBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (salaryBox.getSelectedItemPosition() == 0) {
                }
                else {
                    salary = salaryBox.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }
}
