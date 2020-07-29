package com.example.tuitionapp_surji.candidate_tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Bundle;

import com.example.tuitionapp_surji.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TutorSignUpActivityStep1 extends AppCompatActivity {

    private EditText addressEditText , userNameEditText, mobileNumberEditText, edu_instituteNameEditText, attachedHallBox;
    private TextView edu_tutorSubjectTextView, attachedHallTextView, mobileNumberTextView, userNameTextView ;

    private Spinner genderBox, currentPositionBox, edu_instituteNameBox, edu_tutorSubjectBox ;

    private DatabaseReference myRefCandidateTutor ;
    private FirebaseUser firebaseUser;

    private String userName="" , email="", mobileNumber="", gender="", areaAddress="", currentPosition="", edu_instituteName="", edu_tutorSubject="", attachedHall="" ;

    private CandidateTutorInfo candidateTutorInfo,info;

    private String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step1);
        Intent intent = getIntent() ;
        type = intent.getStringExtra("type");

        userNameEditText = findViewById(R.id.userName);
        userNameTextView = findViewById(R.id.userNameTextView) ;
        mobileNumberEditText = findViewById(R.id.mobileNumber);
        mobileNumberTextView = findViewById(R.id.mobileNumberTextView) ;

        if(type.equals("google")||type.equals("signIn")){
            userNameEditText.setVisibility(View.VISIBLE);
            userNameTextView.setVisibility(View.VISIBLE);
            mobileNumberEditText.setVisibility(View.VISIBLE);
            mobileNumberTextView.setVisibility(View.VISIBLE);
        }
        else{
            userName = intent.getStringExtra("tutorName") ;
            mobileNumber = intent.getStringExtra("tutorMobileNumber") ;
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference().child("CandidateTutor").child(firebaseUser.getUid()) ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        genderBox = findViewById(R.id.gender) ;
        currentPositionBox = findViewById(R.id.currentPosition);
        edu_instituteNameBox = findViewById(R.id.edu_instituteName);
        edu_tutorSubjectBox = findViewById(R.id.departmentSpinner);
        attachedHallBox = findViewById(R.id.attachedHallSpinner) ;

        addressEditText = findViewById(R.id.fullAddress) ;
        edu_instituteNameEditText = findViewById(R.id.instituteNameEditText) ;
        edu_tutorSubjectTextView = findViewById(R.id.departmentTextView) ;
        attachedHallTextView = findViewById(R.id.attachedHallTextView) ;

        // progressBar = findViewById(R.id.progressBar);

        selectCurrentPosition() ;
        selectInstituteName() ;
        selectTutorSubject() ;
    }

    public void signUpCompletion(View view) {

        gender = genderBox.getSelectedItem().toString().trim();
        areaAddress = addressEditText.getText().toString().trim();
        currentPosition = currentPositionBox.getSelectedItem().toString();
        edu_instituteName = edu_instituteNameBox.getSelectedItem().toString();
        edu_tutorSubject = edu_tutorSubjectBox.getSelectedItem().toString();
        attachedHall = attachedHallBox.getText().toString();

        if(gender.equals("GENDER")){
            TextView textView = (TextView) genderBox.getSelectedView() ;
            textView.setError("");
            return;
        }
        if(currentPosition.equals("CURRENT POSITION")){
            TextView textView = (TextView) currentPositionBox.getSelectedView() ;
            textView.setError("");
            return;
        }
        if(edu_instituteName.equals("INSTITUTE")&&!currentPosition.equals("College")){
            TextView textView = (TextView) edu_instituteNameBox.getSelectedView() ;
            textView.setError("");
            return;
        }
        else if(currentPosition.equals("College")){
            edu_instituteName = edu_instituteNameEditText.getText().toString() ;
            if(edu_instituteName.equals("")){
                edu_instituteNameEditText.setError(""); ;
                return;
            }
        }
        if(edu_tutorSubject.equals("SUBJECT/DEPARTMENT")&& !currentPosition.equals("College")){
            TextView textView = (TextView) edu_tutorSubjectBox.getSelectedView() ;
            textView.setError("");
            return;
        }
        else if(currentPosition.equals("College")){
            edu_tutorSubject = "!" ;
        }

        if(attachedHall.equals("")&&!currentPosition.equals("College")){
            attachedHallBox.setError("");
            return;
        }
        else if(currentPosition.equals("College")){
            attachedHall = "!" ;
        }

        if(areaAddress.equals("")){
            addressEditText.setError("");
            return;
        }

        if(type.equals("google")||type.equals("signIn")){
            userName = userNameEditText.getText().toString() ;
            email = firebaseUser.getEmail() ;
            mobileNumber = mobileNumberEditText.getText().toString() ;

            if(userName.equals("")){
                userNameEditText.setError("");
                return;
            }
            if(mobileNumber.equals("")){
                mobileNumberEditText.setError("");
                return;
            }

            candidateTutorInfo = new CandidateTutorInfo(userName, email, mobileNumber, gender, areaAddress, currentPosition, edu_instituteName, edu_tutorSubject, attachedHall);
            myRefCandidateTutor.setValue(candidateTutorInfo) ;
            goToTutorSignUpActivityStep2();
        }
        else if(type.equals("signUp")){
            email = firebaseUser.getEmail() ;
            candidateTutorInfo = new CandidateTutorInfo(userName, email, mobileNumber, gender, areaAddress, currentPosition, edu_instituteName, edu_tutorSubject, attachedHall);
            myRefCandidateTutor.setValue(candidateTutorInfo) ;
            goToTutorSignUpActivityStep2();
        }
    }

    public void goToTutorSignUpActivityStep2(){
        Intent intent = new Intent(this, TutorSignUpActivityStep2.class);
        startActivity(intent);
        finish();
    }

    public void selectCurrentPosition(){
        currentPositionBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(currentPositionBox.getSelectedItemPosition()==0){
                    currentPosition = "" ;
                }
                else {
                    currentPosition = currentPositionBox.getSelectedItem().toString() ;
                    if(currentPosition.equals("College")){
                        edu_instituteNameEditText.setVisibility(View.VISIBLE);
                        edu_instituteNameBox.setVisibility(View.GONE);
                        edu_tutorSubjectBox.setVisibility(View.GONE);
                        edu_tutorSubjectTextView.setVisibility(View.GONE);
                        attachedHallBox.setVisibility(View.GONE);
                        attachedHallTextView.setVisibility(View.GONE);
                        edu_tutorSubject = "" ;
                        attachedHall = "" ;

                    }
                    else{
                        edu_instituteNameEditText.setVisibility(View.GONE);
                        edu_instituteNameBox.setVisibility(View.VISIBLE);
                        edu_tutorSubjectBox.setVisibility(View.VISIBLE);
                        edu_tutorSubjectTextView.setVisibility(View.VISIBLE);
                        attachedHallBox.setVisibility(View.VISIBLE);
                        attachedHallTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void selectInstituteName() {
        edu_instituteNameBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(edu_instituteNameBox.getSelectedItemPosition()==0){
                    edu_instituteName = "" ;
                }
                else {
                    if(currentPosition.equals("College")){
                        edu_instituteName = edu_instituteNameEditText.getText().toString() ;
                    }
                    else if(!currentPosition.equals("")){
                        edu_instituteName = edu_instituteNameBox.getSelectedItem().toString() ;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }
    public void selectTutorSubject() {
        edu_tutorSubjectBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(edu_tutorSubjectBox.getSelectedItemPosition()==0){
                    edu_tutorSubject = "" ;
                }
                else {
                    edu_tutorSubject = edu_tutorSubjectBox.getSelectedItem().toString() ;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}