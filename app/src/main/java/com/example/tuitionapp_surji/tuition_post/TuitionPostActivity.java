package com.example.tuitionapp_surji.tuition_post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TuitionPostActivity extends AppCompatActivity {

    private EditText postTitleBox, studentInstituteBox, studentFullAreaAddressBox, studentContactNoBox, extraNotesBox;
    private MultiAutoCompleteTextView subjectBox ;
    private ArrayList<String> mediumList, banglaMediumClassList, englishMediumClassList, groupList, scienceSubjectList, commerceSubjectList,
            artsSubjectList, generalSubjectList, tutorGenderPreferenceList, areaAddressList, daysPerWeekList, salaryList ;
    private ArrayAdapter mediumAdapter, classAdapter, groupAdapter, subjectAdapter, daysPerWeekAdapter, salaryAdapter ;
    private Spinner mediumBox, classBox, groupBox, tutorGenderPreferenceBox, daysPerWeekOrMonthBox , areaAddressBox ,salaryBox, salaryBox2 ;
    private String postTitle="", studentInstitute="", studentClass="", studentGroup="", studentMedium="", studentSubjectList="",
            tutorGenderPreference="", daysPerWeekOrMonth="", studentAreaAddress="", studentFullAddress="", studentContactNo="", salary="",salary2="", extra="" ;
    private  TuitionPostInfo tuitionPostInfo ;
    private DatabaseReference myRefTuitionPost ;
    private FirebaseUser firebaseUser ;
    private TextView createPostLayoutHeading ;

    private TextView groupTextView ;

    private String postID, type ;
    private int typeFlag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post_create_new);

        myRefTuitionPost = FirebaseDatabase.getInstance().getReference("TuitionPost") ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        Intent intent = getIntent() ;
        type = intent.getStringExtra("type") ;

        if(type.equals("editPost")) {
            typeFlag = -1;
            postID = intent.getStringExtra("tuitionPostID");
            myRefTuitionPost = myRefTuitionPost.child(postID);

            myRefTuitionPost.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tuitionPostInfo = dataSnapshot.getValue(TuitionPostInfo.class);
                    editPostOperation();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else typeFlag = 1 ;

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

        groupTextView = findViewById(R.id.group_text_view) ;

        createPostLayoutHeading = findViewById(R.id.create_post_layout_heading) ;

        mediumList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.medium_array)) ) ;
        mediumList.remove(mediumList.size()-1) ;

        banglaMediumClassList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.preferredClass_array_bangla_medium))) ;
        englishMediumClassList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.preferredClass_array_english_medium))) ;
        groupList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.group_array))) ;
        scienceSubjectList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.scienceSubject_array))) ;
        commerceSubjectList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.commerceSubject_array))) ;
        artsSubjectList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.artsSubject_array))) ;
        generalSubjectList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.primarySubject_array))) ;
        tutorGenderPreferenceList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.preferable_gender_array))) ;
        areaAddressList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.areaAddress_array))) ;
        daysPerWeekList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.daysPerWeekOrMonth_array))) ;
        salaryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.salary_array))) ;

        mediumAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line, mediumList);
        mediumBox.setAdapter(mediumAdapter);

        subjectAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line, generalSubjectList);
        subjectBox.setAdapter(subjectAdapter);
        subjectBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        daysPerWeekAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line, daysPerWeekList);
        daysPerWeekOrMonthBox.setAdapter(daysPerWeekAdapter);

        selectStudentClass() ;
        selectStudentGroup() ;
        selectStudentMedium();
        selectStudentSubjectList();
        selectTutorGenderPreference();
        selectDaysPerWeekOrMonth() ;
        selectAreaAddress() ;
        selectSalary() ;
    }

    public void createPostOperation(View view) {

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

        if(type.equals("editPost")){
            myRefTuitionPost.setValue(guardianPostInfo) ;
        }
        else myRefTuitionPost.push().setValue(guardianPostInfo) ;


        Toast.makeText(getApplicationContext(),"successfully post",Toast.LENGTH_SHORT).show();
        goToGuardianTuitionPostViewActivity();
    }

    private void editPostOperation(){
        createPostLayoutHeading.setText("Edit Your Post");

        postTitleBox.setText(tuitionPostInfo.getPostTitle());

        if(tuitionPostInfo.getStudentMedium().equals("English Medium")){
            mediumBox.setSelection(2);
            classAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line,englishMediumClassList);
            classBox.setAdapter(classAdapter);
            for(int i=0 ; i<englishMediumClassList.size() ; i++){
                if(englishMediumClassList.get(i).equals(tuitionPostInfo.getStudentClass())){
                    classBox.setSelection(i);
                    break;
                }
            }
        }
        else{
            classAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line,banglaMediumClassList);
            classBox.setAdapter(classAdapter);
            mediumBox.setSelection(1);
            for(int i=0 ; i<banglaMediumClassList.size() ; i++){
                System.out.println(banglaMediumClassList.get(i) + "/   /" + tuitionPostInfo.getStudentClass());
                if(banglaMediumClassList.get(i).equals(tuitionPostInfo.getStudentClass())){
                    classBox.setSelection(i);
                    break;
                }
            }
        }


        if(!tuitionPostInfo.getStudentGroup().equals("")){
            groupAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line,groupList);
            groupBox.setAdapter(groupAdapter);
            for(int i=0 ; i<groupList.size() ; i++){
                if(groupList.get(i).equals(tuitionPostInfo.getStudentGroup())){
                    groupBox.setSelection(i);
                    break;
                }
            }
        }

        subjectBox.setText(tuitionPostInfo.getStudentSubjectList() + ", ");
        for(int i=0 ; i<tutorGenderPreferenceList.size() ; i++){
            if(tutorGenderPreferenceList.get(i).equals(tuitionPostInfo.getTutorGenderPreference())){
                tutorGenderPreferenceBox.setSelection(i);
                break;
            }
        }
        studentInstituteBox.setText(tuitionPostInfo.getStudentInstitute());
        studentFullAreaAddressBox.setText(tuitionPostInfo.getStudentFullAddress()) ;
        studentContactNoBox.setText(tuitionPostInfo.getStudentContactNo()) ;
        for(int i=0 ; i<areaAddressList.size() ; i++){
            if(areaAddressList.get(i).equals(tuitionPostInfo.getTutorGenderPreference())){
                areaAddressBox.setSelection(i);
                break;
            }
        }
        for(int i=0 ; i<daysPerWeekList.size() ; i++){
            System.out.println(daysPerWeekList.get(i) + "/   /" + tuitionPostInfo.getDaysPerWeekOrMonth());
            if(daysPerWeekList.get(i).equals(tuitionPostInfo.getDaysPerWeekOrMonth())){
                System.out.println(daysPerWeekList.get(i) + "/  sujon /" + tuitionPostInfo.getDaysPerWeekOrMonth());
                daysPerWeekOrMonthBox.setSelection(i);
                break;
            }
        }

        for(int i=0 ; i<areaAddressList.size() ; i++){
            if(areaAddressList.get(i).equals(tuitionPostInfo.getStudentAreaAddress())){
                areaAddressBox.setSelection(i);
                break;
            }
        }
        extraNotesBox.setText(tuitionPostInfo.getExtra()) ;

        int flag = 0 ;
        String salary1 = "", salary2="" ;
        for(int i=0 ; i<tuitionPostInfo.getSalary().length() ; i++){
            if(flag==0){
                salary1 = salary1 + tuitionPostInfo.getSalary().charAt(i) ;
            }
            if(flag==2){
                salary2 = salary2 + tuitionPostInfo.getSalary().charAt(i) ;
            }
            if(tuitionPostInfo.getSalary().charAt(i)==' '){
                flag = flag+1;
                continue;
            }
        }

        for(int i=0 ; i<salaryList.size() ; i++){
            if(salaryList.get(i).equals(salary1)){
                salaryBox.setSelection(i);
                break;
            }
        }

        for(int i=0 ; i<salaryList.size() ; i++){
            if(salaryList.get(i).equals(salary2)){
                salaryBox2.setSelection(i);
                break;
            }
        }

        typeFlag = 1 ;
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

    public void selectStudentMedium() {
        mediumBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(typeFlag==1){
                    if(mediumList.get(position).equals("English Medium")) {
                        classAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line,englishMediumClassList);
                    }
                    else {
                        classAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line,banglaMediumClassList);
                    }
                    classBox.setAdapter(classAdapter) ;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void selectStudentClass(){
        classBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(typeFlag==1){
                   groupAdapter = new ArrayAdapter(TuitionPostActivity.this, android.R.layout.simple_dropdown_item_1line,groupList);
                   groupBox.setAdapter(groupAdapter);
                   if(position>5){
                        groupBox.setVisibility(View.GONE);
                        groupTextView.setVisibility(View.GONE);
                    }
                    else{
                        groupBox.setVisibility(View.VISIBLE);
                        groupTextView.setVisibility(View.VISIBLE);
                   }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void selectStudentGroup() {
        groupBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(typeFlag==1){
                    if (groupBox.getSelectedItemPosition() == 0) {
                        studentGroup = "" ;
                    }
                    else {
                        studentGroup = groupBox.getSelectedItem().toString();
                        if(studentGroup.equals("Science")){
                            for(int i=0 ; i<generalSubjectList.size() ; i++){
                                scienceSubjectList.add(generalSubjectList.get(i)) ;
                            }
                            subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,scienceSubjectList);
                        }
                        else if(studentGroup.equals("Commerce")){
                            for(int i=0 ; i<generalSubjectList.size() ; i++){
                                commerceSubjectList.add(generalSubjectList.get(i)) ;
                            }
                            subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,commerceSubjectList);
                        }
                        else if(studentGroup.equals("Arts")){
                            for(int i=0 ; i<generalSubjectList.size() ; i++){
                                artsSubjectList.add(generalSubjectList.get(i)) ;
                            }
                            subjectAdapter = new ArrayAdapter(TuitionPostActivity.this,android.R.layout.simple_dropdown_item_1line,artsSubjectList);
                        }


                        subjectBox.setAdapter(subjectAdapter);
                        subjectBox.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });
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
