package com.example.tuitionapp_surji.verified_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.admin.AdminTutorProfileViewActivity;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.candidate_tutor.ReferInfo;
import com.example.tuitionapp_surji.demo_video.DemoVideoMainActivity;
import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.guardian.ViewingSearchingTutorProfileActivity;
import com.example.tuitionapp_surji.message_box.MainMessageActivity;
import com.example.tuitionapp_surji.message_box.MessageActivity;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.message_box.MessageRequestActivity;
import com.example.tuitionapp_surji.notification_pack.NotificationViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.graphics.Color.RED;

public class VerifiedTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefCandidateTutorInfo, myRefVerifiedTutorInfo, myRefMessageBox, myRefReport, myRefApproveAndBlockInfo, myRefRefer ;
    private FirebaseUser firebaseUser ;

    private CandidateTutorInfo candidateTutorInfo;
    private VerifiedTutorInfo verifiedTutorInfo;
    private ApproveAndBlockInfo approveAndBlockInfo ;
    private MessageBoxInfo messageBoxInfo;

    private String user, userEmail, groupID, tutorUid, contextType, tutorEmail , context2, tutorUid2;
    private ArrayList<ReportInfo> reportInfoArrayList ;
    private ArrayList<String> userInfo ;

    private EditText phoneNumber,email,gender,areaAddress,currentPosition,instituteName,subject ;
    private EditText medium,preferredClass,preferredGroup,preferredSubject,daysPerWeekOrMonth,preferredLocation,minimumSalary ;
    private Spinner preferredGroupInvisible, mediumInvisible ;
    private EditText experienceEditText ;
    private ImageView userProfilePicImageView ;
    private ImageView idCardImageView ;
    private TextView userNameTextView, status1, status2;
    private Button approvedAndBlockButton;
    private Button messageRequestButton, demoVideoButton ;
    private LinearLayout layoutForAdmin ;

    private ViewFlipper viewFlipper ;
    private ImageButton aboutButton, excellenceButton, experienceButton ;
    private LinearLayout mobileNumberRow, emailRow, addressRow, subjectRow , preferredMediumRow, preferredClassRow, preferredGroupRow, preferredSubjectRow, preferredAreaRow , preferredDaysPerWeekRow, preferredMinimumSalaryRow;

    private ImageButton privacyMobileNumber, privacyAddress, privacyEmail, privacySubject ;
    private ImageButton privacyMedium, privacyClass, privacyGroup, privacySubjectList, privacyArea, privacyDaysPerWeek, privacySalary ;

    private TextView availability ;
    private MaterialButton changeAvailabilityButton ;

    private FirebaseFirestore databaseFireStore = FirebaseFirestore.getInstance() ;
    private double rating = 0 ;
    private Menu menu ;
    private CustomAdapterForReportTutor adapter ;

    private TextView refer1, refer1Result, refer2, refer2Result ;

    private int mobileNumberFlag = 0, addressFlag = 0, emailFlag = 0, subjectFlag = 0;
    private int mediumFlag = 0, classFlag = 0, groupFlag = 0, subjectListFlag = 0, areaFlag = 0, daysPerWeekFlag = 0, salaryFlag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_profile);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;
        tutorUid = intent.getStringExtra("tutorUid");
        tutorEmail =  intent.getStringExtra("tutorEmail");

        userProfilePicImageView = findViewById(R.id.profilePicImageView) ;
        idCardImageView = findViewById(R.id.idCardImageView) ;
        userNameTextView = findViewById(R.id.userName) ;
        status1 = findViewById(R.id.status1) ;
        status2 = findViewById(R.id.status2) ;
        layoutForAdmin = findViewById(R.id.layoutForAdmin) ;

        myRefCandidateTutorInfo= FirebaseDatabase.getInstance().getReference("CandidateTutor");
        myRefVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            userEmail = userInfo.get(2);
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(userInfo.get(3)) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(userInfo.get(3)) ;
        }
        else if(user.equals("groupAdmin")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            groupID = intent.getStringExtra("groupID") ;
            userEmail = intent.getStringExtra("userEmail") ;
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
        }
        else if(user.equals("groupVisitor")){
            groupID = intent.getStringExtra("groupID") ;
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
        }
        else if(user.equals("referFriend")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
        }
        else if(user.equals("admin") || user.equals("admin2") || user.equals("admin3")){
            myRefReport = FirebaseDatabase.getInstance().getReference("Report").child(tutorUid);
            myRefRefer = FirebaseDatabase.getInstance().getReference("Refer").child(tutorUid);
            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
            userEmail = intent.getStringExtra("userEmail") ;
            reportInfoArrayList = new ArrayList<>() ;
            layoutForAdmin.setVisibility(View.VISIBLE);

            refer1 = findViewById(R.id.reference1) ;
            refer1Result = findViewById(R.id.reference1A) ;
            refer2 = findViewById(R.id.reference2) ;
            refer2Result = findViewById(R.id.reference2A) ;

            myRefReport.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1:dataSnapshot.getChildren()){
                        ReportInfo reportInfo = dS1.getValue(ReportInfo.class) ;
                        reportInfoArrayList.add(reportInfo) ;
                    }
                    adapter = new CustomAdapterForReportTutor(VerifiedTutorProfileActivity.this,reportInfoArrayList) ;
                    myRefReport.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            }) ;

            myRefRefer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int i = 0 ;
                    for (DataSnapshot dS1: snapshot.getChildren()){
                        ReferInfo referInfo = dS1.getValue(ReferInfo.class) ;
                        if(i==0){
                            refer1.setText(referInfo.getVerifiedTutorEmail());
                            refer1Result.setText(referInfo.getReferApprove());
                            i=1 ;
                        }
                        else if(i==1){
                            refer2.setText(referInfo.getVerifiedTutorEmail());
                            refer2Result.setText(referInfo.getReferApprove());
                            i=-1 ;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
            myRefReport = FirebaseDatabase.getInstance().getReference("Report").child(tutorUid);
            userEmail = intent.getStringExtra("userEmail") ;
            context2 = intent.getStringExtra("context2") ;
            contextType =  intent.getStringExtra("context");
            if(context2!=null){
                tutorUid2 = intent.getStringExtra("tutorUid2") ;
                groupID = intent.getStringExtra("groupID") ;
            }else {
                messageRequestButton = findViewById(R.id.messageRequestButton) ;
                messageRequestButton.setVisibility(View.VISIBLE);
                demoVideoButton = findViewById(R.id.demo_video_button);
                demoVideoButton.setVisibility(View.VISIBLE);

                databaseFireStore.collection("Rating").document(tutorUid)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult() ;
                        if(document.get(firebaseUser.getUid())!=null){
                            rating = (double) document.get(firebaseUser.getUid()) ;
                        }
                    }
                }) ;
            }

            myRefCandidateTutorInfo = myRefCandidateTutorInfo.child(tutorUid) ;
            myRefVerifiedTutorInfo = myRefVerifiedTutorInfo.child(tutorUid) ;
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

                    viewAndChangeProfilePicture();
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

        availability = findViewById(R.id.availability) ;
        changeAvailabilityButton = findViewById(R.id.change_availability_button) ;

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

        if(user.equals("groupVisitor") || user.equals("groupAdmin") ){
            excellenceButton.setVisibility(View.GONE);
            experienceButton.setVisibility(View.GONE);
        }
        else if(user.equals("guardian") && context2!=null){
            excellenceButton.setVisibility(View.GONE);
            experienceButton.setVisibility(View.GONE);
        }

        if(user.equals("tutor")){
            availability.setVisibility(View.VISIBLE);
            changeAvailabilityButton.setVisibility(View.VISIBLE);
        }
    }

    public void goToTutorDemoVideo(View view) {

        Intent intent = new Intent(this, DemoVideoMainActivity.class);
        intent.putExtra("userEmail", userEmail) ;
        intent.putExtra("tutorEmail", tutorEmail) ;
        intent.putExtra("tutorUid",tutorUid);
        intent.putExtra("user", "guardian") ;
        startActivity(intent);
        finish();
    }

    public void viewAndChangeProfilePicture(){
        userProfilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifiedTutorProfileActivity.this, VerifiedTutorSetProfilePicture.class);

                intent.putExtra("intentFlag","profile") ;
                intent.putExtra("user", user) ;
                intent.putExtra("profilePicUri",candidateTutorInfo.getProfilePictureUri()) ;

                if(user.equals("tutor")){
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    startActivity(intent);
                    finish();
                }else {
                    startActivity(intent);
                }

            }
        });
    }

    public void addCandidateTutorInfoToProfile(){
        if(candidateTutorInfo.getProfilePictureUri()==null) {
            if(firebaseUser.getPhotoUrl()!=null){
                Picasso.get().load(firebaseUser.getPhotoUrl()).into(userProfilePicImageView);
            }
            else if (candidateTutorInfo.getGender().equals("FEMALE")) {
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

        if(user.equals("admin")||user.equals("admin2")||user.equals("admin3")){
            Picasso.get().load(candidateTutorInfo.getIdCardImageUri()).into(idCardImageView) ;
        }

        final boolean available = candidateTutorInfo.isTutorAvailable();
        if(available){
            availability.setText("You Are Available. ");
            availability.setTextColor(Color.rgb(35,168,41));
            changeAvailabilityButton.setText("Click to set \"Not Available\"");
        }
        else {
            availability.setText("You Are Not Available. ");
            availability.setTextColor(RED);
            changeAvailabilityButton.setText("Click to set \"Available\"");
        }

        changeAvailabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available){
                    candidateTutorInfo.setTutorAvailable(false);
                    myRefCandidateTutorInfo.setValue(candidateTutorInfo) ;
                    Intent intent = new Intent(VerifiedTutorProfileActivity.this, VerifiedTutorProfileActivity.class);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.putExtra("user", "tutor") ;
                    startActivity(intent);
                    finish();
                }
                else{
                    candidateTutorInfo.setTutorAvailable(true);
                    myRefCandidateTutorInfo.setValue(candidateTutorInfo) ;
                    Intent intent = new Intent(VerifiedTutorProfileActivity.this, VerifiedTutorProfileActivity.class);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.putExtra("user", "tutor") ;
                    startActivity(intent);
                    finish();
                }
            }
        });
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

    public void editProfile(){
        Button saveButton = findViewById(R.id.save_profile_button) ;
        saveButton.setVisibility(View.VISIBLE);

        availability.setVisibility(View.GONE);
        changeAvailabilityButton.setVisibility(View.GONE);

        phoneNumber.setEnabled(true);
        areaAddress.setEnabled(true);
        currentPosition.setEnabled(true);

        if(medium.getText().toString().equals("Bangla Medium")){
            mediumInvisible.setSelection(1);
        }
        else if(medium.getText().toString().equals("English Medium")){
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
        System.out.println("User Email === "+userEmail);
        messageBoxInfo = new MessageBoxInfo(firebaseUser.getPhoneNumber(),firebaseUser.getUid(),userEmail, tutorUid, true ,false,false,false,false) ;

        messageRequestButton.setBackgroundColor(Color.GREEN);
        messageRequestButton.setText("REQUEST SENT");

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
                    messageRequestButton.setBackgroundColor(Color.GREEN);
                    messageRequestButton.setText("REQUEST SENT");
                }
                else {
                    messageRequestButton.setBackgroundColor(Color.GREEN);
                    messageRequestButton.setText("ALREADY SENT");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reportIDByGuardian(String reportString){
        ReportInfo reportInfo = new ReportInfo(firebaseUser.getPhoneNumber(), reportString) ;
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

    public void goToBackPageActivity(View view){
        if(user.equals("tutor")){
            Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("guardian")){
            Intent intent ;
            if(context2!=null){
                intent = new Intent(this, GroupHomePageActivity.class) ;
                intent.putExtra("user",user) ;
                intent.putExtra("groupID", groupID) ;
                intent.putExtra("viewType", "tutorView") ;

                intent.putExtra("userEmail", userEmail) ;
                intent.putExtra("tutorUid", tutorUid2) ;
                intent.putExtra("context",contextType) ;

                startActivity(intent) ;
                finish() ;
            }
            else if(contextType!=null){
                if(contextType.equals("messenger")){
                    intent = new Intent(this, MainMessageActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent) ;
                    finish() ;
                }
                else if(contextType.equals("homepage")){
                    finish();
                }
                else {
                    intent= new Intent(this, ViewingSearchingTutorProfileActivity.class);
                    intent.putExtra("user",user) ;
                    startActivity(intent) ;
                    finish() ;
                }
            }
            else {
                finish() ;
            }
        }
        else if(user.equals("admin")){
            Intent intent = new Intent(this, ViewingSearchingTutorProfileActivity.class);
            intent.putExtra("user",user) ;
            startActivity(intent) ;
            finish() ;
        }
        else if(user.equals("admin2") || user.equals("admin3")){
            Intent intent = new Intent(this, AdminTutorProfileViewActivity.class) ;
            if(user.equals("admin2")) intent.putExtra("flag","approveTutor") ;
            else if(user.equals("admin3")) intent.putExtra("flag","blockTutor") ;
            startActivity(intent) ;
            finish() ;
        }
        else if(user.equals("groupAdmin")){
            Intent intent = new Intent(this, GroupHomePageActivity.class) ;
            intent.putExtra("user","tutor") ;
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            intent.putExtra("groupID", groupID) ;
            intent.putExtra("viewType", "tutorView") ;
            startActivity(intent) ;
            finish() ;
        }
        else if(user.equals("groupVisitor")){
            Intent intent = new Intent(this, GroupHomePageActivity.class) ;
            intent.putExtra("user","groupVisitor") ;
            intent.putExtra("groupID", groupID) ;
            intent.putExtra("viewType", "tutorView") ;
            startActivity(intent) ;
            finish() ;
        }
        else if(user.equals("referFriend")){
            finish() ;
        }
    }
    @Override
    public void onBackPressed(){
        goToBackPageActivity(null) ;
    }

    public void onPopupButtonClick(View view) {
        final PopupMenu popup = new PopupMenu(this, view) ;
        if(user.equals("tutor")||user.equals("admin")||user.equals("admin2")||user.equals("admin3")){
            popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu()) ;
        }
        else if(user.equals("guardian")){
            popup.getMenuInflater().inflate(R.menu.tutor_profile_popup_for_guardian, popup.getMenu()) ;
        }
        else if(user.equals("groupAdmin")){
            popup.getMenuInflater().inflate(R.menu.top_app_bar_group_homepage, popup.getMenu()) ;
        }

        menu = popup.getMenu() ;

        if(user.equals("admin")||user.equals("admin2")||user.equals("admin3")){
            menu.removeItem(R.id.edit);
        }
        else if(user.equals("tutor")){
            menu.removeItem(R.id.reportList);
        }
        else if(user.equals("groupAdmin")){
            menu.removeItem(R.id.leave);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Remove From Group")){
                    removeTutorFromGroupByGroupAdmin() ;
                }
                else if(item.getTitle().equals("Edit Profile")){
                    editProfile() ;
                }
                else if(item.getTitle().equals("Rating")){
                    ratingBarOperation(null);
                }
                else if(item.getTitle().equals("Report List")){
                    reportListDialogBoxView();
                }
                else if(!item.getTitle().equals("Report")){
                    reportIDByGuardian(item.getTitle().toString());
                }

                popup.dismiss() ;
                return true ;
            }
        });

        popup.show();
    }

    public void ratingBarOperation(View view){
        if(user.equals("guardian")){
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_adapter_for_dialog_box_tutor_rating);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
            TextView name = (TextView) dialog.findViewById(R.id.name);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.image_view);
            Button submitButton = (Button) dialog.findViewById(R.id.submit) ;

            if(rating!=0){
                ratingBar.setRating((float) rating);
            }

            name.setText(candidateTutorInfo.getUserName());
            if(candidateTutorInfo.getProfilePictureUri()!=null){
                Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(imageView);
            }

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Map<String,Object> map = new HashMap<>() ;
                    map.put(firebaseUser.getUid(), ratingBar.getRating()) ;

                    databaseFireStore.collection("Rating").document(tutorUid).set(map, SetOptions.merge()) ;
                    rating = ratingBar.getRating() ;
                }
            });

            dialog.show();
        }
    }

    public void reportListDialogBoxView(){
        if(user.equals("admin")||user.equals("admin2")||user.equals("admin3")){
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_adapter_for_dialog_box_report_list);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ListView listView2 = dialog.findViewById(R.id.reportList2);
            listView2.setAdapter(adapter);

            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public void removeTutorFromGroupByGroupAdmin(){
        FirebaseDatabase.getInstance().getReference("AddTutor").child(groupID).child(firebaseUser.getUid()).removeValue() ;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notification").child("Tutor").child(tutorUid) ;

        databaseReference.orderByChild("message3").equalTo(groupID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dS1: snapshot.getChildren()){
                    databaseReference.child(dS1.getKey()).removeValue() ;
                    databaseReference.removeEventListener(this);
                }

                goToBackPageActivity(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
