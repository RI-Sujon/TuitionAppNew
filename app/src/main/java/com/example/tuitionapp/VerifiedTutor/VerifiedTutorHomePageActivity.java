package com.example.tuitionapp.VerifiedTutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionapp.R;
import com.example.tuitionapp.HomePageActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class VerifiedTutorHomePageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private GoogleSignInClient mGoogleSignInClient ;
    private DatabaseReference myRefVerifiedTutorInfo , myRefApproveInfo;
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_home_page);
        mAuth = FirebaseAuth.getInstance() ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        user = mAuth.getCurrentUser() ;
    }

    public void goToVerifiedTutorNotificationActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorNotificationActivity.class) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorProfileActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorViewPostActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorViewPostActivity.class) ;
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
}
