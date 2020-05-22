package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.example.tuitionapp_surji.admin.AdminHomePageActivity;
import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.group.GroupInfo;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorInfo;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewingSearchingTutorProfileActivity extends AppCompatActivity {

    private DatabaseReference myRefVerifiedTutor, myRefCandidateTutor, myRefGroup, myRefApproveAndBlock;

    private ArrayList<ApproveAndBlockInfo> approveAndBlockInfoList ;
    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;
    private ArrayList<String> tutorUidInfoArrayList ;
    private ArrayList<VerifiedTutorInfo> verifiedTutorInfoList ;
    private ArrayList<GroupInfo> groupInfoList ;
    private ArrayList<String> approveAndBlockTutorUidList ;
    private ArrayList<String> groupNameList ;
    private ArrayList<String> groupIDList ;
    private ArrayList<String> groupAdminEmailList ;
    private ArrayList<String> emailList ;
    private ArrayList<String> tutorUidList ;

    private Map<String,String> profilePicUriListMap ;

    private CustomAdapterForTutorListView adapter ;
    private CustomAdapterForGroupListView adapter2 ;

    private String user;

    private ListView tutorListView ;
    private ListView groupListView ;
    private EditText searchBar ;
    private ViewFlipper viewFlipper ;
    private Button tutorListViewButton, groupListViewButton ;
    private ImageView tutorListViewButton2, groupListViewButton2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_searching_tutor_profile);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        tutorListView = findViewById(R.id.verifiedTutorList) ;
        groupListView = findViewById(R.id.groupList) ;
        searchBar = findViewById(R.id.search_bar) ;
        viewFlipper = findViewById(R.id.viewFlipper) ;
        tutorListViewButton = findViewById(R.id.tutorListViewButton) ;
        tutorListViewButton2 = findViewById(R.id.tutor_list_button_image_view) ;
        groupListViewButton = findViewById(R.id.groupListViewButton) ;
        groupListViewButton2 = findViewById(R.id.group_list_button_image_view) ;

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group") ;
        myRefApproveAndBlock = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;

        tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // String tutorUid = tutorUidInfoArrayList.get(position) ;
                String userEmail = emailList.get(position) ;
                String tutorUid = tutorUidList.get(position) ;
                goToSelectedVerifiedTutorProfile(userEmail,tutorUid) ;
            }
        });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupID = groupIDList.get(position) ;
               // String userEmail = emailList.get(position) ;

                String adminEmail = groupAdminEmailList.get(position) ;
                String groupAdminUid = groupInfoList.get(position).getGroupAdminUid();
                goToSelectedGroup(groupID,groupAdminUid,adminEmail) ;
            }
        });

        tutorListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(0);
                tutorListViewButton.setVisibility(View.GONE);
                tutorListViewButton2.setVisibility(View.VISIBLE);
                groupListViewButton.setVisibility(View.VISIBLE);
                groupListViewButton2.setVisibility(View.GONE);
            }
        });

        groupListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(1);
                tutorListViewButton.setVisibility(View.VISIBLE);
                tutorListViewButton2.setVisibility(View.GONE);
                groupListViewButton.setVisibility(View.GONE);
                groupListViewButton2.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifiedTutorInfoList = new ArrayList<>() ;
        tutorUidList = new ArrayList<>() ;
        approveAndBlockTutorUidList = new ArrayList<>() ;
        tutorUidInfoArrayList = new ArrayList<>();
        approveAndBlockInfoList = new ArrayList<>() ;
        candidateTutorInfoArrayList = new ArrayList<>() ;
        groupAdminEmailList = new ArrayList<>() ;
        emailList = new ArrayList<>() ;

        profilePicUriListMap = new HashMap<String, String>() ;

        groupInfoList = new ArrayList<>() ;
        groupNameList = new ArrayList<>() ;
        groupIDList = new ArrayList<>() ;

        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                    verifiedTutorInfoList.add(verifiedTutorInfo) ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefApproveAndBlock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    ApproveAndBlockInfo approveAndBlockInfo = dS1.getValue(ApproveAndBlockInfo.class) ;

                    if(approveAndBlockInfo.getStatus().equals("running")){
                        approveAndBlockTutorUidList.add(dS1.getKey()) ;
                        myRefApproveAndBlock.removeEventListener(this);
                    }

                }

                myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            for (String info : approveAndBlockTutorUidList) {
                                if (dS1.getKey().equals(info)){
                                    CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class);
                                    candidateTutorInfoArrayList.add(candidateTutorInfo) ;
                                    tutorUidInfoArrayList.add(dS1.getKey()) ;
                                    break;
                                }
                            }
                        }

                        setVerifiedTutorListView();

                        myRefCandidateTutor.removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                myRefApproveAndBlock.removeEventListener(this);
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
            for(int i=0; i<candidateTutorInfoArrayList.size();i++){
                if(vT.getEmailPK().equals(candidateTutorInfoArrayList.get(i).getEmailPK())){
                    emailList.add(vT.getEmailPK()) ;
                    tutorUidList.add(tutorUidInfoArrayList.get(i));
                    //nameList.add(candidateTutorInfoList.get(i).getFirstName()+" "+candidateTutorInfoList.get(i).getLastName()) ;
                }
            }
        }
        adapter = new CustomAdapterForTutorListView(this, candidateTutorInfoArrayList, "guardianTutor");
        tutorListView.setAdapter(adapter);
    }

    public void setGroupListView(){

        for(GroupInfo groupInfo: groupInfoList){
            groupNameList.add(groupInfo.getGroupName()) ;
            groupAdminEmailList.add(groupInfo.getGroupAdminEmail());
        }
        adapter2 = new CustomAdapterForGroupListView(this,groupInfoList);
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
        intent.putExtra("user", user) ;
        intent.putExtra("tutorUid",tutorUid);
        intent.putExtra("userEmail", userEmail) ;
        startActivity(intent);
        finish();
    }

    public void goToSelectedGroup(String groupID, String tutorUid, String userEmail){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("user", user) ;
        intent.putExtra("tutorUid", tutorUid) ;
        intent.putExtra("userEmail", userEmail) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
    }
}

