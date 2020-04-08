package com.example.tuitionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionapp.Guardian.GuardianHomePageActivity;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorHomePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

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
                signUpComletionChecking(user);
            }
            else if(!phoneNumber.equals("")){
                Intent intent = new Intent(this, GuardianHomePageActivity.class);
                startActivity(intent);
                finish();
            }

            System.out.println("Phone Number:" + phoneNumber + "\nEmail:" + email);
        }
        else {
            goToHomePageActivity();
        }
    }

    public void signUpComletionChecking(FirebaseUser user){
        final DatabaseReference myVerifiedTutorInfo ;
        myVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(user.getUid());

        myVerifiedTutorInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    goToVerifiedTutorHomePageActivity();
                    myVerifiedTutorInfo.removeEventListener(this);
                }else{
                    goToHomePageActivity();
                    myVerifiedTutorInfo.removeEventListener(this);
                }
                myVerifiedTutorInfo.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goToVerifiedTutorHomePageActivity(){
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToHomePageActivity(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
