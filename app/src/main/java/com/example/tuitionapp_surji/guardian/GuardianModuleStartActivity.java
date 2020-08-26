package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.starting.HomePageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class GuardianModuleStartActivity extends AppCompatActivity {
    private DatabaseReference myRefGuardian ;

    private EditText phoneNumberBox ;
    private TextView phoneNumberTextView ;
    private RelativeLayout mobileNumberLayout, verificationCodeLayout ;

    private FirebaseAuth mAuth;
    private PinView pinView ;

    private String phoneNumber , verificationCode;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_module_start);

        mAuth = FirebaseAuth.getInstance() ;

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
                String code = credential.getSmsCode() ;
                if(code!=null){
                    //verificationCodeBox.setText(code);
                    setVerificationCodeIntoBox(code);
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

                mobileNumberLayout.setVisibility(View.GONE);
                verificationCodeLayout.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumberBox = findViewById(R.id.guardianMobileNumber) ;
        phoneNumberTextView = findViewById(R.id.mobileNumberTextView2) ;
        pinView = findViewById(R.id.firstPinView) ;
        mobileNumberLayout = findViewById(R.id.mobileNumberLayout) ;
        verificationCodeLayout = findViewById(R.id.verificationCodeLayout) ;

        
    }

    public void sendVerificationCodeToPhoneNumber(View view){
        phoneNumber = phoneNumberBox.getText().toString().trim() ;

        if(phoneNumber.length()==10 && phoneNumber.charAt(0)=='1')
        {
            phoneNumber = "+880" + phoneNumber ;
        }
        else if(phoneNumber.length()==11 && phoneNumber.charAt(0)=='0')
        {
            phoneNumber = "+88" + phoneNumber ;
        }
        else{
            phoneNumberBox.setError("");
            return;
        }

        phoneNumberTextView.setText(phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);
        mVerificationInProgress = true;
    }

    public void setVerificationCodeIntoBox(String code){
        /*for(int i=0; i<code.length();i++ ){
            vc[i].setText(code.charAt(i)+"");
        }*/
        pinView.setText(code);
    }


    public void signUpCompletion(View view){
        verificationCode = "" ;

        /*for(int i=0; i<6; i++){
            if(vc[i].equals("")){
                vc[i].setError("");
                vc[i].requestFocus();
                return;
            }
            verificationCode = verificationCode + vc[i].getText().toString() ;
        }*/
        verificationCode = pinView.getText().toString() ;

        if(verificationCode.length()==6) {
            verifyCode(verificationCode);
        }
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

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
                            goToNextPage();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Sorry, Verification code is not matched",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void goToNextPage(){
        myRefGuardian = FirebaseDatabase.getInstance().getReference("Guardian").child(mAuth.getCurrentUser().getUid()) ;

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

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
