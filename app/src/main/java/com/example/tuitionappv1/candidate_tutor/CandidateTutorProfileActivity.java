package com.example.tuitionappv1.candidate_tutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tuitionappv1.admin.AdminHomePageActivity;
import com.example.tuitionappv1.admin.ApproveInfo;
import com.example.tuitionappv1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.graphics.Color.GRAY;

public class CandidateTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefCandidateTutorInfo, myRefReferInfo, myRefApproveInfo ;
    private TextView name,email,phoneNumber,gender,areaAddress,currentPosition,instituteName,subject,referenceTextView ;
    private ProgressBar progressBar ;
    private ImageView idCardImageView ;

    private String userEmail,user ;
    private String allReference = "" ;

    private CandidateTutorInfo candidateTutorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_tutor_profile);
        Intent intent = getIntent() ;
        userEmail = intent.getStringExtra("userEmail") ;
        user = intent.getStringExtra("user") ;

        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        myRefReferInfo = FirebaseDatabase.getInstance().getReference("Refer");

        setTitle("Profile");
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar = findViewById(R.id.progressBar) ;


        name = findViewById(R.id.name) ;
        email = findViewById(R.id.email) ;
        phoneNumber = findViewById(R.id.phoneNumber) ;
        gender = findViewById(R.id.gender) ;
        areaAddress = findViewById(R.id.areaAddress) ;
        currentPosition = findViewById(R.id.currentPosition) ;
        instituteName = findViewById(R.id.instituteName) ;
        subject = findViewById(R.id.subject) ;
        referenceTextView = findViewById(R.id.referenceTextView) ;
        idCardImageView = findViewById(R.id.idCardImageView) ;


        myRefCandidateTutorInfo.orderByChild("emailPK").equalTo(userEmail)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;

                        myRefReferInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                myRefReferInfo.removeEventListener(this);
                                allReference = "" ;
                                for(DataSnapshot dS1 : dataSnapshot.getChildren()){
                                    ReferInfo referInfo = dS1.getValue(ReferInfo.class) ;

                                    if(referInfo.getCandidateTutorEmail().equals(userEmail)){
                                        allReference = allReference +  "\n" + referInfo.getVerifiedTutorEmail() + "\t\t\t\t\t\t\t\t\t" + referInfo.isReferApprove() ;
                                    }
                                }
                                addAccountInfoToProfile();
                                myRefCandidateTutorInfo.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );

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
        currentPosition.setText("Current Position  :   " + candidateTutorInfo.getCurrentPosition() );
        instituteName.setText("Institute Name  :   " + candidateTutorInfo.getEdu_instituteName());
        subject.setText("Subject/Department  :   " + candidateTutorInfo.getEdu_tutorSubject());
        referenceTextView.setVisibility(View.VISIBLE);

        TextView textView1 = findViewById(R.id.textView1) ;
        TextView textView2 = findViewById(R.id.textView2) ;
        Button approveButton = findViewById(R.id.approveButton) ;
        textView1.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        if(user.equals("admin")){
            approveButton.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(candidateTutorInfo.getIdCardImageUri()).into(idCardImageView);
        referenceTextView.setText("REFERENCES\t\t\t                    IS ACCEPTED?" + allReference);
    }

    public void approveCandidateTutorAsAVerifiedTutor(View view){
        myRefApproveInfo = FirebaseDatabase.getInstance().getReference("Approve") ;
        Button approveButton = findViewById(R.id.approveButton) ;
        approveButton.setText("Approved");
        approveButton.setBackgroundColor(GRAY);
        approveButton.setEnabled(false);
        ApproveInfo approveInfo = new ApproveInfo("tuitionapsspl02@gmail.com", userEmail) ;
        approveInfo.setApprove(true);
        myRefApproveInfo.push().setValue(approveInfo) ;
    }


    public void goToHomePageActivity(View view){

        if(user.equals("user")){
            Intent intent = new Intent(this, CandidateTutorHomePageActivity.class);
            startActivity(intent);
            finish();
        }
        else if(user.equals("admin")){
            Intent intent = new Intent(this, AdminHomePageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        goToHomePageActivity(null);
    }
}
