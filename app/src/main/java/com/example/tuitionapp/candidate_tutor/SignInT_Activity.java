package com.example.tuitionapp.candidate_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuitionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInT_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,password ;
    private ProgressBar progressBar ;
    private String emailString,passwordString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_t);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signInCompletion(View view){

        email = findViewById(R.id.email) ;
        password = findViewById(R.id.password) ;

        progressBar = findViewById(R.id.progressBar) ;
        progressBar.setVisibility(View.VISIBLE);

        emailString = email.getText().toString().trim() ;
        passwordString = password.getText().toString().trim() ;

        mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"sign in successfully",Toast.LENGTH_SHORT).show();
                    goToProfile() ;

                } else {
                    Toast.makeText(getApplicationContext(),"something wrong, try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToProfile(){

        Intent intent = new Intent(this, CandidateTutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
