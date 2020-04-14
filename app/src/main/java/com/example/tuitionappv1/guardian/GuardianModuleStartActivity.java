package com.example.tuitionappv1.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionappv1.R;
import com.example.tuitionappv1.system.HomePageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GuardianModuleStartActivity extends AppCompatActivity {
    private EditText phoneNumberBox, verificationCodeBox ;
    private TextView verificationCodeBar ;
    private Button submitPhoneNumberButton, resendVerificationButton ;
    private ProgressBar progressBar ;
    private FirebaseAuth mAuth;

    String phoneNumber , verificationCode;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_module_start);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance() ;

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
                String code = credential.getSmsCode() ;
                if(code!=null){
                    verificationCodeBox.setText(code);
                    verifyCode(code);
                }

                if(code==null){
                    signInWithPhoneAuthCredential(credential);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(),"Invalid phone Number" , Toast.LENGTH_SHORT ).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getApplicationContext(),"something wrong" , Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                phoneNumberBox.setEnabled(false);
                phoneNumberBox.setBackgroundColor(Color.TRANSPARENT);
                submitPhoneNumberButton.setVisibility(View.INVISIBLE);
                resendVerificationButton.setVisibility(View.VISIBLE);
                verificationCodeBar.setVisibility(View.VISIBLE);
                verificationCodeBox.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumberBox = findViewById(R.id.phoneNumber) ;
        verificationCodeBox = findViewById(R.id.verificationCode) ;
        submitPhoneNumberButton = findViewById(R.id.submitPhoneNumberButton) ;
        resendVerificationButton = findViewById(R.id.resend) ;
        verificationCodeBar = findViewById(R.id.verificationCodeBar) ;
        progressBar = findViewById(R.id.progressBar) ;
    }

    public void sendVerificationCodeToPhoneNumber(View view){
        phoneNumber = phoneNumberBox.getText().toString().trim() ;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);
        mVerificationInProgress = true;
    }


    public void signUpCompletion(View view){
        verificationCode = verificationCodeBox.getText().toString() ;

        if(verificationCode.length()<6){
            verificationCodeBox.setError("Enter verificationCode");
            verificationCodeBox.requestFocus() ;
            return;
        }

        verifyCode(verificationCode);
    }

    public void resend(View view){
        resendVerificationCode(phoneNumber,mResendToken);
    }

    public void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    public void verifyCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode) ;
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"sign up problem",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void backFromSignUpT_Activity(View view){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        backFromSignUpT_Activity(null);
    }
}
