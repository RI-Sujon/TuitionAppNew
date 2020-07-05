package com.example.tuitionapp_surji.system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.guardian.GuardianInformationActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String tutorName, tutorProfilePicUri, tutorEmail, tutorUid, tutorGender;
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startModule();
    }

    public void startModule() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String phoneNumber = user.getPhoneNumber() ;
            String email = user.getEmail() ;

            if(!email.equals("")){
                signUpCompletionChecking(user);
            }
            else if(!phoneNumber.equals("")){
                goToNextPage();
            }

            else {
                goToHomePageActivity();
            }
        }
        else {
            goToHomePageActivity();
        }

    }

    public void signUpCompletionChecking(FirebaseUser user){
        final DatabaseReference myRefVerifiedTutorInfo, myRefCandidateTutorInfo, myRefApproveAndBlockInfo ;
        tutorUid = user.getUid() ;
        myRefVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(user.getUid());
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(user.getUid());
        myRefApproveAndBlockInfo = FirebaseDatabase.getInstance().getReference("ApproveAndBlock").child(user.getUid());

        myRefApproveAndBlockInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ApproveAndBlockInfo approveAndBlockInfo = dataSnapshot.getValue(ApproveAndBlockInfo.class) ;
                    if(approveAndBlockInfo.getStatus().equals("blocked")){

                    }
                    else{
                        myRefVerifiedTutorInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot1) {
                                            CandidateTutorInfo candidateTutorInfo = dataSnapshot1.getValue(CandidateTutorInfo.class) ;
                                            tutorEmail = candidateTutorInfo.getEmailPK() ;
                                            tutorName = candidateTutorInfo.getUserName()  ;
                                            tutorGender = candidateTutorInfo.getGender()  ;
                                            tutorProfilePicUri = candidateTutorInfo.getProfilePictureUri() ;
                                            goToVerifiedTutorHomePageActivity();
                                            myRefCandidateTutorInfo.removeEventListener(this);
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else{
                                    goToHomePageActivity();
                                }
                                myRefVerifiedTutorInfo.removeEventListener(this);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else{
                    goToHomePageActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    public void goToVerifiedTutorHomePageActivity(){
        ArrayList<String> userInfo = new ArrayList<>() ;
        userInfo.add(tutorName) ;
        if(tutorProfilePicUri==null){
            if(user.getPhotoUrl()==null){
                userInfo.add("") ;
            }
            else {
                userInfo.add(user.getPhotoUrl().toString()) ;
            }
        }else{
            userInfo.add(tutorProfilePicUri) ;
        }

        if(tutorEmail.charAt(0)=='-'){
            tutorEmail = tutorEmail.substring(1, tutorEmail.length()) ;
        }

        userInfo.add(tutorEmail) ;
        userInfo.add(tutorUid) ;
        userInfo.add(tutorGender) ;

        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;

        startActivity(intent);
        finish();
    }

    public void goToHomePageActivity(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToNextPage(){
        final DatabaseReference myRefGuardian ;

        myRefGuardian = FirebaseDatabase.getInstance().getReference("Guardian").child(user.getUid()) ;

        myRefGuardian.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    goToGuardianHomePageActivity();
                }
                else {
                    goToGuardianInformationActivity();
                }
                myRefGuardian.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    public void goToGuardianHomePageActivity(){
        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToGuardianInformationActivity(){
        Intent intent = new Intent(this, GuardianInformationActivity.class);
        startActivity(intent);
        finish();
    }
}
