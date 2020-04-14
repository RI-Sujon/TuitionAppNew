package com.example.tuitionapp.candidate_tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.example.tuitionapp.R;

import com.example.tuitionapp.verified_tutor.TutorSignUpActivityStep3;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TutorSignUpActivityStep1 extends AppCompatActivity {

    private EditText firstNameBox, lastNameBox, emailBox, mobileNumberBox , edu_instituteNameEditText ;
    private TextView edu_tutorSubjectTextView ;

    private Spinner areaAddressBox, currentPositionBox, edu_instituteNameBox, edu_tutorSubjectBox ;
    private RadioGroup genderBoxGroup ;
    private RadioButton genderBox ;

    private DatabaseReference databaseReference ;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar ;

    private String firstName="", lastName="", email="", mobileNumber="", gender="", areaAddress="", currentPosition="", edu_instituteName="", edu_tutorSubject="" ;

    private CandidateTutorInfo AccountInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step1);
        getSupportActionBar().hide();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("CandidateTutor").child(firebaseUser.getUid()) ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        genderBoxGroup = (RadioGroup)findViewById(R.id.gender) ;
        areaAddressBox = (Spinner) findViewById(R.id.areaAddress);
        currentPositionBox = (Spinner) findViewById(R.id.currentPosition);
        edu_instituteNameBox = (Spinner) findViewById(R.id.edu_instituteName);
        edu_tutorSubjectBox = (Spinner) findViewById(R.id.department);

        firstNameBox = findViewById(R.id.fName);
        lastNameBox = findViewById(R.id.lName);
        emailBox = findViewById(R.id.email);
        mobileNumberBox = findViewById(R.id.mobileNumber);
        genderBox = (RadioButton) findViewById((int) genderBoxGroup.getCheckedRadioButtonId());
        progressBar = findViewById(R.id.progressBar);
        edu_instituteNameEditText = findViewById(R.id.edu_instituteNameEditText) ;
        edu_tutorSubjectTextView = findViewById(R.id.departmentTextView) ;

        ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this,
                R.array.areaAddress_array, android.R.layout.simple_spinner_item);

        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        areaAddressBox.setAdapter(adapter0);

        emailBox.setText(firebaseUser.getEmail());
        emailBox.setEnabled(false);

        selectAreaAddress() ;
        selectCurrentPosition() ;
        selectInstituteName() ;
        selectTutorSubject() ;
    }

    public void signUpCompletion(View view) {

        progressBar.setVisibility(View.VISIBLE);

        firstName = firstNameBox.getText().toString().trim();
        lastName = lastNameBox.getText().toString().trim();
        email = emailBox.getText().toString().trim();
        mobileNumber = mobileNumberBox.getText().toString().trim();
        gender = genderBox.getText().toString().trim();
        areaAddress = areaAddressBox.getSelectedItem().toString();
        currentPosition = currentPositionBox.getSelectedItem().toString();
        edu_instituteName = edu_instituteNameBox.getSelectedItem().toString();
        edu_tutorSubject = edu_tutorSubjectBox.getSelectedItem().toString();

        AccountInfo = new CandidateTutorInfo(firstName, lastName, email, mobileNumber, gender, areaAddress, currentPosition, edu_instituteName, edu_tutorSubject);

        databaseReference.setValue(AccountInfo) ;
        Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
        goToTutorSignUpActivityStep2();
    }

    public void goToTutorSignUpActivityStep2(){
        Intent intent = new Intent(this, TutorSignUpActivityStep2.class);
        startActivity(intent);
        finish();
    }

    public void goToTutorSignUpActivityStep3(){
        Intent intent = new Intent(this, TutorSignUpActivityStep3.class);
        startActivity(intent);
        finish();
    }

    public void backFromSignUpT_Activity(View view){
        Intent intent = new Intent(this, TutorModuleStartActivity.class);
        startActivity(intent);
        finish();
    }

    public void selectAreaAddress() {
        areaAddressBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(areaAddressBox.getSelectedItemPosition()==0){
                    areaAddress = "" ;
                }
                else{
                    areaAddress = areaAddressBox.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
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
                        edu_tutorSubject = "" ;

                    }
                    else{
                        edu_instituteNameEditText.setVisibility(View.GONE);
                        edu_instituteNameBox.setVisibility(View.VISIBLE);
                        edu_tutorSubjectBox.setVisibility(View.VISIBLE);
                        edu_tutorSubjectTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
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
                // sometimes you need nothing here
            }
        });
    }

}