package com.example.tuitionapp_sujon.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tuitionapp_sujon.R;
import com.example.tuitionapp_sujon.admin.ApproveAndBlockInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupTutorViewActivity extends AppCompatActivity {

    private DatabaseReference myRefAddTutor, myRefCandidateTutor, myRefApproveAndBlock ;

    private Button addTutorButton;
    private TextInputEditText tutorEmailEditText ;
    private String tutorEmail ;

    private String groupID ,user ;
    private ArrayList<String>userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tutor_view);
        Intent intent = getIntent() ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user") ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        addTutorButton = findViewById(R.id.addTutor) ;
        tutorEmailEditText = findViewById(R.id.group_tutor_email) ;

        myRefAddTutor = FirebaseDatabase.getInstance().getReference("AddTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefApproveAndBlock = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;

    }

    public void addTutorOperation(View view){
        tutorEmail = tutorEmailEditText.getText().toString() ;

        myRefCandidateTutor.orderByChild("emailPK").equalTo(tutorEmail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myRefApproveAndBlock = myRefApproveAndBlock.child(dataSnapshot.getKey()) ;
                System.out.println("sssssssssssssss" + dataSnapshot.getKey());
                myRefApproveAndBlock.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        if(dataSnapshot2.exists()){
                            ApproveAndBlockInfo approveAndBlockInfo = dataSnapshot2.getValue(ApproveAndBlockInfo.class) ;
                            if(approveAndBlockInfo.getStatus().equals("running")){
                                AddTutorInfo addTutorInfo = new AddTutorInfo(groupID,tutorEmail) ;
                                myRefAddTutor.push().setValue(addTutorInfo) ;
                            }
                            System.out.println("9999999s" + dataSnapshot2.getKey());

                            goToBackPageActivity(null);
                        }
                        else System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa..nai");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }) ;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }


    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("groupID", groupID) ;
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
