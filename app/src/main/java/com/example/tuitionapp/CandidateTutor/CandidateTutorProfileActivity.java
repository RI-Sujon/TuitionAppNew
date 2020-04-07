package com.example.tuitionapp.CandidateTutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;
import com.example.tuitionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CandidateTutorProfileActivity extends AppCompatActivity {

    private FirebaseDatabase database ;
    private DatabaseReference myRefCandidateTutorInfo ;
    private TextView name,email,phoneNumber,gender,areaAddress,currentPositon,instituteName,subject ;
    private ProgressBar progressBar ;
    private FirebaseUser firebaseUser ;

    private String userEmail ;

    private CandidateTutorInfo candidateTutorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_tutor_profile);
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = firebaseUser.getEmail().toString() ;
        setTitle("Profile");
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar = findViewById(R.id.progressBar) ;
        progressBar.setVisibility(View.VISIBLE);

        name = findViewById(R.id.name) ;
        email = findViewById(R.id.email) ;
        phoneNumber = findViewById(R.id.phoneNumber) ;
        gender = findViewById(R.id.gender) ;
        areaAddress = findViewById(R.id.areaAddress) ;
        currentPositon = findViewById(R.id.currentPosition) ;
        instituteName = findViewById(R.id.instituteName) ;
        subject = findViewById(R.id.subject) ;

        myRefCandidateTutorInfo.orderByChild("emailPK").equalTo(userEmail)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        System.out.println("Helo:" + dataSnapshot.getValue().toString());
                        candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                        addAccountInfoToProfile();
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });


    }

    public void addAccountInfoToProfile(){
        progressBar.setVisibility(View.GONE);

        name.setText("Name  :   " + candidateTutorInfo.getFirstName() + " " +  candidateTutorInfo.getLastName());
        email.setText("Email  :   " + candidateTutorInfo.getEmailPK() );
        phoneNumber.setText("Contact No  :   " + candidateTutorInfo.getMobileNumber() );
        gender.setText("Gender  :   " + candidateTutorInfo.getGender() );
        areaAddress.setText("Area Address  :   " + candidateTutorInfo.getAreaAddress() );
        currentPositon.setText("Current Position  :   " + candidateTutorInfo.getCurrentPosition() );
        instituteName.setText("Institute Name  :   " + candidateTutorInfo.getEdu_instituteName());
        subject.setText("Subject/Department  :   " + candidateTutorInfo.getEdu_tutorSubject());
    }


    public void goToCandidateTutorHomePageActivity(View view){
        System.out.println("Coronaaaaaaaaaaaaaaaaaaaa");
        Intent intent = new Intent(this, CandidateTutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
