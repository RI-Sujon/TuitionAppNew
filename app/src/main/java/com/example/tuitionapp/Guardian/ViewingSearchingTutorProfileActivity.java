package com.example.tuitionapp.Guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tuitionapp.Admin.AdminHomePageActivity;
import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;
import com.example.tuitionapp.Group.GroupHomePageActivity;
import com.example.tuitionapp.Group.GroupInfo;
import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorProfileActivity;
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
    private ArrayList<GroupInfo> groupInfoList ;
    private ArrayList<String> emailList ;
    private ArrayList<String> nameList ;
    private ArrayList<String> groupNameList ;
    private ArrayList<String> groupEmailList ;

    private CustomAdapterForViewingSearchingTutorProfile adapter ;
    private CustomAdapterForViewingSearchingGroup adapter2 ;

    private String user;

    private ListView tutorListView ;
    private ListView groupListView ;
    private Button searchButton ;
    private EditText searchBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_searching_tutor_profile);
        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        tutorListView = findViewById(R.id.verifiedTutorList) ;
        groupListView = findViewById(R.id.groupList) ;
        searchButton = findViewById(R.id.searchButton) ;
        searchBar = findViewById(R.id.search_bar) ;
        setTitle("SEARCHING");

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group") ;

        tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userEmail = emailList.get(position) ;
                goToSelectedVerifiedTutorProfile(userEmail) ;
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userEmail = groupEmailList.get(position) ;
                goToSelectedGroup(userEmail) ;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        verifiedTutorInfoList = new ArrayList<>() ;
        candidateTutorInfoList = new ArrayList<>() ;
        emailList = new ArrayList<>() ;
        nameList = new ArrayList<>() ;

        groupInfoList = new ArrayList<>() ;
        groupNameList = new ArrayList<>() ;
        groupEmailList = new ArrayList<>() ;

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
                    setGroupListView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    public void setVerifiedTutorListView(){
        for(VerifiedTutorInfo vT: verifiedTutorInfoList){
            for(CandidateTutorInfo cT: candidateTutorInfoList){
                if(vT.getEmailPK().equals(cT.getEmailPK())){
                    emailList.add(vT.getEmailPK()) ;
                    nameList.add(cT.getFirstName()+" "+cT.getLastName()) ;
                }
            }
        }

        //adapter = new CustomAdapterForViewingSearchingTutorProfile(this,nameList,emailList);
        //groupListView.setAdapter(adapter);
    }

    public void setGroupListView(){
        for(GroupInfo groupInfo: groupInfoList){
            groupNameList.add(groupInfo.getGroupName()) ;
            groupEmailList.add(groupInfo.getGroupAdminEmail()) ;
        }

        adapter2 = new CustomAdapterForViewingSearchingGroup(this,nameList,emailList);
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

    public void goToSelectedVerifiedTutorProfile(String userEmail){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("userEmail", userEmail) ;
        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    public void goToSelectedGroup(String userEmail){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("userEmail", userEmail) ;
        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }
}
