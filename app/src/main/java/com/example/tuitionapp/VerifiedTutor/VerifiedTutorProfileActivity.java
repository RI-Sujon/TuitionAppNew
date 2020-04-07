package com.example.tuitionapp.VerifiedTutor;

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

public class VerifiedTutorProfileActivity extends AppCompatActivity {

    private FirebaseDatabase database ;
    private DatabaseReference myRefAccountInfo, myRefTuitionInfo ;
    private TextView name,email,phoneNumber,gender,areaAddress,currentPositon,instituteName,subject ;
    private TextView medium,preferredClass,preferredGroup,preferredSubject,daysPerWeekOrMonth,salaryUpto ;
    private ProgressBar progressBar ;
    private FirebaseUser firebaseUser ;

    private String userEmail ;

    private CandidateTutorInfo candidateTutorInfo;
    private VerifiedTutorInfo verifiedTutorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        myRefAccountInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        myRefTuitionInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor");
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

        medium = findViewById(R.id.medium) ;
        preferredClass = findViewById(R.id.preferredClass) ;
        preferredGroup = findViewById(R.id.preferredGroup) ;
        preferredSubject = findViewById(R.id.preferredSubject) ;
        daysPerWeekOrMonth = findViewById(R.id.daysPerWeekOrMonth) ;
        salaryUpto = findViewById(R.id.salary) ;

        myRefAccountInfo.orderByChild("emailPK").equalTo(userEmail)
                .addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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



        myRefTuitionInfo.orderByChild("emailPK").equalTo(userEmail)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        System.out.println("Hellllo:" + dataSnapshot.getValue().toString());
                        verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class) ;
                        addTuitionInfoToProfile();
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

    public void addTuitionInfoToProfile(){
        System.out.println(firebaseUser.getUid()+" " + firebaseUser.getIdToken(true) + " " + firebaseUser.getProviderId() );
        medium.setText("Medium  :   " + verifiedTutorInfo.getPreferredMediumOrVersion() );
        String strClass = verifiedTutorInfo.getPreferredClasses() + "";
        if(strClass.length()>0) {
            strClass = strClass.replace("_"," || ") ;
            strClass = strClass.substring(0, strClass.length() - 4);
        }

        String strSub = verifiedTutorInfo.getPreferredSubjects() + "" ;
        if(strSub.length()>0) {
            strSub = strSub.replace("_", " || ");
            strSub = strSub.substring(0, strSub.length() - 4);
        }
        preferredClass.setText("Preferred Class  :   " +  strClass);
        preferredGroup.setText("Preferred Group  :   " + verifiedTutorInfo.getPreferredGroup() );
        preferredSubject.setText("Preferred Subject  :   " + strSub );
        daysPerWeekOrMonth.setText("Days Per Week  :   " + verifiedTutorInfo.getPreferredDaysPerWeekOrMonth() );
        salaryUpto.setText("Salary Upto  :   " + verifiedTutorInfo.getMinimumSalary() );
    }

    public void goToTutorHomePageActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
