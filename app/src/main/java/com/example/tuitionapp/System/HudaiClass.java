package com.example.tuitionapp.System;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;
import com.example.tuitionapp.Guardian.GuardianHomePageActivity;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HudaiClass {

    FirebaseDatabase database ;
    DatabaseReference myRefCandidateTutorInfo , myRefVerifiedTutorInfo ;

    List<CandidateTutorInfo> candidateTutorInfoList  ;
    List<VerifiedTutorInfo> verifiedTutorInfoList  ;

    public HudaiClass(){
        candidateTutorInfoList = new ArrayList<>() ;
        verifiedTutorInfoList = new ArrayList<>() ;
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefVerifiedTutorInfo = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
    }

    public List<CandidateTutorInfo> getCandidateTutorInfoList(){
            myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1: dataSnapshot.getChildren()){
                        CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                        candidateTutorInfoList.add(candidateTutorInfo) ;
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        return candidateTutorInfoList ;
    }

    public List<VerifiedTutorInfo> getVerifiedTutorInfoList(){
        myRefVerifiedTutorInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                    verifiedTutorInfoList.add(verifiedTutorInfo) ;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        return verifiedTutorInfoList ;
    }
}
