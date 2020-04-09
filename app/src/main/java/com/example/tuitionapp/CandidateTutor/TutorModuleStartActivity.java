package com.example.tuitionapp.CandidateTutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuitionapp.Admin.AdminHomePageActivity;
import com.example.tuitionapp.Admin.ApproveInfo;
import com.example.tuitionapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TutorModuleStartActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient ;
    private static final int RC_SIGN_IN = 9003 ;
    private  FirebaseAuth mAuth ;
    private ProgressBar progressBar ;

    private DatabaseReference  myRefCandidateTutorInfo, myRefVerifiedTutorInfo, myRefApproveInfo;

    private int flagForHandling = -1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_module_start);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance() ;

        progressBar = findViewById(R.id.progressBar) ;

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            isApprovedChecking(currentUser);
        }
    }

    public void goToTutorSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpT_Activity.class);
        startActivity(intent);
        finish();

    }

    public void goToTutorSignInActivity(View view) {
        Intent intent = new Intent(this, SignInT_Activity.class);
        startActivity(intent);
        finish();
    }

    public void goToSignInWithGoogle(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                    firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"something wrong google sign in, try again" ,Toast.LENGTH_SHORT).show() ;
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    //signUpComletionChecking1(user);
                    isApprovedChecking(user);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
                });
    }

    public void updateUI(FirebaseUser user){
        if(user!=null){
            Intent intent = new Intent(this, TutorSignUpActivityStep1.class);
            startActivity(intent);
            finish();
        }
    }


    public void isApprovedChecking(final FirebaseUser user){
        if(user.getEmail().equals("tuitionapsspl02@gmail.com")){
            goToAdminPanel() ;
            return;
        }
        final int[] flag = {0};
        if (user !=null){
            myRefApproveInfo = FirebaseDatabase.getInstance().getReference("Approve");
            myRefApproveInfo.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1: dataSnapshot.getChildren()){
                        ApproveInfo approveInfo = dS1.getValue(ApproveInfo.class) ;
                        if(approveInfo.getCandidateTutorEmail().equals(user.getEmail())){
                            System.out.println("Marhaba");
                            goToCandidateTutorHomePageActivity();
                            flag[0] = 1 ;
                            myRefApproveInfo.removeEventListener(this);
                        }
                    }
                    if(flag[0]==0){
                        isSignUpStep1CompletedChecking(user);
                        myRefApproveInfo.removeEventListener(this);
                    }

                    //goToTutorSignUpActivityStep1();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }
    }

    public void isSignUpStep1CompletedChecking(final FirebaseUser user){
        if (user !=null){
            myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(user.getUid());
            myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        CandidateTutorInfo candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                        if(candidateTutorInfo.getIdCardImageUri()!=null){
                            goToCandidateTutorHomePageActivity();
                        }
                        else {
                            goToTutorSignUpActivityStep2();
                        }

                        myRefCandidateTutorInfo.removeEventListener(this);
                    }else{
                        goToTutorSignUpActivityStep1();
                        myRefCandidateTutorInfo.removeEventListener(this);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public void goToCandidateTutorHomePageActivity(){
        System.out.println("Hello sir from tutor module start");
        Intent intent = new Intent(this, CandidateTutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToTutorSignUpActivityStep2(){
        Intent intent = new Intent(this, TutorSignUpActivityStep2.class);
        startActivity(intent);
        finish();
    }

    public void goToTutorSignUpActivityStep1(){
        Intent intent = new Intent(this, TutorSignUpActivityStep1.class);
        startActivity(intent);
        finish();
    }

    public void goToAdminPanel(){
        Intent intent = new Intent(this, AdminHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
