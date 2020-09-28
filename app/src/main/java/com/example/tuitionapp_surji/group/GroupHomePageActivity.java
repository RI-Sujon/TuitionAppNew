package com.example.tuitionapp_surji.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.batch.BatchCreationActivity;
import com.example.tuitionapp_surji.batch.BatchInfo;
import com.example.tuitionapp_surji.batch.BatchViewInfoActivity;
import com.example.tuitionapp_surji.batch.CustomAdapterForSelectAndViewBatch;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.guardian.CustomAdapterForTutorListView;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.guardian.ViewingSearchingTutorProfileActivity;
import com.example.tuitionapp_surji.message_box.MainMessageActivity;
import com.example.tuitionapp_surji.message_box.MessageActivity;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.example.tuitionapp_surji.notice_board.NoticeBoardViewAndCreateActivity;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.notification_pack.NotificationViewActivity;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewActivity;
import com.example.tuitionapp_surji.verified_tutor.ReportInfo;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupHomePageActivity extends AppCompatActivity {

    private DatabaseReference myRefGroup, myRefMessageBox, myRefReport, myRefBatch, myRefAddTutor, myRefCandidateTutor, myRefNotification ;
    private FirebaseUser firebaseUser ;
    private MessageBoxInfo messageBoxInfo;
    private String user, tutorEmail, groupID , tutorUid2, batchID , groupName, groupAddress, context, viewType;
    private ArrayList<String>userInfo ;

    private TextView groupNameTextView, fullAddressTextView ;
    private ImageView groupProfileImage, leaveFromGroup ;
    private String leaveFlag ;

    private MaterialButton messageRequestButton, reportButton , blockButton;
    private RelativeLayout messageFloatingButton ;

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

    private GroupInfo groupInfo ;
    private Dialog mDialog;
    private TextView request_acceptation_btn,request_accept_yes_btn,request_accept_no_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home_page);

        Intent intent = getIntent() ;
        mDialog = new Dialog(this);
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;
        groupID = intent.getStringExtra("groupID") ;
        viewType = intent.getStringExtra("viewType") ;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group").child(groupID) ;
        myRefBatch = FirebaseDatabase.getInstance().getReference("Batch");
        myRefAddTutor = FirebaseDatabase.getInstance().getReference("AddTutor").child(groupID) ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Tutor").child(firebaseUser.getUid()) ;

        onStartActivity();

        myRefGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupInfo = dataSnapshot.getValue(GroupInfo.class) ;
                groupHomePage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        if(user.equals("guardian")||user.equals("guardianHomePage")){
            //messageFloatingButton.setVisibility(View.GONE);

            context = intent.getStringExtra("context") ;
            tutorUid2 = intent.getStringExtra("tutorUid");
            tutorEmail = intent.getStringExtra("userEmail");
            myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;
            messageRequestButton = findViewById(R.id.sendMessageRequestButton) ;
            messageRequestButton.setVisibility(View.VISIBLE);

            messageRequestButton.setVisibility(View.VISIBLE);

            myRefMessageBox.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        MessageBoxInfo messageBoxInfo = dataSnapshot.getValue(MessageBoxInfo.class);

                        if (messageBoxInfo.getGuardianUid().equals(firebaseUser.getUid()) && messageBoxInfo.getTutorUid().equals(groupInfo.getGroupAdminUid()))
                        {
                            if(!messageBoxInfo.isMessageFromGuardianSide() && !messageBoxInfo.isMessageFromTutorSide()){
                                messageRequestButton.setText("Send A Message Request");
                            }

                            else if(messageBoxInfo.isMessageFromGuardianSide() && messageBoxInfo.isMessageFromTutorSide()){
                                messageRequestButton.setText("Send Message");
                            }

                            else if((messageBoxInfo.isMessageFromGuardianSide()) && !messageBoxInfo.isMessageFromTutorSide()){
                                messageRequestButton.setText("Request Sent");
                            }

                            else if(!messageBoxInfo.isMessageFromGuardianSide() && messageBoxInfo.isMessageFromTutorSide()){
                                messageRequestButton.setText("Respond Request");
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(user.equals("admin")){
            messageFloatingButton.setVisibility(View.GONE);

            //blockButton = findViewById(R.id.blockGroupButton) ;
            //blockButton.setVisibility(View.VISIBLE);
        }
        else if(user.equals("groupVisitor")){
            leaveFromGroup.setVisibility(View.VISIBLE);
        }

    }

    private void onStartActivity() {

        batchInfoArrayList = new ArrayList<>() ;
        batchIDArrayList = new ArrayList<>() ;

        addTutorInfoArrayList = new ArrayList<>() ;
        candidateTutorInfoArrayList = new ArrayList<>() ;
        tutorUidArrayList = new ArrayList<>() ;

        groupDashboardLayout = findViewById(R.id.group_home_page_dashboard_layout) ;
        batchListView = findViewById(R.id.batchList) ;
        createNewBatchOptionButton = findViewById(R.id.createNewBatchButton) ;

        batchListLayout = findViewById(R.id.batch_list_layout) ;
        leaveFromGroup = findViewById(R.id.leave) ;

        groupNameTextView = findViewById(R.id.groupNameTextView) ;
        fullAddressTextView = findViewById(R.id.fullAddressTextView) ;
        groupProfileImage = findViewById(R.id.groupProfileImage) ;

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

        if(viewType!=null){
            if(viewType.equals("batchView")){
                goToBatchManagement();
            }
            else if(viewType.equals("tutorView")){
                goToGroupTutorManagement();
            }
        }
    }

    public void groupHomePage(){
        groupName = groupInfo.getGroupName() ;
        groupAddress = groupInfo.getFullAddress() + ", " + groupInfo.getAddress() ;

        groupNameTextView.setText(groupName);
        fullAddressTextView.setText(groupAddress);
        if(!groupInfo.getGroupImageUri().equals("")){
            Picasso.get().load(groupInfo.getGroupImageUri()).into(groupProfileImage);
        }

        groupProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGroupProfile(null);
            }
        });
    }

    public void goToGroupProfile(View view){
        Intent intent = new Intent(this, GroupProfileActivity.class) ;
        intent.putExtra("groupName", groupInfo.getGroupName());
        intent.putExtra("groupAddress", groupInfo.getAddress());
        intent.putExtra("groupFullAddress", groupInfo.getFullAddress());
        intent.putExtra("classRange", groupInfo.getClassRange());
        intent.putExtra("extraInfo", groupInfo.getExtraInfo());
        intent.putExtra("groupImage", groupInfo.getGroupImageUri());
        intent.putExtra("groupID", groupID) ;
        intent.putExtra("user", user) ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);

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

        intent.putExtra("user", user) ;

        intent.putExtra("batchID",batchID) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID" , groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress", groupAddress) ;
        if(user.equals("guardian")){
            intent.putExtra("context", context) ;
            intent.putExtra("userEmail" , tutorEmail) ;
            intent.putExtra("tutorUid",tutorUid2) ;
        }
        startActivity(intent);
        finish();
    }

    public void goToGroupTutorManagement(){
        backButtonFlag = 2 ;

        if(tutorReUseFlag==0){
            addTutorOptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupHomePageActivity.this, GroupTutorAddActivity.class) ;
                    intent.putExtra("user",user) ;
                    intent.putExtra("groupID" , groupID) ;
                    intent.putExtra("groupName", groupName) ;
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
                        addTutorInfoArrayList.add(addTutorInfo) ;
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
            finish();
        }
        else if(user.equals("guardian")){
            intent.putExtra("user", user) ;
            intent.putExtra("context2", "group") ;
            intent.putExtra("context", context) ;
            intent.putExtra("userEmail" , tutorEmail) ;
            intent.putExtra("tutorUid2",tutorUid2) ;
        }
        else {
            intent.putExtra("user", user);
        }

        intent.putExtra("tutorUid",tutorUid) ;

        startActivity(intent);
        finish();
    }


    public void sendMessageRequestByGuardianFromGroup(View view){
        //Send A Message Request

        myRefMessageBox.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String string = String.valueOf(messageRequestButton.getText());
                int flag = 0;
                Log.e("String  ",string);

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    final String snapshotKey = snapshot.getKey();
                    Log.e("DataSnapshotKey ",snapshotKey);

                    MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);

                    if (messageBoxInfo1.getGuardianUid().equals(firebaseUser.getUid()) && messageBoxInfo1.getTutorUid().equals(groupInfo.getGroupAdminUid()))
                    {
                        flag=1;
                        if(flag==1 && string.equals("Send A Message Request")){

                            Log.e("Message Button Text ","The text is Send Message. ");
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("messageFromGuardianSide",true);
                            myRefMessageBox.child(snapshot.getKey()).updateChildren(hashMap);
                            messageRequestButton.setText("Request Sent");
                            myRefMessageBox.removeEventListener(this);
                        }

                        else if(string.equals("Request Sent")){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("messageFromGuardianSide",false);
                            myRefMessageBox.child(snapshot.getKey()).updateChildren(hashMap);
                            messageRequestButton.setText("Send A Message Request");
                            Log.e("Message Button Text ","The text is Request Sent. ");
                            myRefMessageBox.removeEventListener(this);
                        }

                        else if(string.equals("Send Message"))
                        {
                            Log.e("DataSnapshotKey ",snapshotKey);
                            Log.e("Tutor Email ", groupInfo.getGroupAdminEmail());
                            Log.e("Tutor User ID ", groupInfo.getGroupAdminUid());

                            Intent intent = new Intent(GroupHomePageActivity.this, MessageActivity.class);
                            intent.putExtra("userId",  groupInfo.getGroupAdminUid());
                            intent.putExtra("tutorEmail", groupInfo.getGroupAdminEmail());
                            intent.putExtra("user", "guardian");
                            startActivity(intent);
                            finish();
                        }

                        else if(string.equals("Respond Request"))
                        {
                            Log.e("Message Button Text ","The text is Respond. ");
                            Log.e("DataSnapshotKey ",snapshotKey);

                            mDialog.setContentView(R.layout.custom_pop_up_accept_message_request);
                            request_acceptation_btn = mDialog.findViewById(R.id.request_acceptation_btn);
                            request_accept_yes_btn = mDialog.findViewById(R.id.request_accept_yes_btn);
                            request_accept_no_btn = mDialog.findViewById(R.id.request_accept_no_btn);
                            mDialog.show();

                            request_accept_yes_btn.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("messageFromGuardianSide",true);
                                    myRefMessageBox.child(snapshotKey).updateChildren(hashMap);
                                    mDialog.dismiss();
                                }
                            });

                            request_accept_no_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });

                            myRefMessageBox.removeEventListener(this);
                        }

                        break;
                    }
                }

                Log.e("Flag  ", String.valueOf(flag));
                if(flag==0 && string.equals("Send A Message Request"))
                {

                    messageBoxInfo = new MessageBoxInfo(firebaseUser.getPhoneNumber(),firebaseUser.getUid(), groupInfo.getGroupAdminEmail(),
                            groupInfo.getGroupAdminUid(), true , false,false,
                            false,true) ;


                    myRefMessageBox.push().setValue(messageBoxInfo) ;
                    messageRequestButton.setText("Request Sent");
                    myRefMessageBox.removeEventListener(this);
                }


                myRefMessageBox.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reportIDByGuardian(View view){
        myRefReport = FirebaseDatabase.getInstance().getReference("Report").child(tutorUid2) ;
        ReportInfo reportInfo = new ReportInfo(firebaseUser.getPhoneNumber(), "this is a fake account") ;
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
        if(user.equals("tutor")) {
            finish();
        }
    }

    public void goToBackPageActivity(View view){
        if(backButtonFlag == 0){
            if(user.equals("tutor")){
                finish();
            }
            else if(user.equals("guardian")||user.equals("admin")){
                Intent intent ;
                if(context==null){
                    intent = new Intent(this, ViewingSearchingTutorProfileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
                else if(context.equals("homepage")){
                    finish();
                }
            }
            else if(user.equals("groupVisitor")){
                if(leaveFlag!=null){
                    Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class) ;
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
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

    public void onPopupMoreButtonClick(View view) {
        final PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.top_app_bar_group_homepage, popup.getMenu());
        Menu menu = popup.getMenu() ;
        menu.removeItem(R.id.remove);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Leave Group")){
                    leaveGroupOperation();
                }
                popup.dismiss();
                return true ;
            }
        });

        popup.show();
    }

    public void leaveGroupOperation(){
        myRefAddTutor.child(firebaseUser.getUid()).removeValue() ;
        myRefNotification.orderByChild("message3").equalTo(groupID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dS1: snapshot.getChildren()){
                    myRefNotification.child(dS1.getKey()).removeValue() ;
                    myRefNotification.removeEventListener(this);
                    leaveFlag = "true" ;
                }

                goToBackPageActivity(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
