package com.example.tuitionapp.VerifiedTutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tuitionapp.Admin.AdminHomePageActivity;
import com.example.tuitionapp.Admin.BlockInfo;
import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;
import com.example.tuitionapp.Group.GroupTutorViewActivity;
import com.example.tuitionapp.Guardian.ViewingSearchingTutorProfileActivity;
import com.example.tuitionapp.MessageBox.MessageBoxInfo;
import com.example.tuitionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VerifiedTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefAccountInfo, myRefTuitionInfo, myRefMessageBox, myRefReport ;
    private TextView phoneNumber,gender,areaAddress,currentPosition,instituteName,subject ;
    private TextView medium,preferredClass,preferredGroup,preferredSubject,daysPerWeekOrMonth,minimumSalary ;
    private ProgressBar progressBar ;
    private FirebaseUser firebaseUser ;

    private String userEmail,user ,groupID;

    private ArrayList<String> userInfo ;
    private ImageView userProfilePicImageView ;
    private TextView userNameTextView, userEmailTextView;
    private Button messageRequestButton, reportButton , blockButton;

    private CandidateTutorInfo candidateTutorInfo;
    private VerifiedTutorInfo verifiedTutorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_profile);
        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        userProfilePicImageView = findViewById(R.id.profilePicImageView) ;
        userNameTextView = findViewById(R.id.userName) ;
        userEmailTextView  = findViewById(R.id.userEmail) ;

        if(user.equals("tutor")){
            System.out.println(userInfo.toString());
            userEmail = userInfo.get(2);
            userNameTextView.setText(userInfo.get(0));
            Picasso.get().load(userInfo.get(1)).into(userProfilePicImageView) ;
            userEmailTextView.setText(userInfo.get(2));
        }
        else if(user.equals("group")){
            userEmail = intent.getStringExtra("groupTutorEmail") ;
            groupID = intent.getStringExtra("groupID") ;
        }
        else {
            userEmail = intent.getStringExtra("userEmail") ;
        }

        myRefAccountInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        myRefTuitionInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(user.equals("guardian")){
            messageRequestButton = findViewById(R.id.sendMessageRequestButton) ;
            reportButton = findViewById(R.id.reportButton) ;
            messageRequestButton.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.VISIBLE);
        }
        else if(user.equals("admin")){
            blockButton = findViewById(R.id.blockButton) ;
            blockButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar = findViewById(R.id.progressBar) ;
        progressBar.setVisibility(View.VISIBLE);

        phoneNumber = findViewById(R.id.phoneNumber) ;
        gender = findViewById(R.id.gender) ;
        areaAddress = findViewById(R.id.areaAddress) ;
        currentPosition = findViewById(R.id.currentPosition) ;
        instituteName = findViewById(R.id.instituteName) ;
        subject = findViewById(R.id.subject) ;

        medium = findViewById(R.id.medium) ;
        preferredClass = findViewById(R.id.preferredClass) ;
        preferredGroup = findViewById(R.id.preferredGroup) ;
        preferredSubject = findViewById(R.id.preferredSubject) ;
        daysPerWeekOrMonth = findViewById(R.id.daysPerWeekOrMonth) ;
        minimumSalary = findViewById(R.id.salary) ;

        myRefAccountInfo.orderByChild("emailPK").equalTo(userEmail)
                .addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                        addAccountInfoToProfile();
                   }
                   @Override
                   public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                   @Override
                   public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) { }
                });



        myRefTuitionInfo.orderByChild("emailPK").equalTo(userEmail)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class) ;
                        addTuitionInfoToProfile();
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

    }

    public void addAccountInfoToProfile(){
        progressBar.setVisibility(View.GONE);

        phoneNumber.setText("Contact No  :   " + candidateTutorInfo.getMobileNumber() );
        gender.setText("Gender  :   " + candidateTutorInfo.getGender() );
        areaAddress.setText("Area Address  :   " + candidateTutorInfo.getAreaAddress() );
        currentPosition.setText("Current Position  :   " + candidateTutorInfo.getCurrentPosition() );
        instituteName.setText("Institute Name  :   " + candidateTutorInfo.getEdu_instituteName());
        subject.setText("Subject/Department  :   " + candidateTutorInfo.getEdu_tutorSubject());
    }

    public void addTuitionInfoToProfile(){
        System.out.println(firebaseUser.getUid()+" " + firebaseUser.getIdToken(true) + " " + firebaseUser.getProviderId() );
        medium.setText("Medium  :   " + verifiedTutorInfo.getPreferredMediumOrVersion() );
        String strClass = verifiedTutorInfo.getPreferredClasses() + "";
        if(strClass.length()>0) {
            strClass = strClass.replace("_"," || ") ;
            strClass = strClass.substring(0, strClass.length() - 4);
        }

        String strSub = verifiedTutorInfo.getPreferredSubjects() + "" ;
        if(strSub.length()>0) {
            strSub = strSub.replace("_", " || ");
            strSub = strSub.substring(0, strSub.length() - 4);
        }
        preferredClass.setText("Preferred Class  :   " +  strClass);
        preferredGroup.setText("Preferred Group  :   " + verifiedTutorInfo.getPreferredGroup() );
        preferredSubject.setText("Preferred Subject  :   " + strSub );
        daysPerWeekOrMonth.setText("Days Per Week  :   " + verifiedTutorInfo.getPreferredDaysPerWeekOrMonth() );
        minimumSalary.setText("Minimum Salary  :   " + verifiedTutorInfo.getMinimumSalary() );
    }

    public void sendMessageRequestByGuardian(View view){
        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;
        MessageBoxInfo messageBoxInfo = new MessageBoxInfo(firebaseUser.getPhoneNumber(),userEmail, false ,true) ;
        myRefMessageBox.push().setValue(messageBoxInfo) ;

        messageRequestButton.setEnabled(false);
        messageRequestButton.setBackgroundColor(Color.GRAY);

    }

    public void reportIDByGuardian(View view){
        myRefReport = FirebaseDatabase.getInstance().getReference("Report") ;
        ReportInfo reportInfo = new ReportInfo(firebaseUser.getPhoneNumber(),userEmail, "this is a fake account") ;
        myRefReport.push().setValue(reportInfo) ;

        reportButton.setEnabled(false);
        reportButton.setBackgroundColor(Color.GRAY);
    }

    public void blockVerifiedTutorByAdmin(View view){
        DatabaseReference myRefBlockInfo = FirebaseDatabase.getInstance().getReference("Block") ;
        BlockInfo blockInfo = new BlockInfo("tuitionApsspl02@gmail.com",userEmail,true) ;
        myRefBlockInfo.push().setValue(blockInfo) ;

        blockButton.setEnabled(false);
        blockButton.setBackgroundColor(Color.GRAY);
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
            Intent intent = new Intent(this, AdminHomePageActivity.class);
            intent.putExtra("user",user) ;
            startActivity(intent);
            finish();
        }
        else if(user.equals("group")){
            Intent intent = new Intent(this, GroupTutorViewActivity.class);
            intent.putExtra("user",user) ;
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            intent.putExtra("groupID", groupID) ;
            startActivity(intent);
            finish();
        }

    }
}
