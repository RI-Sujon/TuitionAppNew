package com.example.tuitionapp.VerifiedTutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.tuitionapp.CandidateTutor.ReferInfo;
import com.example.tuitionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerifiedTutorNotificationActivity extends AppCompatActivity {

    private DatabaseReference myRefRefer ;
    private ArrayList<ReferInfo>referInfoList ;
    private Map<String,ReferInfo> map = new HashMap<String,ReferInfo>() ;
    private ArrayList<String> candidateTutorEmailList ;
    private FirebaseUser user ;

    private ListView listView ;

    ArrayList<String>userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_notification);
        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        myRefRefer = FirebaseDatabase.getInstance().getReference("Refer") ;
        user = FirebaseAuth.getInstance().getCurrentUser() ;
        listView = findViewById(R.id.candidateTutorList) ;

        referInfoList = new ArrayList<>() ;
        candidateTutorEmailList = new ArrayList<>() ;

        myRefRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Checking4");
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    ReferInfo referInfo = dS1.getValue(ReferInfo.class) ;
                    if(user.getEmail().equals(referInfo.getVerifiedTutorEmail()) && referInfo.isReferApprove()!= true){
                        referInfoList.add(referInfo) ;
                        map.put(dS1.getKey(),referInfo) ;
                    }
                }
                System.out.println("Checking3");
                myRefRefer.removeEventListener(this);
                addReferenceNotification() ;

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void addReferenceNotification(){
        for(ReferInfo rf: referInfoList){
            candidateTutorEmailList.add(rf.getCandidateTutorEmail()) ;
        }

        CustomAdapterForVerifiedTutorNotification adapter = new CustomAdapterForVerifiedTutorNotification(this,candidateTutorEmailList,map,user.getEmail());
        listView.setAdapter(adapter);
    }

    public void backToHomePage(View view){
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }


}
