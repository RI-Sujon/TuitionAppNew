package com.example.tuitionappv2.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionappv2.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionappv2.guardian.GuardianHomePageActivity;
import com.example.tuitionappv2.R;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorHomePageActivity;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    String userName, userProfilePicUri, userEmail, userUid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startModule(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String phoneNumber = user.getPhoneNumber() ;
            String email = user.getEmail() ;

            if(!email.equals("")){
                signUpCompletionChecking(user);
            }
            else if(!phoneNumber.equals("")){
                Intent intent = new Intent(this, GuardianHomePageActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            goToHomePageActivity();
        }
    }

    public void signUpCompletionChecking(FirebaseUser user){
        final DatabaseReference myRefVerifiedTutorInfo,myRefCandidateTutorInfo ;
        userUid = user.getUid() ;
        myRefVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(user.getUid());
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(user.getUid());


        myRefVerifiedTutorInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    VerifiedTutorInfo verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class) ;
                    userProfilePicUri = verifiedTutorInfo.getProfilePictureUri() ;
                    userEmail = verifiedTutorInfo.getEmailPK() ;

                    myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            CandidateTutorInfo candidateTutorInfo = dataSnapshot1.getValue(CandidateTutorInfo.class) ;
                            userName = candidateTutorInfo.getFirstName() + " " + candidateTutorInfo.getLastName() ;
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

    public void goToVerifiedTutorHomePageActivity(){
        ArrayList<String> userInfo = new ArrayList<>() ;
        userInfo.add(userName) ;
        userInfo.add(userProfilePicUri) ;
        userInfo.add(userEmail) ;
        userInfo.add(userUid) ;

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
}
