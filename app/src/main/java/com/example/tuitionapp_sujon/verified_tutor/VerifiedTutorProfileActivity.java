package com.example.tuitionapp_sujon.verified_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tuitionapp_sujon.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_sujon.admin.AdminTutorProfileViewActivity;
import com.example.tuitionapp_sujon.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_sujon.group.GroupHomePageActivity;
import com.example.tuitionapp_sujon.guardian.ViewingSearchingTutorProfileActivity;
import com.example.tuitionapp_sujon.message_box.MessageBoxInfo;
import com.example.tuitionapp_sujon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.graphics.Color.GRAY;

public class VerifiedTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefCandidateTutorInfo, myRefVerifiedTutorInfo, myRefMessageBox, myRefReport, myRefApproveAndBlockInfo ;
    private FirebaseUser firebaseUser ;

    private CandidateTutorInfo candidateTutorInfo;
    private VerifiedTutorInfo verifiedTutorInfo;
    private ApproveAndBlockInfo approveAndBlockInfo ;
    private MessageBoxInfo messageBoxInfo;

    private String user ,groupID,tutorUid;
    private ArrayList<ReportInfo>reportInfoArrayList ;
    private ArrayList<String> userInfo ;

    private EditText phoneNumber,email,gender,areaAddress,currentPosition,instituteName,subject ;
    private EditText medium,preferredClass,preferredGroup,preferredSubject,daysPerWeekOrMonth,preferredLocation,minimumSalary ;
    private Spinner preferredGroupInvisible, mediumInvisible ;
    private EditText experienceEditText ;
    private ImageView userProfilePicImageView ;
    private ImageView idCardImageView ;
    private TextView userNameTextView, status1, status2;
    private Button approvedAndBlockButton;
    private ImageButton messageRequestButton ;
    private ListView reportListView ;
    private LinearLayout layoutForAdmin ;

    private ViewFlipper viewFlipper ;
    private ImageButton aboutButton, excellenceButton, experienceButton ;
    private LinearLayout mobileNumberRow, emailRow, addressRow, subjectRow , preferredMediumRow, preferredClassRow, preferredGroupRow, preferredSubjectRow, preferredAreaRow , preferredDaysPerWeekRow, preferredMinimumSalaryRow;

    private ImageButton privacyMobileNumber, privacyAddress, privacyEmail, privacySubject ;
    private ImageButton privacyMedium, privacyClass, privacyGroup, privacySubjectList, privacyArea, privacyDaysPerWeek, privacySalary ;

    private int mobileNumberFlag = 0, addressFlag = 0, emailFlag = 0, subjectFlag = 0;
    private int mediumFlag = 0, classFlag = 0, groupFlag = 0, subjectListFlag = 0, areaFlag = 0, daysPerWeekFlag = 0, salaryFlag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_profile_new);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;
        tutorUid = intent.getStringExtra("tutorUid");

        userProfilePicImageView = findViewById(R.id.profilePicImageView) ;
        idCardImageView = findViewById(R.id.idCardImageView) ;
        userNameTextView = findViewById(R.id.userName) ;
        status1 = findViewById(R.id.status1) ;
        status2 = findViewById(R.id.status2) ;
        reportListView = findViewById(R.id.reportList) ;
        layoutForAdmin = findViewById(R.id.layoutForAdmin) ;

        myRefCandidateTutorInfo= FirebaseDatabase.getInstance().getReference("CandidateTutor");
        myRefVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor");
        myRefReport = FirebaseDatabase.getInstance().getReference("Report");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;

            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(userInfo.get(3)) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(userInfo.get(3)) ;
        }
        else if(user.equals("groupAdmin")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            groupID = intent.getStringExtra("groupID") ;
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
        }
        else if(user.equals("groupVisitor")){
            groupID = intent.getStringExtra("groupID") ;
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
        }
        else if(user.equals("admin") || user.equals("admin2") || user.equals("admin3")){
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;

            reportInfoArrayList = new ArrayList<>() ;
            layoutForAdmin.setVisibility(View.VISIBLE);

            myRefReport.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1:dataSnapshot.getChildren()){
                        ReportInfo reportInfo = dS1.getValue(ReportInfo.class) ;
                        if(reportInfo.getTutorUid().equals(tutorUid)){
                            reportInfoArrayList.add(reportInfo) ;
                        }
                    }
                    goToReportTutorListView() ;
                    myRefReport.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            }) ;

            approvedAndBlockButton = findViewById(R.id.approve_block_button) ;
            approvedAndBlockButton.setVisibility(View.VISIBLE);

            myRefApproveAndBlockInfo = FirebaseDatabase.getInstance().getReference("ApproveAndBlock").child(tutorUid) ;

            myRefApproveAndBlockInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    approveAndBlockInfo = dataSnapshot.getValue(ApproveAndBlockInfo.class) ;
                    if(approveAndBlockInfo.getStatus().equals("waiting")){
                        approvedAndBlockButton.setText("APPROVE");
                        approvedAndBlockButton.setBackgroundColor(Color.rgb(4,70,4));
                    }
                    else if(approveAndBlockInfo.getStatus().equals("running") ){
                        approvedAndBlockButton.setText("BLOCK");
                        approvedAndBlockButton.setBackgroundColor(Color.RED);
                    }
                    else if(approveAndBlockInfo.getStatus().equals("blocked")){
                        approvedAndBlockButton.setText("UNBLOCK");
                        approvedAndBlockButton.setBackgroundColor(Color.BLUE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            }) ;
        }
        else if(user.equals("guardian")){
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
            messageRequestButton = findViewById(R.id.messageRequestButton) ;
            messageRequestButton.setVisibility(View.VISIBLE);
        }

        myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                    addCandidateTutorInfoToProfile();
                    myRefCandidateTutorInfo.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        myRefVerifiedTutorInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class) ;
                    addVerifiedTutorInfoToProfile();
                    myRefVerifiedTutorInfo.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }) ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber = findViewById(R.id.mobile_number) ;
        email  = findViewById(R.id.email) ;
        gender = findViewById(R.id.gender) ;
        areaAddress = findViewById(R.id.address) ;
        currentPosition = findViewById(R.id.current_position) ;
        instituteName = findViewById(R.id.institute_name) ;
        subject = findViewById(R.id.subject_department) ;

        medium = findViewById(R.id.medium) ;
        mediumInvisible = findViewById(R.id.medium_invisible) ;
        preferredClass = findViewById(R.id.preferred_class) ;
        preferredGroup = findViewById(R.id.preferred_group) ;
        preferredGroupInvisible = findViewById(R.id.preferred_group_invisible) ;
        preferredSubject = findViewById(R.id.preferred_subject) ;
        preferredLocation = findViewById(R.id.preferred_location) ;
        daysPerWeekOrMonth = findViewById(R.id.preferred_days_per_week) ;
        minimumSalary = findViewById(R.id.preferred_minimum_salary) ;
        viewFlipper = findViewById(R.id.view_flipper) ;
        aboutButton = findViewById(R.id.about_button) ;
        excellenceButton = findViewById(R.id.excellence_button) ;
        experienceButton = findViewById(R.id.experience_button) ;
        experienceEditText = findViewById(R.id.experience) ;

        privacyMobileNumber = findViewById(R.id.privacy_mobile_number) ;
        privacyAddress = findViewById(R.id.privacy_address) ;
        privacyEmail = findViewById(R.id.privacy_email) ;
        privacySubject = findViewById(R.id.privacy_subject) ;
        privacyMedium = findViewById(R.id.privacy_medium) ;
        privacyClass = findViewById(R.id.privacy_class) ;
        privacyGroup = findViewById(R.id.privacy_group) ;
        privacySubjectList = findViewById(R.id.privacy_subject_list) ;
        privacyArea = findViewById(R.id.privacy_area) ;
        privacyDaysPerWeek = findViewById(R.id.privacy_days_per_week) ;
        privacySalary = findViewById(R.id.privacy_salary) ;

        mobileNumberRow = findViewById(R.id.mobile_number_row) ;
        emailRow = findViewById(R.id.email_row) ;
        addressRow = findViewById(R.id.address_row) ;
        subjectRow = findViewById(R.id.subject_department_row) ;
        preferredMediumRow = findViewById(R.id.medium_row) ;
        preferredClassRow = findViewById(R.id.class_row) ;
        preferredGroupRow = findViewById(R.id.group_row) ;
        preferredSubjectRow = findViewById(R.id.subject_list_row) ;
        preferredAreaRow = findViewById(R.id.preferred_area_row) ;
        preferredDaysPerWeekRow = findViewById(R.id.days_per_week_row) ;
        preferredMinimumSalaryRow = findViewById(R.id.minimum_salary_row) ;

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(0);
                aboutButton.setImageResource(R.drawable.about_blue);
                excellenceButton.setImageResource(R.drawable.exc_gray);
                experienceButton.setImageResource(R.drawable.exp_gray);
            }
        });
        excellenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(1);
                aboutButton.setImageResource(R.drawable.about_gray);
                excellenceButton.setImageResource(R.drawable.exc_blue);
                experienceButton.setImageResource(R.drawable.exp_gray);
            }
        });
        experienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(2);
                aboutButton.setImageResource(R.drawable.about_gray);
                excellenceButton.setImageResource(R.drawable.exc_gray);
                experienceButton.setImageResource(R.drawable.exp_blue);
            }
        });

        if(user.equals("groupVisitor") || user.equals("groupAdmin")){
            excellenceButton.setVisibility(View.GONE);
            experienceButton.setVisibility(View.GONE);
        }

    }

    public void addCandidateTutorInfoToProfile(){
        if(candidateTutorInfo.getProfilePictureUri()==null) {
            if (candidateTutorInfo.getGender().equals("FEMALE")) {
                userProfilePicImageView.setImageResource(R.drawable.female_pic);
            } else {
                userProfilePicImageView.setImageResource(R.drawable.male_pic);
            }
        }
        else {
            Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(userProfilePicImageView);
        }

        userNameTextView.setText(candidateTutorInfo.getUserName());
        if(!candidateTutorInfo.getEdu_tutorSubject().equals("!")) {
            status1.setText("Studies " + candidateTutorInfo.getEdu_tutorSubject() + " at ");
        }else {
            status1.setText("Studies at ");
        }
        status2.setText(candidateTutorInfo.getEdu_instituteName());

        String emailText=candidateTutorInfo.getEmailPK() ;

        if(emailText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
            emailText = emailText.substring(1,emailText.length()-1) ;
            privacyEmail.setImageResource(R.drawable.only_me);
        }
        else if(emailText.charAt(0)=='-' && user.equals("guardian") || user.equals("groupVisitor") || user.equals("groupAdmin")){
            emailRow.setVisibility(View.GONE);
        }
        else if(user.equals("tutor")){
            privacyEmail.setImageResource(R.drawable.open_privacy);
        }
        email.setText(emailText);

        String addressText=candidateTutorInfo.getAreaAddress() ;
        if(addressText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
            addressText = addressText.substring(1, addressText.length()) ;
            privacyAddress.setImageResource(R.drawable.only_me);
        }
        else if(addressText.charAt(0)=='-' && (user.equals("guardian") || user.equals("groupVisitor") || user.equals("groupAdmin"))){
            addressRow.setVisibility(View.GONE);
        }
        else if(user.equals("tutor")){
            privacyAddress.setImageResource(R.drawable.open_privacy);
        }
        areaAddress.setText(addressText);

        String mobileNumberText=candidateTutorInfo.getMobileNumber() ;
        if(mobileNumberText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
            mobileNumberText = mobileNumberText.substring(1, mobileNumberText.length()) ;
            privacyMobileNumber.setImageResource(R.drawable.only_me);
        }
        else if(mobileNumberText.charAt(0)=='-' && (user.equals("guardian") || user.equals("groupVisitor") || user.equals("groupAdmin"))){
            mobileNumberRow.setVisibility(View.GONE);
        }
        else if(user.equals("tutor")){
            privacyMobileNumber.setImageResource(R.drawable.open_privacy);
        }
        phoneNumber.setText(mobileNumberText);

        String subjectText=candidateTutorInfo.getEdu_tutorSubject() ;
        if(subjectText.charAt(0)=='-' && (user.equals("guardian") || user.equals("groupVisitor") || user.equals("groupAdmin")) || subjectText.charAt(0)=='!'){
            subjectRow.setVisibility(View.GONE);
        }
        else if(subjectText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
            subjectText = subjectText.substring(1,subjectText.length()) ;
            privacySubject.setImageResource(R.drawable.only_me);
        }
        else if(user.equals("tutor")){
            privacySubject.setImageResource(R.drawable.open_privacy);
        }
        subject.setText(subjectText);

        gender.setText( candidateTutorInfo.getGender() );
        currentPosition.setText(candidateTutorInfo.getCurrentPosition());
        instituteName.setText( candidateTutorInfo.getEdu_instituteName());

        if(user.equals("admin")){
            Picasso.get().load(candidateTutorInfo.getIdCardImageUri()).into(idCardImageView) ;
        }
    }

    public void addVerifiedTutorInfoToProfile(){

        String mediumText = verifiedTutorInfo.getPreferredMediumOrVersion() ;
        if(mediumText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
            mediumText = mediumText.substring(1,mediumText.length()) ;
            privacyMedium.setImageResource(R.drawable.only_me);
        }
        else if(mediumText.charAt(0)=='-' && user.equals("guardian")){
            preferredMediumRow.setVisibility(View.GONE);
        }
        else if(user.equals("tutor")){
            privacyMedium.setImageResource(R.drawable.open_privacy);
        }
        medium.setText(mediumText);

        String classesText=verifiedTutorInfo.getPreferredClasses() ;
        if(!classesText.equals("")){
            if(classesText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
                classesText = classesText.substring(1,classesText.length()) ;
                privacyClass.setImageResource(R.drawable.only_me);
            }
            else if(classesText.charAt(0)=='-' && user.equals("guardian")){
                preferredClassRow.setVisibility(View.GONE);
            }
            else if(user.equals("tutor")){
                privacyClass.setImageResource(R.drawable.open_privacy);
            }
        }
        else if(user.equals("guardian")){
            preferredClassRow.setVisibility(View.GONE);
        }

        preferredClass.setText(classesText);

        String groupText=verifiedTutorInfo.getPreferredGroup() ;
        if(!groupText.equals("")){
            if(groupText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
                groupText = groupText.substring(1,groupText.length()) ;
                privacyGroup.setImageResource(R.drawable.only_me);
            }
            else if(groupText.charAt(0)=='-' && user.equals("guardian")){
                preferredGroupRow.setVisibility(View.GONE);
            }
            else if(user.equals("tutor")){
                privacyGroup.setImageResource(R.drawable.open_privacy);
            }
        }
        else if(user.equals("guardian")){
            preferredGroupRow.setVisibility(View.GONE);
        }
        preferredGroup.setText(groupText);

        String subjectListText=verifiedTutorInfo.getPreferredSubjects() ;
        if(!subjectListText.equals("")){
            if(subjectListText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
                subjectListText = subjectListText.substring(1,subjectListText.length()) ;
                privacySubjectList.setImageResource(R.drawable.only_me);
            }
            else if(subjectListText.charAt(0)=='-' && user.equals("guardian")){
                preferredSubjectRow.setVisibility(View.GONE);
            }
            else if(user.equals("tutor")){
                privacySubjectList.setImageResource(R.drawable.open_privacy);
            }
        }
        else if(user.equals("guardian")){
            preferredSubjectRow.setVisibility(View.GONE);
        }
        preferredSubject.setText(subjectListText);

        String areaText=verifiedTutorInfo.getPreferredAreas() ;
        if(!areaText.equals("")){
            if(areaText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
                areaText = areaText.substring(1,areaText.length()) ;
                privacyArea.setImageResource(R.drawable.only_me);
            }
            else if(user.equals("guardian")){
                preferredAreaRow.setVisibility(View.GONE);
            }
            else if(user.equals("tutor")){
                privacyArea.setImageResource(R.drawable.open_privacy);
            }
        }
        else if(  user.equals("guardian")){
            preferredAreaRow.setVisibility(View.GONE);
        }
        preferredLocation.setText(areaText);

        String daysPerWeekText=verifiedTutorInfo.getPreferredDaysPerWeekOrMonth() ;
        if(!daysPerWeekText.equals("")){
            if(daysPerWeekText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
                daysPerWeekText = daysPerWeekText.substring(1,daysPerWeekText.length()) ;
                privacyDaysPerWeek.setImageResource(R.drawable.only_me);
            }
            else if(daysPerWeekText.charAt(0)=='-' && user.equals("guardian")){
                preferredDaysPerWeekRow.setVisibility(View.GONE);
            }
            else if(user.equals("tutor")){
                privacyDaysPerWeek.setImageResource(R.drawable.open_privacy);
            }
        }
        else if(user.equals("guardian")){
            preferredDaysPerWeekRow.setVisibility(View.GONE);
        }
        daysPerWeekOrMonth.setText(daysPerWeekText);

        String salaryText=verifiedTutorInfo.getMinimumSalary() ;
        if(!salaryText.equals("")){
            if(salaryText.charAt(0)=='-' && (user.equals("tutor")||user.equals("admin"))){
                salaryText = salaryText.substring(1,salaryText.length()) ;
                privacySalary.setImageResource(R.drawable.only_me);
            }
            else if(salaryText.charAt(0)=='-' && user.equals("guardian")){
                preferredMinimumSalaryRow.setVisibility(View.GONE);
            }
            else if(user.equals("tutor")){
                privacySalary.setImageResource(R.drawable.open_privacy);
            }
        }
        else if(user.equals("guardian")){
            preferredMinimumSalaryRow.setVisibility(View.GONE);
        }

        minimumSalary.setText(salaryText);

        experienceEditText.setText(verifiedTutorInfo.getExperienceStatus());
    }

    public void goToReportTutorListView(){
        CustomAdapterForReportTutor adapter = new CustomAdapterForReportTutor(this,reportInfoArrayList) ;
        reportListView.setAdapter(adapter);
    }

    public void editProfile(){
        Button saveButton = findViewById(R.id.save_profile_button) ;
        saveButton.setVisibility(View.VISIBLE);

        phoneNumber.setEnabled(true);
        areaAddress.setEnabled(true);
        currentPosition.setEnabled(true);

        if(medium.getText().toString().equals("Bangla")){
            mediumInvisible.setSelection(1);
        }
        else if(medium.getText().toString().equals("English")){
            mediumInvisible.setSelection(2);
        }
        else if(medium.getText().toString().equals("Both")){
            mediumInvisible.setSelection(3);
        }

        mediumInvisible.setVisibility(View.VISIBLE);
        medium.setVisibility(View.GONE);
        preferredClass.setEnabled(true);

        if(preferredGroup.getText().toString().equals("Science")){
            preferredGroupInvisible.setSelection(1);
        }
        else if(preferredGroup.getText().toString().equals("Commerce")){
            preferredGroupInvisible.setSelection(2);
        }
        else if(preferredGroup.getText().toString().equals("Arts")){
            preferredGroupInvisible.setSelection(3);
        }

        preferredGroupInvisible.setVisibility(View.VISIBLE);
        preferredGroup.setVisibility(View.GONE);
        preferredSubject.setEnabled(true);
        daysPerWeekOrMonth.setEnabled(true);
        minimumSalary.setEnabled(true);
        experienceEditText.setEnabled(true);

        final PopupMenu popupMobileNumber = new PopupMenu(VerifiedTutorProfileActivity.this, privacyMobileNumber);
        final PopupMenu popupAddress = new PopupMenu(VerifiedTutorProfileActivity.this, privacyAddress);
        final PopupMenu popupEmail = new PopupMenu(VerifiedTutorProfileActivity.this, privacyEmail);
        final PopupMenu popupSubject = new PopupMenu(VerifiedTutorProfileActivity.this, privacySubject);
        final PopupMenu popupMedium = new PopupMenu(VerifiedTutorProfileActivity.this, privacyMedium);
        final PopupMenu popupClasses = new PopupMenu(VerifiedTutorProfileActivity.this, privacyClass);
        final PopupMenu popupGroup = new PopupMenu(VerifiedTutorProfileActivity.this, privacyGroup);
        final PopupMenu popupSubjectList = new PopupMenu(VerifiedTutorProfileActivity.this, privacySubjectList);
        final PopupMenu popupArea = new PopupMenu(VerifiedTutorProfileActivity.this, privacyArea);
        final PopupMenu popupDaysPerWeek = new PopupMenu(VerifiedTutorProfileActivity.this, privacyDaysPerWeek);
        final PopupMenu popupSalary = new PopupMenu(VerifiedTutorProfileActivity.this, privacySalary);

        if(!candidateTutorInfo.getMobileNumber().equals("")){
            if(candidateTutorInfo.getMobileNumber().charAt(0)=='-'){
                mobileNumberFlag = -1 ;
            }
            else mobileNumberFlag = 1 ;
        }
        else mobileNumberFlag = 1 ;

        if(!candidateTutorInfo.getAreaAddress().equals("")){
            if(candidateTutorInfo.getAreaAddress().charAt(0)=='-'){
                addressFlag = -1 ;
            }
            else addressFlag = 1 ;
        }
        else addressFlag = 1 ;

        if(!candidateTutorInfo.getEmailPK().equals("")){
            if(candidateTutorInfo.getEmailPK().charAt(0)=='-'){
                emailFlag = -1 ;
            }
            else emailFlag = 1 ;
        }
        else emailFlag = 1 ;

        if(!verifiedTutorInfo.getPreferredMediumOrVersion().equals("")){
            if(verifiedTutorInfo.getPreferredMediumOrVersion().charAt(0)=='-'){
                mediumFlag = -1 ;
            }
            else mediumFlag = 1 ;

        }
        else mediumFlag = 1 ;

        if(!verifiedTutorInfo.getPreferredClasses().equals("")){
            if(verifiedTutorInfo.getPreferredClasses().charAt(0)=='-'){
                classFlag = -1 ;
            }
            else classFlag = 1 ;
        }
        else classFlag = 1 ;

        if(!verifiedTutorInfo.getPreferredGroup().equals("")){
            if(verifiedTutorInfo.getPreferredGroup().charAt(0)=='-'){
                groupFlag = -1 ;
            }
            else groupFlag = 1 ;        }
        else groupFlag = 1 ;

        if(!verifiedTutorInfo.getPreferredSubjects().equals("")){
            if(verifiedTutorInfo.getPreferredSubjects().charAt(0)=='-'){
                subjectListFlag = -1 ;
            }
            else subjectListFlag = 1 ;        }
        else subjectListFlag = 1 ;

        if(!verifiedTutorInfo.getPreferredAreas().equals("")){
            if(verifiedTutorInfo.getPreferredAreas().charAt(0)=='-'){
                areaFlag = -1 ;
            }
            else areaFlag = 1 ;
        }
        else areaFlag = 1 ;

        if(!verifiedTutorInfo.getPreferredDaysPerWeekOrMonth().equals("")){
            if(verifiedTutorInfo.getPreferredDaysPerWeekOrMonth().charAt(0)=='-'){
                daysPerWeekFlag = -1 ;
            }
            else daysPerWeekFlag = 1 ;
        }
        else daysPerWeekFlag = 1 ;

        if(!verifiedTutorInfo.getMinimumSalary().equals("")){
            if(verifiedTutorInfo.getMinimumSalary().charAt(0)=='-'){
                salaryFlag = -1 ;
            }
            else salaryFlag = 1 ;
        }
        else salaryFlag = 1 ;

        popupMobileNumber.getMenuInflater().inflate(R.menu.privacy_pop_up, popupMobileNumber.getMenu());
        privacyMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMobileNumber.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyMobileNumber.setImageResource(R.drawable.only_me);
                            mobileNumberFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyMobileNumber.setImageResource(R.drawable.open_privacy);
                            mobileNumberFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupMobileNumber.show();
            }
        });

        popupAddress.getMenuInflater().inflate(R.menu.privacy_pop_up, popupAddress.getMenu());
        privacyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddress.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyAddress.setImageResource(R.drawable.only_me);
                            addressFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyAddress.setImageResource(R.drawable.open_privacy);
                            addressFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupAddress.show();
            }
        });

        popupEmail.getMenuInflater().inflate(R.menu.privacy_pop_up, popupEmail.getMenu());
        privacyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupEmail.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyEmail.setImageResource(R.drawable.only_me);
                            emailFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyEmail.setImageResource(R.drawable.open_privacy);
                            emailFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupEmail.show();
            }
        });


        popupSubject.getMenuInflater().inflate(R.menu.privacy_pop_up, popupSubject.getMenu());
        privacySubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSubject.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacySubject.setImageResource(R.drawable.only_me);
                            subjectFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacySubject.setImageResource(R.drawable.open_privacy);
                            subjectFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupSubject.show();
            }
        });


        popupMedium.getMenuInflater().inflate(R.menu.privacy_pop_up, popupMedium.getMenu());
        privacyMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMedium.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyMedium.setImageResource(R.drawable.only_me);
                            mediumFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyMedium.setImageResource(R.drawable.open_privacy);
                            mediumFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupMedium.show();
            }
        });

        popupClasses.getMenuInflater().inflate(R.menu.privacy_pop_up, popupClasses.getMenu());
        privacyClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupClasses.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyClass.setImageResource(R.drawable.only_me);
                            classFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyClass.setImageResource(R.drawable.open_privacy);
                            classFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupClasses.show();
            }
        });


        popupGroup.getMenuInflater().inflate(R.menu.privacy_pop_up, popupGroup.getMenu());
        privacyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupGroup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyGroup.setImageResource(R.drawable.only_me);
                            groupFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyGroup.setImageResource(R.drawable.open_privacy);
                            groupFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupGroup.show();
            }
        });


        popupSubjectList.getMenuInflater().inflate(R.menu.privacy_pop_up, popupSubjectList.getMenu());
        privacySubjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSubjectList.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacySubjectList.setImageResource(R.drawable.only_me);
                            subjectListFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacySubjectList.setImageResource(R.drawable.open_privacy);
                            subjectListFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupSubjectList.show();
            }
        });


        popupArea.getMenuInflater().inflate(R.menu.privacy_pop_up, popupArea.getMenu());
        privacyArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupArea.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyArea.setImageResource(R.drawable.only_me);
                            areaFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyArea.setImageResource(R.drawable.open_privacy);
                            areaFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupArea.show();
            }
        });


        popupDaysPerWeek.getMenuInflater().inflate(R.menu.privacy_pop_up, popupDaysPerWeek.getMenu());
        privacyDaysPerWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDaysPerWeek.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacyDaysPerWeek.setImageResource(R.drawable.only_me);
                            daysPerWeekFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacyDaysPerWeek.setImageResource(R.drawable.open_privacy);
                            daysPerWeekFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupDaysPerWeek.show();
            }
        });


        popupSalary.getMenuInflater().inflate(R.menu.privacy_pop_up, popupSalary.getMenu());
        privacySalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSalary.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Only Me")){
                            privacySalary.setImageResource(R.drawable.only_me);
                            salaryFlag = -1 ;
                        }
                        else if(item.getTitle().equals("Public")){
                            privacySalary.setImageResource(R.drawable.open_privacy);
                            salaryFlag = 1 ;
                        }
                        return true;
                    }
                });
                popupSalary.show();
            }
        });
    }

    public void saveProfile(View view){
        String phoneNumberText = "", areaAddressText="", emailText="", subjectText="";
        String mediumText="", classesText="", groupText="", subjectListText="", areaText="",daysPerWeekText="",salaryText="";

        phoneNumberText = phoneNumber.getText().toString() ;
        if(mobileNumberFlag==1 && !phoneNumberText.equals("")){
            if(phoneNumberText.charAt(0)=='-'){
                phoneNumberText = phoneNumberText.substring(1,phoneNumberText.length()) ;
            }
        }
        else if(mobileNumberFlag==-1 && !phoneNumberText.equals("")){
            if(phoneNumberText.charAt(0)!='-'){
                phoneNumberText = "-" + phoneNumberText ;
            }
        }
        candidateTutorInfo.setMobileNumber(phoneNumberText);

        emailText= email.getText().toString() ;
        if(emailFlag==1 && !emailText.equals("")){
            if(emailText.charAt(0)=='-'){
                emailText = emailText.substring(1,emailText.length()) ;
            }
        }
        if(emailFlag==-1 && !emailText.equals("")){
            if(emailText.charAt(0)!='-'){
                emailText = "-" + emailText ;
            }
        }
        candidateTutorInfo.setEmailPK(emailText);

        areaAddressText = areaAddress.getText().toString() ;
        if(addressFlag==1 && !areaAddressText.equals("")){
            if(areaAddressText.charAt(0)=='-'){
                areaAddressText = areaAddressText.substring(1,areaAddressText.length()) ;
            }
        }
        if(addressFlag==-1 && !areaAddressText.equals("")){
            if(areaAddressText.charAt(0)!='-'){
                areaAddressText = "-" + areaAddressText ;
            }
        }
        candidateTutorInfo.setAreaAddress(areaAddressText);

        subjectText = subject.getText().toString() ;
        if(subjectFlag==1 && !subjectText.equals("")){
            if(subjectText.charAt(0)=='-'){
                subjectText = subjectText.substring(1,subjectText.length()) ;
            }
        }
        if(subjectFlag==-1 && !subjectText.equals("")){
            if(subjectText.charAt(0)!='-'){
                subjectText = "-" + subjectText ;
            }
        }
        candidateTutorInfo.setEdu_tutorSubject(subjectText);


        mediumText = mediumInvisible.getSelectedItem().toString() ;
        if(mediumFlag==1 && !mediumText.equals("")){
            if(mediumText.charAt(0)=='-'){
                mediumText = mediumText.substring(1,mediumText.length()) ;
            }
        }
        if(mediumFlag==-1  && !mediumText.equals("")){
            if(mediumText.charAt(0)!='-'){
                mediumText = "-" + mediumText ;
            }
        }
        verifiedTutorInfo.setPreferredMediumOrVersion(mediumText);

        classesText = preferredClass.getText().toString() ;
        if(classFlag==1 && !classesText.equals("")){
            if(classesText.charAt(0)=='-'){
                classesText = classesText.substring(1,classesText.length()) ;
            }
        }
        if(classFlag==-1 && !classesText.equals("")){
            if(classesText.charAt(0)!='-'){
                classesText = "-" + classesText ;
            }
        }
        verifiedTutorInfo.setPreferredClasses(classesText);

        groupText = preferredGroupInvisible.getSelectedItem().toString() ;
        if(groupFlag==1 && !groupText.equals("")){
            if(groupText.charAt(0)=='-'){
                groupText = groupText.substring(1,groupText.length()) ;
            }
        }
        if(groupFlag==-1 && !groupText.equals("")){
            if(groupText.charAt(0)!='-'){
                groupText = "-" + groupText ;
            }
        }
        verifiedTutorInfo.setPreferredGroup(groupText);

        subjectListText = preferredSubject.getText().toString() ;
        if(subjectListFlag==1 && !subjectListText.equals("")){
            if(subjectListText.charAt(0)=='-'){
                subjectListText = subjectListText.substring(1,subjectListText.length()) ;
            }
        }
        if(subjectListFlag==-1 && !subjectListText.equals("")){
            if(subjectListText.charAt(0)!='-'){
                subjectListText = "-" + subjectListText ;
            }
        }
        verifiedTutorInfo.setPreferredSubjects(subjectListText);

        areaText = preferredLocation.getText().toString() ;
        if(areaFlag==1 && !areaText.equals("")){
            if(areaText.charAt(0)=='-'){
                areaText = areaText.substring(1,areaText.length()) ;
            }
        }
        if(areaFlag==-1 && !areaText.equals("")){
            if(areaText.charAt(0)!='-'){
                areaText = "-" + areaText ;
            }
        }
        verifiedTutorInfo.setPreferredAreas(areaText);

        daysPerWeekText = daysPerWeekOrMonth.getText().toString() ;
        if(daysPerWeekFlag==1 && !daysPerWeekText.equals("")){
            if(daysPerWeekText.charAt(0)=='-'){
                daysPerWeekText = daysPerWeekText.substring(1,daysPerWeekText.length()) ;
            }
        }
        if(daysPerWeekFlag==-1 && !daysPerWeekText.equals("")){
            if(daysPerWeekText.charAt(0)!='-'){
                daysPerWeekText = "-" + daysPerWeekText ;
            }
        }
        verifiedTutorInfo.setPreferredDaysPerWeekOrMonth(daysPerWeekText);

        salaryText = minimumSalary.getText().toString() ;
        if(salaryFlag==1 && !salaryText.equals("")){
            if(salaryText.charAt(0)=='-'){
                salaryText = salaryText.substring(1,salaryText.length()) ;
            }
        }
        if(salaryFlag==-1 && !salaryText.equals("")){
            if(salaryText.charAt(0)!='-'){
                salaryText = "-" + salaryText ;
            }
        }
        verifiedTutorInfo.setMinimumSalary(salaryText);

        verifiedTutorInfo.setExperienceStatus(experienceEditText.getText().toString());

        myRefCandidateTutorInfo.setValue(candidateTutorInfo) ;
        myRefVerifiedTutorInfo.setValue(verifiedTutorInfo) ;

        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("user",user) ;
        intent.putStringArrayListExtra("userInfo",userInfo) ;
        startActivity(intent);
        finish();
    }

    public void sendMessageRequestByGuardian(View view){
        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;
        messageBoxInfo = new MessageBoxInfo(firebaseUser.getPhoneNumber(),firebaseUser.getUid(),firebaseUser.getEmail(), tutorUid, false ,true) ;

        myRefMessageBox.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);
                    if(messageBoxInfo1.getGuardianUid().equals(firebaseUser.getUid())
                            && messageBoxInfo1.getTutorUid().equals(tutorUid)){
                        flag=1;
                    }
                }

                if(flag == 0){
                    myRefMessageBox.push().setValue(messageBoxInfo) ;
                    messageRequestButton.setEnabled(false);
                    messageRequestButton.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reportIDByGuardian(String reportString){
        ReportInfo reportInfo = new ReportInfo(firebaseUser.getPhoneNumber(), tutorUid, reportString) ;
        myRefReport.push().setValue(reportInfo) ;
    }

    public void approveAndBlockOperation(View view){

        if(approvedAndBlockButton.getText().toString().equals("APPROVE")){
            approveAndBlockInfo.setStatus("running");
        }
        else if(approvedAndBlockButton.getText().toString().equals("BLOCK")){
            approveAndBlockInfo.setStatus("blocked");
        }
        else if(approvedAndBlockButton.getText().toString().equals("UNBLOCK")){
            approveAndBlockInfo.setStatus("running");
        }
        myRefApproveAndBlockInfo.setValue(approveAndBlockInfo) ;
    }

    public void approveCandidateTutorAsAVerifiedTutor(View view){
        DatabaseReference myRefApproveInfo = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;
        Button approveButton = findViewById(R.id.approveButton) ;
        approveButton.setText("Approved");
        approveButton.setBackgroundColor(GRAY);
        approveButton.setEnabled(false);
        ApproveAndBlockInfo approveInfo = new ApproveAndBlockInfo("tuitionapsspl02@gmail.com", "approved") ;
        myRefApproveInfo.push().setValue(approveInfo) ;
    }

    public void goToBackPageActivity(View view){
        if(user.equals("tutor")){
            Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("guardian")){
            Intent intent = new Intent(this, ViewingSearchingTutorProfileActivity.class);
            intent.putExtra("user",user) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("admin")){
            Intent intent = new Intent(this, ViewingSearchingTutorProfileActivity.class);
            intent.putExtra("user",user) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("admin2") || user.equals("admin3")){
            Intent intent = new Intent(this, AdminTutorProfileViewActivity.class);
            if(user.equals("admin2")) intent.putExtra("flag","approveTutor") ;
            else if(user.equals("admin3")) intent.putExtra("flag","blockTutor") ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("groupAdmin")){
            Intent intent = new Intent(this, GroupHomePageActivity.class);
            intent.putExtra("user","tutor") ;
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            intent.putExtra("groupID", groupID) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("groupVisitor")){
            Intent intent = new Intent(this, GroupHomePageActivity.class);
            intent.putExtra("user","guardian") ;
            intent.putExtra("groupID", groupID) ;
            startActivity(intent);
            finish();
        }

    }
    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }

    public void onPopupButtonClick(View view) {
        final PopupMenu popup = new PopupMenu(this, view);
        if(user.equals("tutor")){
            popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
        }
        else if(user.equals("guardian")){
            popup.getMenuInflater().inflate(R.menu.tutor_profile_popup_for_guardian, popup.getMenu());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Edit Profile")){
                    editProfile() ;
                }

                if(!item.getTitle().equals("Edit Profile")&&!item.getTitle().equals("Report")){
                    reportIDByGuardian(item.getTitle().toString());
                    Toast.makeText(VerifiedTutorProfileActivity.this,
                            "Clicked popup menu item " + item.getTitle(),
                            Toast.LENGTH_SHORT).show() ;
                }

                popup.dismiss();
                return true ;
            }
        });

        popup.show();
    }
}
