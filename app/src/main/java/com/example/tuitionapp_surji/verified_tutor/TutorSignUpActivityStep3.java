package com.example.tuitionapp_surji.verified_tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorSignUpActivityStep3 extends AppCompatActivity{

    private EditText experienceStatusBox ;
    private MultiAutoCompleteTextView preferredClassBox, preferredSubjectBox, daysPerWeekOrMonthBox ;
    private Spinner minimumSalaryBox, groupBox, mediumBox, preferredAreaBox ;
    private String medium="", preferredClass="", preferredGroup="", preferredSubject="", preferredAreas="", experienceStatus="",daysPerWeekOrMonth="", minimumSalary="";
    private String emailPK="" ;

    List<String> mediumArray, classArray, groupArray, subjectArray,daysPerWeekArray;

    private DatabaseReference myRefCandidateTutor, myRefVerifiedTutor ;
    private FirebaseUser firebaseUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step3);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(firebaseUser.getUid().toString()) ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(firebaseUser.getUid().toString()) ;
    }

    protected void onStart() {
        super.onStart();

        mediumBox = findViewById(R.id.mediumBox);
        preferredClassBox = findViewById(R.id.classBox);
        preferredSubjectBox = findViewById(R.id.subjectBox);
        daysPerWeekOrMonthBox = findViewById(R.id.daysPerWeekOrMonthBox);
        minimumSalaryBox = findViewById(R.id.minimumSalaryBox);
        groupBox = findViewById(R.id.groupBox);
        preferredAreaBox = findViewById(R.id.areaAddressBox) ;
        experienceStatusBox = findViewById(R.id.experienceStatusBox) ;

        mediumArray = Arrays.asList(getResources().getStringArray(R.array.medium_array));
        classArray = Arrays.asList(getResources().getStringArray(R.array.preferredClass_array_bangla_medium));
        groupArray = Arrays.asList(getResources().getStringArray(R.array.group_array));
        subjectArray = Arrays.asList(getResources().getStringArray(R.array.scienceSubject_array));
        daysPerWeekArray = Arrays.asList(getResources().getStringArray(R.array.daysPerWeekOrMonth_array));

        selectMedium() ;
        selectPreferredClass() ;
        selectPreferredGroup() ;
        selectPreferredSubject() ;
        selectDaysPerWeekOrMonth() ;
    }

    public void signUpCompletion(View view) {
        medium = mediumBox.getSelectedItem().toString() ;
        preferredClass = preferredClassBox.getText().toString() ;
        preferredGroup = groupBox.getSelectedItem().toString() ;
        preferredSubject = preferredClassBox.getText().toString() ;
        daysPerWeekOrMonth = daysPerWeekOrMonthBox.getText().toString() ;
        preferredAreas = preferredAreaBox.getSelectedItem().toString() ;
        minimumSalary = minimumSalaryBox.getSelectedItem().toString() ;
        emailPK = firebaseUser.getEmail().toString() ;
        experienceStatus =experienceStatusBox.getText().toString() ;

        if(!preferredClass.equals("")){
            while(preferredClass.charAt(preferredClass.length()-1)==','||preferredClass.charAt(preferredClass.length()-1)==' '){
                preferredClass = preferredClass.substring(0,preferredClass.length()-2) ;
            }
        }
        if(!preferredSubject.equals("")){
            while(preferredSubject.charAt(preferredSubject.length()-1)==','||preferredSubject.charAt(preferredSubject.length()-1)==' '){
                preferredSubject = preferredSubject.substring(0,preferredSubject.length()-2) ;
            }
        }

        if(!daysPerWeekOrMonth.equals("")){
            while(daysPerWeekOrMonth.charAt(daysPerWeekOrMonth.length()-1)==','||daysPerWeekOrMonth.charAt(daysPerWeekOrMonth.length()-1)==' '){
                daysPerWeekOrMonth = daysPerWeekOrMonth.substring(0,daysPerWeekOrMonth.length()-2) ;
            }
        }

        if(medium.equals("MEDIUM")){
            medium = "Bangla Medium" ;
        }
        if(preferredGroup.equals("GROUP")){
            preferredGroup = "" ;
        }
        if(preferredAreas.equals("AREA")){
            preferredAreas = "" ;
        }

        VerifiedTutorInfo verifiedTutorInfo = new VerifiedTutorInfo(emailPK, medium, preferredClass, preferredGroup, preferredSubject, preferredAreas ,
                experienceStatus, daysPerWeekOrMonth, minimumSalary) ;

        myRefVerifiedTutor.setValue(verifiedTutorInfo) ;

        Map<String,Object> update = new HashMap<>() ;

        update.put("tutorAvailable",true);

        myRefCandidateTutor.updateChildren(update) ;

        Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
        goToSetProfilePictureActivity();
    }

    public void goToSetProfilePictureActivity(){
        Intent intent = new Intent(this, VerifiedTutorSetProfilePicture.class);
        intent.putExtra("intentFlag", "registration") ;
        intent.putExtra("userEmail", firebaseUser.getEmail()) ;
        intent.putExtra("userUid", firebaseUser.getUid()) ;
        startActivity(intent);
        finish();
    }

    public void selectMedium() {

        mediumBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectPreferredClass(){

        ArrayAdapter classAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, classArray);
        preferredClassBox.setAdapter(classAdapter);
        preferredClassBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        preferredClassBox.setThreshold(1);

        preferredClassBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferredClassBox.showDropDown();
            }
        });

        preferredClassBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*if(preferredClassBox.getSelectedItemPosition()==0){
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
                preferredClassBox.setSelection(0);*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }


    public void selectPreferredGroup() {

        //ArrayAdapter groupAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, groupArray);
        //groupBox.setAdapter(groupAdapter);
        //preferredGroupBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        groupBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*if (preferredGroupBox.getSelectedItemPosition() == 0) {
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
                }/*/            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectPreferredSubject(){

        ArrayAdapter subjectAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, subjectArray);
        preferredSubjectBox.setAdapter(subjectAdapter);
        preferredSubjectBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        preferredSubjectBox.setThreshold(1);

        preferredSubjectBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferredSubjectBox.showDropDown();
            }
        });

        preferredSubjectBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*if(preferredSubjectBox.getSelectedItemPosition()==0){
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
                preferredSubjectBox.setSelection(0);*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void selectDaysPerWeekOrMonth() {
        ArrayAdapter daysPerWeekAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, daysPerWeekArray);
        daysPerWeekOrMonthBox.setAdapter(daysPerWeekAdapter);
        daysPerWeekOrMonthBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        preferredClassBox.setThreshold(1);

        daysPerWeekOrMonthBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                daysPerWeekOrMonthBox.showDropDown();
                return false;
            }
        });

        daysPerWeekOrMonthBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /* (daysPerWeekOrMonthBox.getSelectedItemPosition() == 0) {
                }
                else {
                    String str = daysPerWeekOrMonthBox.getSelectedItem().toString();
                    daysPerWeekOrMonthTextView.setText(str);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
    }

    public void setArrayAdapter(String flag){

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
