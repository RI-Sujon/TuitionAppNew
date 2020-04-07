package com.example.tuitionapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionapp.R;
import com.example.tuitionapp.HomePageActivity;
import com.example.tuitionapp.TuitionPost.TuitionPostActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private GoogleSignInClient mGoogleSignInClient ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        setTitle("ADMIN PANEL");
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void goToCandidateTutorProfile(View view) {
        Intent intent = new Intent(this, TuitionPostActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToCandidateTutorProfileVeiwActivity(View view){
        Intent intent = new Intent(this, CandidateTutorProfileAdminViewActivity.class) ;
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
