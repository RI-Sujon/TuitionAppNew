package com.example.tuitionapp_surji.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tuitionapp_surji.batch.BatchCreationActivity;
import com.example.tuitionapp_surji.batch.BatchInfo;
import com.example.tuitionapp_surji.batch.BatchViewInfoActivity;
import com.example.tuitionapp_surji.batch.CustomAdapterForSelectAndViewBatch;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.guardian.CustomAdapterForTutorListView;
import com.example.tuitionapp_surji.guardian.ViewingSearchingTutorProfileActivity;
import com.example.tuitionapp_surji.message_box.MainMessageActivity;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.example.tuitionapp_surji.notice_board.NoticeBoardViewAndCreateActivity;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.ReportInfo;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupHomePageActivity extends AppCompatActivity {

    private DatabaseReference myRefGroup, myRefMessageBox, myRefReport, myRefBatch, myRefAddTutor, myRefCandidateTutor ;
    private FirebaseUser firebaseUser ;
    private MessageBoxInfo messageBoxInfo;
    private String user, userEmail, groupID , tutorUid, batchID , groupName,groupAddress;
    private ArrayList<String>userInfo ;

    private TextView groupNameTextView, fullAddressTextView ;

    private MaterialButton messageRequestButton, reportButton , blockButton;
    private ImageView messageFloatingButton ;

    private LinearLayout groupDashboardLayout ;

    private RelativeLayout batchListLayout ;
    private ListView batchListView ;
    private MaterialButton createNewBatchOptionButton ;
    private CustomAdapterForSelectAndViewBatch adapter ;
    private ArrayList<BatchInfo> batchInfoArrayList ;
    private ArrayList<String> batchIDArrayList ;


    private RelativeLayout groupTutorListLayout ;
    private ListView tutorListView ;
    private MaterialButton addTutorOptionButton ;
    private CustomAdapterForTutorListView adapter2 ;
    private ArrayList<AddTutorInfo> addTutorInfoArrayList ;
    private ArrayList<CandidateTutorInfo> candidateTutorInfoArrayList ;
    private ArrayList<String> tutorUidArrayList ;

    private MaterialCardView batchManagementCardView, tutorManagementCardView, noticeBoardManagementCardView ;

    private int backButtonFlag = 0, batchReUseFlag = 0, tutorReUseFlag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home_page);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;
        groupID = intent.getStringExtra("groupID") ;

        myRefGroup = FirebaseDatabase.getInstance().getReference("Group").child(groupID) ;
        myRefBatch = FirebaseDatabase.getInstance().getReference("Batch");
        myRefAddTutor = FirebaseDatabase.getInstance().getReference("AddTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        myRefGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class) ;
                groupHomePage(groupInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        if(user.equals("guardian")){
            messageFloatingButton = findViewById(R.id.messageBoxFloatingButtonS) ;
            messageFloatingButton.setVisibility(View.GONE);

            tutorUid = intent.getStringExtra("tutorUid");
            messageRequestButton = findViewById(R.id.sendMessageRequestButton) ;
            messageRequestButton.setVisibility(View.VISIBLE);
            //reportButton = findViewById(R.id.reportButton) ;
            messageRequestButton.setVisibility(View.VISIBLE);
            //reportButton.setVisibility(View.VISIBLE);
        }
        else if(user.equals("admin")){

            messageFloatingButton = findViewById(R.id.messageBoxFloatingButtonS) ;
            messageFloatingButton.setVisibility(View.GONE);

            //blockButton = findViewById(R.id.blockGroupButton) ;
            //blockButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        batchInfoArrayList = new ArrayList<>() ;
        batchIDArrayList = new ArrayList<>() ;

        addTutorInfoArrayList = new ArrayList<>() ;
        candidateTutorInfoArrayList = new ArrayList<>() ;
        tutorUidArrayList = new ArrayList<>() ;

        groupDashboardLayout = findViewById(R.id.group_home_page_dashboard_layout) ;
        batchListView = findViewById(R.id.batchList) ;
        createNewBatchOptionButton = findViewById(R.id.createNewBatchButton) ;

        batchListLayout = findViewById(R.id.batch_list_layout) ;

        groupNameTextView = findViewById(R.id.groupNameTextView) ;
        fullAddressTextView = findViewById(R.id.fullAddressTextView) ;

        groupTutorListLayout = findViewById(R.id.group_tutor_list_layout) ;
        tutorListView = findViewById(R.id.groupTutorList) ;
        addTutorOptionButton = findViewById(R.id.addTutorButton) ;


        batchManagementCardView = findViewById(R.id.batch_management_card);
        tutorManagementCardView = findViewById(R.id.group_tutor_card);
        noticeBoardManagementCardView = findViewById(R.id.notice_board_card);

        batchManagementCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBatchManagement();
            }
        });

        tutorManagementCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroupTutorManagement();
            }
        });

        noticeBoardManagementCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNoticeBoardManagement();
            }
        });
    }

    public void groupHomePage(GroupInfo groupInfo){
        groupName = groupInfo.getGroupName() ;
        groupAddress = groupInfo.getFullAddress() + ", " + groupInfo.getAddress() ;

        groupNameTextView.setText(groupName);
        fullAddressTextView.setText(groupAddress);
    }


    public void goToBatchManagement(){
        backButtonFlag = 1 ;

        if(batchReUseFlag==0){
            createNewBatchOptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupHomePageActivity.this, BatchCreationActivity.class) ;
                    intent.putExtra("user",user) ;
                    intent.putExtra("groupID" , groupID) ;
                    intent.putStringArrayListExtra("userInfo",userInfo) ;

                    startActivity(intent);
                    finish();
                }
            });

            batchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BatchInfo newBatchInfo = batchInfoArrayList.get(position) ;
                    batchID = batchIDArrayList.get(position) ;
                    goToViewBatchInfo() ;
                }
            });

            if(user.equals("tutor")){
                createNewBatchOptionButton.setVisibility(View.VISIBLE);
            }

            myRefBatch.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1: dataSnapshot.getChildren()){
                        BatchInfo batchInfo = dS1.getValue(BatchInfo.class) ;
                        if(batchInfo.getGroupIDFK().equals(groupID)){
                            batchInfoArrayList.add(batchInfo) ;
                            batchIDArrayList.add(dS1.getKey()) ;
                        }
                        goToBatchListView();
                    }

                    myRefBatch.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }
        else {

        }


        batchListLayout.setVisibility(View.VISIBLE);
        groupDashboardLayout.setVisibility(View.GONE);

        batchReUseFlag = 1 ;
    }

    public void goToBatchListView(){
        adapter = new CustomAdapterForSelectAndViewBatch(this,batchInfoArrayList) ;
        batchListView.setAdapter(adapter);
    }

    public void goToViewBatchInfo(){
        Intent intent = new Intent(this, BatchViewInfoActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("batchID",batchID) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID" , groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress", groupAddress) ;
        startActivity(intent);
        finish();
    }

    public void goToGroupTutorManagement(){
        backButtonFlag = 2 ;

        if(tutorReUseFlag==0){
            addTutorOptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupHomePageActivity.this, GroupTutorViewActivity.class) ;
                    intent.putExtra("user",user) ;
                    intent.putExtra("groupID" , groupID) ;
                    intent.putStringArrayListExtra("userInfo",userInfo) ;

                    startActivity(intent);
                    finish();
                }
            });

            if(user.equals("tutor")){
                addTutorOptionButton.setVisibility(View.VISIBLE);
            }

            tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String tutorUID = tutorUidArrayList.get(position) ;
                    goToViewTutorProfileInfo(tutorUID);
                }
            });

            myRefAddTutor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dS1: dataSnapshot.getChildren()){
                        AddTutorInfo addTutorInfo = dS1.getValue(AddTutorInfo.class) ;
                        if(addTutorInfo.getGroupID().equals(groupID)){
                            addTutorInfoArrayList.add(addTutorInfo) ;
                        }
                    }
                    myRefAddTutor.removeEventListener(this);


                    myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String email ;
                            for(DataSnapshot dS2: dataSnapshot.getChildren()){
                                CandidateTutorInfo candidateTutorInfo = dS2.getValue(CandidateTutorInfo.class);

                                for(int i=0; i<addTutorInfoArrayList.size();i++){
                                    if(candidateTutorInfo.getEmailPK().charAt(0)=='-'){
                                        email = candidateTutorInfo.getEmailPK().substring(1,candidateTutorInfo.getEmailPK().length()) ;
                                    }
                                    else email = candidateTutorInfo.getEmailPK() ;

                                    if(email.equals(addTutorInfoArrayList.get(i).getTutorEmail())){
                                        candidateTutorInfoArrayList.add(candidateTutorInfo) ;
                                        tutorUidArrayList.add(dS2.getKey()) ;
                                        break;
                                    }
                                }
                            }
                            goToGroupTutorListViewInfo();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }) ;
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }
        else {

        }

        groupTutorListLayout.setVisibility(View.VISIBLE);
        groupDashboardLayout.setVisibility(View.GONE);

        tutorReUseFlag = 1 ;
    }

    private void goToGroupTutorListViewInfo(){
        adapter2 = new CustomAdapterForTutorListView(this, candidateTutorInfoArrayList,"groupTutor") ;
        tutorListView.setAdapter(adapter2);
    }

    public void goToViewTutorProfileInfo(String tutorUid){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putExtra("user","groupAdmin") ;
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        else intent.putExtra("user","groupVisitor") ;

        intent.putExtra("tutorUid",tutorUid) ;

        startActivity(intent);
        finish();
    }

    public void goToMessageBox(View view){
        Intent intent = new Intent(this, MainMessageActivity.class);
        intent.putExtra("user",user) ;
        startActivity(intent);
        finish();
    }

    public void sendMessageRequestByGuardianFromGroup(View view){
        Intent intent = getIntent() ;
        tutorUid = intent.getStringExtra("tutorUid");
        userEmail = intent.getStringExtra("userEmail");
        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;

        messageBoxInfo = new MessageBoxInfo(firebaseUser.getPhoneNumber(),firebaseUser.getUid(),userEmail,tutorUid, true ,false) ;

        myRefMessageBox.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);
                    if(messageBoxInfo1.getGuardianUid().equals(firebaseUser.getUid())
                            && messageBoxInfo1.getTutorUid().equals(tutorUid)){
                        flag=1;
                    }

                }

                if(flag == 0){
                    myRefMessageBox.push().setValue(messageBoxInfo) ;
                    messageRequestButton.setEnabled(false);
                    messageRequestButton.setBackgroundColor(Color.GRAY);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reportIDByGuardian(View view){
        myRefReport = FirebaseDatabase.getInstance().getReference("Report") ;
        ReportInfo reportInfo = new ReportInfo(firebaseUser.getPhoneNumber(),userEmail, "this is a fake account") ;
        myRefReport.push().setValue(reportInfo) ;

        reportButton.setEnabled(false);
        reportButton.setBackgroundColor(Color.GRAY);
    }

    public void blockGroupByAdmin(View view){
        /*myRefBlockInfo = FirebaseDatabase.getInstance().getReference("Block") ;
        BlockInfo blockInfo = new BlockInfo("tuitionApsspl02@gmail.com",userEmail,true) ;
        myRefBlockInfo.push().setValue(blockInfo) ;

        blockButton.setEnabled(false);
        blockButton.setBackgroundColor(Color.GRAY);*/
    }


    public void goToNoticeBoardManagement(){
        Intent intent = new Intent(this, NoticeBoardViewAndCreateActivity.class) ;
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        if(backButtonFlag == 0){
            if(user.equals("tutor")){
                Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
                intent.putStringArrayListExtra("userInfo",userInfo) ;
                startActivity(intent);
                finish();
            }
            else if(user.equals("guardian")||user.equals("admin")){
                Intent intent = new Intent(this, ViewingSearchingTutorProfileActivity.class);
                intent.putExtra("user",user) ;
                startActivity(intent);
                finish();
            }
        }
        else if(backButtonFlag == 1){
            groupDashboardLayout.setVisibility(View.VISIBLE);
            batchListLayout.setVisibility(View.GONE);
            backButtonFlag = 0 ;
        }
        else if(backButtonFlag == 2){
            groupDashboardLayout.setVisibility(View.VISIBLE);
            groupTutorListLayout.setVisibility(View.GONE);
            backButtonFlag = 0 ;
        }
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
