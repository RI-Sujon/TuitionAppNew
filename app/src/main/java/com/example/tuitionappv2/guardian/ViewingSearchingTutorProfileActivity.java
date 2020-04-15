package com.example.tuitionappv2.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.example.tuitionappv2.admin.AdminHomePageActivity;
import com.example.tuitionappv2.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionappv2.group.GroupHomePageActivity;
import com.example.tuitionappv2.group.GroupInfo;
import com.example.tuitionappv2.R;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorInfo;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewingSearchingTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefVerifiedTutor, myRefCandidateTutor, myRefGroup;

    private ArrayList<VerifiedTutorInfo> verifiedTutorInfoList ;
    private ArrayList<CandidateTutorInfo> candidateTutorInfoList ;
    private ArrayList<String> tutorUidInfoArrayList;

    private ArrayList<GroupInfo> groupInfoList ;
    private ArrayList<String> emailList ;
    private ArrayList<String> nameList ;
    private ArrayList<String> tutorUidList ;
    private ArrayList<String> groupNameList ;
    private ArrayList<String> groupIDList ;

    private CustomAdapterForViewingSearchingTutorProfile adapter ;
    private CustomAdapterForViewingSearchingGroup adapter2 ;

    private String user;

    private ListView tutorListView ;
    private ListView groupListView ;
    private Button searchButton ;
    private EditText searchBar ;
    private ViewFlipper viewFlipper ;
    private Button tutorListViewButton, groupListViewButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_searching_tutor_profile);
//        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        tutorListView = findViewById(R.id.verifiedTutorList) ;
        groupListView = findViewById(R.id.groupList) ;
        searchButton = findViewById(R.id.searchButton) ;
        searchBar = findViewById(R.id.search_bar) ;
        viewFlipper = findViewById(R.id.viewFlipper) ;
        tutorListViewButton = findViewById(R.id.tutorListViewButton) ;
        groupListViewButton = findViewById(R.id.groupListViewButton) ;
        setTitle("SEARCHING");

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group") ;

        tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userEmail = emailList.get(position) ;
                String tutorUid = tutorUidList.get(position) ;
                goToSelectedVerifiedTutorProfile(userEmail,tutorUid) ;
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupID = groupIDList.get(position) ;
                String groupAdminUid = groupInfoList.get(position).getGroupAdminUid();
                goToSelectedGroup(groupID,groupAdminUid) ;
            }
        });

        tutorListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(0);
                tutorListViewButton.setBackgroundColor(Color.rgb(144,238,144));
                tutorListViewButton.setTextColor(Color.rgb(0,0,128));
                groupListViewButton.setBackgroundColor(Color.GRAY);
                groupListViewButton.setTextColor(Color.BLACK);
            }
        });

        groupListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(1);
                groupListViewButton.setBackgroundColor(Color.rgb(144,238,144));
                groupListViewButton.setTextColor(Color.rgb(0,0,128));
                tutorListViewButton.setBackgroundColor(Color.GRAY);
                tutorListViewButton.setTextColor(Color.BLACK);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        tutorUidInfoArrayList = new ArrayList<>();
        verifiedTutorInfoList = new ArrayList<>() ;
        candidateTutorInfoList = new ArrayList<>() ;

        emailList = new ArrayList<>() ;
        tutorUidList = new ArrayList<>() ;
        nameList = new ArrayList<>() ;

        groupInfoList = new ArrayList<>() ;
        groupNameList = new ArrayList<>() ;
        groupIDList = new ArrayList<>() ;

        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                    verifiedTutorInfoList.add(verifiedTutorInfo) ;
                }

                myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                            candidateTutorInfoList.add(candidateTutorInfo) ;
                            tutorUidInfoArrayList.add(dS1.getKey()) ;
                        }
                        myRefCandidateTutor.removeEventListener(this);
                        setVerifiedTutorListView();
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                myRefVerifiedTutor.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        myRefGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1:dataSnapshot.getChildren()){
                    GroupInfo groupInfo = dS1.getValue(GroupInfo.class) ;
                    groupInfoList.add(groupInfo) ;
                    groupIDList.add(dS1.getKey()) ;
                }
                setGroupListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    public void setVerifiedTutorListView(){
        for(VerifiedTutorInfo vT: verifiedTutorInfoList){
            for(int i=0; i<candidateTutorInfoList.size();i++){
                if(vT.getEmailPK().equals(candidateTutorInfoList.get(i).getEmailPK())){
                    emailList.add(vT.getEmailPK()) ;
                    tutorUidList.add(tutorUidInfoArrayList.get(i));
                    nameList.add(candidateTutorInfoList.get(i).getFirstName()+" "+candidateTutorInfoList.get(i).getLastName()) ;
                }
            }
        }

        adapter = new CustomAdapterForViewingSearchingTutorProfile(this,nameList,emailList);
        tutorListView.setAdapter(adapter);
    }

    public void setGroupListView(){
        for(GroupInfo groupInfo: groupInfoList){
            groupNameList.add(groupInfo.getGroupName()) ;
        }

        adapter2 = new CustomAdapterForViewingSearchingGroup(this,groupNameList);
        groupListView.setAdapter(adapter2);

    }

    public void backToHomePage(View view){

        if(user.equals("guardian")){
            Intent intent = new Intent(this, GuardianHomePageActivity.class);
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
        backToHomePage(null);
    }

    public void goToSelectedVerifiedTutorProfile(String userEmail,String tutorUid){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("userEmail", userEmail) ;
        intent.putExtra("user", user) ;
        intent.putExtra("tutorUid",tutorUid);
        startActivity(intent);
        finish();
    }

    public void goToSelectedGroup(String groupID,String tutorUid){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("user", user) ;
        intent.putExtra("tutorUid", tutorUid) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
    }
}
