package com.example.tuitionapp_sujon.candidate_tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp_sujon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TutorSignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText, nameEditText , phoneNumberEditText ;
    private RelativeLayout signUpLayout, verificationLayout ;
    private TextView emailTextView2 ;

    private ProgressBar progressBar ;
    private String nameString, phoneNumberString, emailString, newPasswordString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up);

        mAuth = FirebaseAuth.getInstance() ;
        emailEditText = findViewById(R.id.email_edit_text) ;
        passwordEditText = findViewById(R.id.password_edit_text);
        nameEditText = findViewById(R.id.name_edit_text);
        phoneNumberEditText = findViewById(R.id.mobile_number_edit_text) ;
        signUpLayout = findViewById(R.id.sign_up_layout);
        verificationLayout = findViewById(R.id.verificationLinkLayout) ;
        emailTextView2 = findViewById(R.id.emailTextView2) ;

    }

    public void signUpCompletion(View view){

        //progressBar = findViewById(R.id.progressBar) ;
        //progressBar.setVisibility(View.VISIBLE);

        nameString = nameEditText.getText().toString().trim() ;
        phoneNumberString = phoneNumberEditText.getText().toString().trim() ;
        emailString = emailEditText.getText().toString().trim() ;
        newPasswordString = passwordEditText.getText().toString().trim() ;

        if(nameString.equals("")){
            nameEditText.setError("");
            return ;
        }
        else if(nameString.length()>25){
            nameEditText.setError("");
            return;
        }
        if(nameString.equals("")){
            nameEditText.setError("");
            return ;
        }
        else if(nameString.length()>25){
            nameEditText.setError("");
            return;
        }

        if(phoneNumberString.length()<11){
            phoneNumberEditText.setError("");
            return ;
        }
        else if(phoneNumberString.length()>14){
            phoneNumberEditText.setError("");
            return;
        }

        if(newPasswordString.length() >= 6 && newPasswordString.length() <= 15){

            mAuth.createUserWithEmailAndPassword(emailString, newPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User already exist.",  Toast.LENGTH_SHORT).show();
                    }
                    else if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Verification Sujon Done.", Toast.LENGTH_SHORT).show();
                                    waitForClickVerificationLink(mAuth.getCurrentUser());
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "wrong in send message.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }) ;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "wrong in ", Toast.LENGTH_SHORT).show();
                    }
                }
            }) ;
        }
        else {
            Toast.makeText(getApplicationContext(),"password length is less than 6 character",Toast.LENGTH_SHORT).show();
        }
    }

    public void waitForClickVerificationLink(FirebaseUser user){
        signUpLayout.setVisibility(View.GONE);
        verificationLayout.setVisibility(View.VISIBLE);
        emailTextView2.setText(user.getEmail());
        //while(!user.isEmailVerified()){
          //  mAuth.getCurrentUser().reload();
        //}
        //goToTutorSignUpActivityStep1();
    }

    public void verificationCompletion(View view){
        mAuth.getCurrentUser().reload() ;

        if( mAuth.getCurrentUser() !=null && mAuth.getCurrentUser().isEmailVerified()){
            goToTutorSignUpActivityStep1();
        }

    }

    public void createCandidateTutorDatabase(){
        DatabaseReference myRefDatabase = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(mAuth.getCurrentUser().getUid()) ;
        CandidateTutorInfo candidateTutorInfo = new CandidateTutorInfo(nameString,emailString,phoneNumberString) ;
        myRefDatabase.setValue(candidateTutorInfo) ;
        goToTutorSignUpActivityStep1();
    }

    public void goToTutorSignUpActivityStep1(){
        Intent intent = new Intent(this,TutorSignUpActivityStep1.class);
        intent.putExtra("type","signUp") ;
        intent.putExtra("tutorName",nameString);
        intent.putExtra("tutorMobileNumber",phoneNumberString);
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, TutorSignInActivity.class);
        startActivity(intent);
        finish();
    }
}