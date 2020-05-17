package com.example.tuitionapp_sujon.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tuitionapp_sujon.candidate_tutor.CandidateTutorInfo;

import com.example.tuitionapp_sujon.R;
import com.example.tuitionapp_sujon.guardian.CustomAdapterForTutorListView;
import com.example.tuitionapp_sujon.verified_tutor.VerifiedTutorProfileActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminTutorProfileViewActivity extends AppCompatActivity {

    private DatabaseReference myRefCandidateTutorInfo, myRefApproveAndBlockInfo;

    private ArrayList<CandidateTutorInfo> candidateTutorInfoList ;
    private List<String>  approveInfoList ;
    private List<String>  blockInfoList ;

    private CustomAdapterForTutorListView adapterForListView ;

    private ListView listView ;
    private String listTypeFlag ;
    private String approveAndBlockInfoStatus ;

    private ArrayList<String> tutorUidList ;

    private MaterialToolbar materialToolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tutor_profile_view);

        Intent intent = getIntent() ;
        listTypeFlag = intent.getStringExtra("flag");

        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHomePage();
            }
        });

        if(listTypeFlag.equals("blockTutor")){
            approveAndBlockInfoStatus = "blocked" ;
            materialToolbar.setTitle("BLOCKED TUTOR");
            materialToolbar.setTitleTextColor(Color.rgb(251,18,18));
        }
        else if(listTypeFlag.equals("approveTutor")){
            approveAndBlockInfoStatus = "waiting" ;
            materialToolbar.setTitle("New Tutor Request");
        }

        listView = findViewById(R.id.candidateTutorList) ;
        setTitle("VIEW TUTOR PROFILE");
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefApproveAndBlockInfo = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tutorUid = tutorUidList.get(position) ;
                goToSelectedCandidateTutorProfile(tutorUid) ;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        candidateTutorInfoList = new ArrayList<>() ;
        approveInfoList = new ArrayList<>() ;
        tutorUidList = new ArrayList<>();

        myRefApproveAndBlockInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {
                for(DataSnapshot dS1: dataSnapshot1.getChildren()){
                    ApproveAndBlockInfo approveInfo = dS1.getValue(ApproveAndBlockInfo.class) ;
                    if(approveInfo.getStatus().equals(approveAndBlockInfoStatus)){
                        approveInfoList.add(dS1.getKey()) ;
                        myRefApproveAndBlockInfo.removeEventListener(this);
                    }
                }

                myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()) {
                            for (String info : approveInfoList) {
                                if (dS1.getKey().equals(info)){
                                    CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class);
                                    candidateTutorInfoList.add(candidateTutorInfo) ;
                                    tutorUidList.add(dS1.getKey()) ;
                                    break;
                                }
                            }
                        }
                        setListView();
                        myRefCandidateTutorInfo.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
                setListView() ;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void setListView(){
        adapterForListView = new CustomAdapterForTutorListView(this, candidateTutorInfoList , "admin");
        listView.setAdapter(adapterForListView);
    }

    public void backToHomePage(){
        Intent intent = new Intent(this, AdminHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSelectedCandidateTutorProfile(String tutorUid){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("tutorUid", tutorUid) ;
        if(listTypeFlag.equals("approveTutor"))
            intent.putExtra("user", "admin2") ;

        else if(listTypeFlag.equals("blockTutor"))
            intent.putExtra("user", "admin3") ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        backToHomePage();
    }
}

