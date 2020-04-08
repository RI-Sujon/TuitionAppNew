package com.example.tuitionapp.CandidateTutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpT_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, newPassword, confirmPassword ;

    private ProgressBar progressBar ;
    private String emailString, newPasswordString, confirmPasswordString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_t);
        this.setTitle("SIGN UP");
        mAuth = FirebaseAuth.getInstance() ;
        email = findViewById(R.id.email) ;
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
    }

    public void signUpCompletion(View view){

        progressBar = findViewById(R.id.progressBar) ;
        progressBar.setVisibility(View.VISIBLE);

        emailString = email.getText().toString().trim() ;
        newPasswordString = newPassword.getText().toString().trim() ;
        confirmPasswordString = confirmPassword.getText().toString().trim() ;


        if(newPassword.length() >= 6){
            if(newPasswordString.equals(confirmPasswordString)){
                mAuth.createUserWithEmailAndPassword(emailString,confirmPasswordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
                            goToTutorSignUpActivityStep1();

                        } else {
                            Toast.makeText(getApplicationContext(),"something wrong, try again" ,Toast.LENGTH_SHORT).show() ;
                        }
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(),"Password is not matching",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"password length is less than 6 character",Toast.LENGTH_SHORT).show();
        }

    }

    public void goToTutorSignUpActivityStep1(){
        Intent intent = new Intent(this,TutorSignUpActivityStep1.class);
        startActivity(intent);
        finish();
    }

    public void backFromSignUpT_Activity(View view){
        Intent intent = new Intent(this, TutorModuleStartActivity.class);
        startActivity(intent);
        finish();
    }

}

