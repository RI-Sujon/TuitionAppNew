package com.example.tuitionappv2.candidate_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionappv2.admin.ApproveInfo;
import com.example.tuitionappv2.admin.BlockInfo;
import com.example.tuitionappv2.R;
import com.example.tuitionappv2.system.HomePageActivity;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorHomePageActivity;
import com.example.tuitionappv2.verified_tutor.TutorSignUpActivityStep3;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CandidateTutorHomePageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private GoogleSignInClient mGoogleSignInClient ;
    private DatabaseReference myRefVerifiedTutorInfo , myRefApproveInfo , myRefBlockInfo ,myRefCandidateTutorInfo;
    private FirebaseUser user ;
    String userName, userProfilePicUri, userEmail, userUid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_tutor_home_page);
//        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance() ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        user = mAuth.getCurrentUser() ;
        isApprovedAndRegisterWithVerifiedTutor(user);
    }

    public void goToCandidateTutorProfileActivity(View view){
        Intent intent = new Intent(this, CandidateTutorProfileActivity.class) ;
        intent.putExtra("userEmail",user.getEmail()) ;
        intent.putExtra("user", "user") ;
        startActivity(intent);
        finish();
    }


    public void signOut(View view) {
        mAuth.signOut();
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        Intent intent = new Intent(this, HomePageActivity.class) ;
        startActivity(intent);
        finish();
    }

    public void isApprovedAndRegisterWithVerifiedTutor(final FirebaseUser user){
        userUid = user.getUid() ;
        myRefVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(userUid);
        myRefApproveInfo = FirebaseDatabase.getInstance().getReference("Approve");
        myRefBlockInfo = FirebaseDatabase.getInstance().getReference("Block");
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(userUid);

        myRefVerifiedTutorInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    VerifiedTutorInfo verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class);
                    userProfilePicUri = verifiedTutorInfo.getProfilePictureUri() ;
                    userEmail = verifiedTutorInfo.getEmailPK() ;

                    myRefBlockInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            int flag = 0 ;
                            for(DataSnapshot dS1: dataSnapshot1.getChildren()){
                                BlockInfo blockInfo = dS1.getValue(BlockInfo.class) ;
                                if(blockInfo.getVerifiedTutorEmail().equals(user.getEmail())){
                                    flag = 1 ;
                                    break ;
                                }
                            }
                            if(flag==0){

                                myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot3) {
                                        CandidateTutorInfo candidateTutorInfo = dataSnapshot3.getValue(CandidateTutorInfo.class) ;
                                        userName = candidateTutorInfo.getFirstName() + " " + candidateTutorInfo.getLastName() ;
                                        goToVerifiedTutorHomePageActivity();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                    }
                                });
                            }

                            myRefBlockInfo.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });

                }
                else{

                    myRefApproveInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            for(DataSnapshot dS1: dataSnapshot2.getChildren()){
                                ApproveInfo approveInfo = dS1.getValue(ApproveInfo.class) ;
                                System.out.println(approveInfo.getCandidateTutorEmail()+"\t\t"+user.getEmail());
                                if(approveInfo.getCandidateTutorEmail().equals(user.getEmail())){
                                    goToTutorSignUpActivityStep3();
                                }
                            }
                            myRefApproveInfo.removeEventListener(this);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });

                    myRefVerifiedTutorInfo.removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goToTutorSignUpActivityStep3(){
        Intent intent = new Intent(this, TutorSignUpActivityStep3.class);
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorHomePageActivity(){
        ArrayList<String> userInfo = new ArrayList<>() ;
        userInfo.add(userName) ;
        userInfo.add(userProfilePicUri) ;
        userInfo.add(userEmail) ;
        userInfo.add(userUid) ;

        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        //intent.putExtra("userName", userName);
        //intent.putExtra("userProfilePicUri", userProfilePicUri);
        //intent.putExtra("userEmail", userEmail);
        //intent.putExtra("userUid", userUid);
        startActivity(intent);
        finish();
    }
}
