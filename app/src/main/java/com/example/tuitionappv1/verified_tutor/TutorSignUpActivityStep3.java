package com.example.tuitionappv1.verified_tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionappv1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TutorSignUpActivityStep3 extends AppCompatActivity{
    private TextView [] preferredClassTextView = new TextView[6];
    private TextView preferredGroupTextView , mediumTextView , daysPerWeekOrMonthTextView;
    private TextView [] preferredSubjectTextView = new TextView[6];

    private Spinner mediumBox, preferredClassBox, preferredGroupBox, preferredSubjectBox , daysPerWeekOrMonthBox ,salaryUptoBox ;
    private String medium="", preferredClass="", preferredGroup="", preferredSubject="", preferredAreas="", experianceStatus="",daysPerWeekOrMonth="", minimumSalary="";
    private String profilePicture,demoVideo ;
    private String emailPK="" ,groupIdFK;
    private Button removeClassButton , removeSubjectButton ;

    private FirebaseDatabase database ;
    private DatabaseReference databaseReference ;
    private FirebaseUser firebaseUser ;
    private ProgressBar progressBar ;

    private  int iClass = 0 , iSubject = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step3);
        getSupportActionBar().hide();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        database = FirebaseDatabase.getInstance() ;
        databaseReference = database.getReference("VerifiedTutor").child(firebaseUser.getUid().toString()) ;
    }

    protected void onStart() {
        super.onStart();

        mediumTextView = findViewById(R.id.mediumTextView) ;

        preferredClassTextView[0] = findViewById(R.id.classTextView1) ;
        preferredClassTextView[1] = findViewById(R.id.classTextView2) ;
        preferredClassTextView[2] = findViewById(R.id.classTextView3) ;
        preferredClassTextView[3] = findViewById(R.id.classTextView4) ;
        preferredClassTextView[4] = findViewById(R.id.classTextView5) ;
        preferredClassTextView[5] = findViewById(R.id.classTextView6) ;

        preferredGroupTextView = findViewById(R.id.groupTextView) ;

        preferredSubjectTextView[0] = findViewById(R.id.subjectTextView1) ;
        preferredSubjectTextView[1] = findViewById(R.id.subjectTextView2) ;
        preferredSubjectTextView[2] = findViewById(R.id.subjectTextView3) ;
        preferredSubjectTextView[3] = findViewById(R.id.subjectTextView4) ;
        preferredSubjectTextView[4] = findViewById(R.id.subjectTextView5) ;
        preferredSubjectTextView[5] = findViewById(R.id.subjectTextView6) ;

        daysPerWeekOrMonthTextView = findViewById(R.id.daysPerWeekOrMonthTextView) ;

        mediumBox = (Spinner) findViewById(R.id.mediumSpinner);
        preferredClassBox = (Spinner) findViewById(R.id.classSpinner);
        preferredGroupBox = (Spinner) findViewById(R.id.groupSpinner);
        preferredSubjectBox = (Spinner) findViewById(R.id.subjectSpinner);
        daysPerWeekOrMonthBox = (Spinner) findViewById(R.id.daysPerWeekOrMonthSpinner);
        salaryUptoBox = (Spinner) findViewById(R.id.salaryRangeSpinner);

        removeClassButton = findViewById(R.id.removeClass) ;
        removeSubjectButton = findViewById(R.id.removeSubject) ;

        selectMedium();
        selectPreferredClass();
        selectPreferredGroup() ;
        selectPreferredSubject();
        selectDaysPerWeekOrMonth() ;
    }

    public void signUpCompletion(View view) {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        medium = mediumTextView.getText().toString() ;
        for(int i=0 ; i<iClass ; i++){
            preferredClass = preferredClass + preferredClassTextView[i].getText().toString()+"_" ;
        }

        for(int i=0 ; i<iSubject ; i++){
            preferredSubject = preferredSubject + preferredSubjectTextView[i].getText().toString() + "_" ;
        }
        preferredGroup = preferredGroupTextView.getText().toString().trim() ;
        daysPerWeekOrMonth = daysPerWeekOrMonthTextView.getText().toString().trim() ;
        minimumSalary = salaryUptoBox.getSelectedItem().toString() ;
        emailPK = firebaseUser.getEmail().toString() ;

        VerifiedTutorInfo verifiedTutorInfo = new VerifiedTutorInfo(emailPK, medium, preferredClass, preferredGroup, preferredSubject, preferredAreas ,
                experianceStatus, daysPerWeekOrMonth, minimumSalary) ;


        databaseReference.setValue(verifiedTutorInfo) ;
        Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
        goToSetProfilePictureActivity();
    }

    public void goToSetProfilePictureActivity(){
        Intent intent = new Intent(this, VerifiedTutorSetProfilePicture.class);
        intent.putExtra("userEmail", firebaseUser.getEmail()) ;
        intent.putExtra("userUid", firebaseUser.getUid()) ;
        startActivity(intent);
        finish();
    }

    /*public void backFromSignUpT_Activity(View view){
        Intent intent = new Intent(this, TutorModuleStartActivity.class);
        startActivity(intent);
        finish();
    }*/

    public void selectMedium() {
        mediumBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mediumBox.getSelectedItemPosition() == 0) {
                }
                else {
                    String str = mediumBox.getSelectedItem().toString();
                    mediumTextView.setText(str);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectPreferredClass(){

        preferredClassBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(preferredClassBox.getSelectedItemPosition()==0){
                }
                else if (iClass < 6){
                    String str = preferredClassBox.getSelectedItem().toString();
                    int flag = -1 ;
                    for(int i=0 ; i<iClass ; i++){
                        if(str.equals(preferredClassTextView[i].getText().toString())){
                            flag = 1 ;
                            break;
                        }
                    }
                    if(flag==-1) {
                        preferredClassTextView[iClass].setText(str);
                        preferredClassTextView[iClass].setVisibility(View.VISIBLE);
                        iClass++;
                    }
                }
                preferredClassBox.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        removeClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClass-- ;
                preferredClassTextView[iClass].setText("");
                preferredClassTextView[iClass].setVisibility(View.GONE);
            }
        });
    }


    public void selectPreferredGroup() {
        preferredGroupBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (preferredGroupBox.getSelectedItemPosition() == 0) {
                }
                else {
                    String str = preferredGroupBox.getSelectedItem().toString();
                    if(str.equals("Science")){
                        setArrayAdapter("Science");
                    }
                    else if(str.equals("Commerce")){
                        setArrayAdapter("Commerce");
                    }
                    else if(str.equals("Arts")){
                        setArrayAdapter("Arts");
                    }
                    preferredGroupTextView.setText(str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectPreferredSubject(){

        preferredSubjectBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(preferredSubjectBox.getSelectedItemPosition()==0){
                }
                else if (iSubject < 6){
                    String str = preferredSubjectBox.getSelectedItem().toString();
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
                preferredSubjectBox.setSelection(0);
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

    public void setArrayAdapter(String flag){
        for ( ; iSubject>0 ;) {
            iSubject-- ;
            preferredSubjectTextView[iSubject].setText("");
            preferredSubjectTextView[iSubject].setVisibility(View.GONE);
        }

        if(flag=="Science"){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.scienceSubject_array, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            preferredSubjectBox.setAdapter(adapter);
        }

        if(flag=="Commerce"){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.commerceSubject_array, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            preferredSubjectBox.setAdapter(adapter);
        }

        if(flag=="Arts"){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.artsSubject_array, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            preferredSubjectBox.setAdapter(adapter);
        }
    }
}
