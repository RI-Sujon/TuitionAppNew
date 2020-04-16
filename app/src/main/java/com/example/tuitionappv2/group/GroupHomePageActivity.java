package com.example.tuitionappv2.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionappv2.admin.BlockInfo;
import com.example.tuitionappv2.batch.BatchCreateAndSelectActivity;
import com.example.tuitionappv2.guardian.ViewingSearchingTutorProfileActivity;
import com.example.tuitionappv2.message_box.MessageBoxInfo;
import com.example.tuitionappv2.notice_board.NoticeBoardViewAndCreateActivity;
import com.example.tuitionappv2.R;
import com.example.tuitionappv2.verified_tutor.ReportInfo;
import com.example.tuitionappv2.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupHomePageActivity extends AppCompatActivity {

    private DatabaseReference myRefGroupInfo, myRefMessageBox, myRefReport, myRefBlockInfo ;
    private FirebaseUser firebaseUser ;
    private String user, userEmail, groupID , tutorUid;
    private ArrayList<String>userInfo ;

    private Button messageRequestButton, reportButton , blockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home_page);
//        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;

        myRefGroupInfo = FirebaseDatabase.getInstance().getReference("Group") ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        if(user.equals("tutor"))
        {

            tutorUid=userInfo.get(3);
            userEmail = userInfo.get(2);
            myRefGroupInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int flag = 0 ;
                    for(DataSnapshot dS1: dataSnapshot.getChildren()){
                        GroupInfo groupInfo = dS1.getValue(GroupInfo.class) ;
                        if(groupInfo.getGroupAdminEmail().equals(userEmail)){
                            groupID = dS1.getKey() ;
                            groupHomePage(groupInfo);
                            flag = 1 ;
                            break ;
                        }

                    }
                    if(flag == 0){
                        goToGroupCreation();
                    }
                    myRefGroupInfo.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
        }

        else
        {
            groupID = intent.getStringExtra("groupID") ;
            myRefGroupInfo.child(groupID) ;

            myRefGroupInfo.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    GroupInfo groupInfo2 = dataSnapshot.getValue(GroupInfo.class) ;
                    groupHomePage(groupInfo2);
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

        if(user.equals("guardian")){
            tutorUid = intent.getStringExtra("tutorUid");
            LinearLayout downBar = findViewById(R.id.downBar) ;
            downBar.setVisibility(View.VISIBLE);
            messageRequestButton = findViewById(R.id.sendMessageRequestButton) ;
            //reportButton = findViewById(R.id.reportButton) ;
            messageRequestButton.setVisibility(View.VISIBLE);
            //reportButton.setVisibility(View.VISIBLE);
        }
        else if(user.equals("admin")){
            //LinearLayout downBar = findViewById(R.id.downBar) ;
            //downBar.setVisibility(View.VISIBLE);
            //blockButton = findViewById(R.id.blockGroupButton) ;
            //blockButton.setVisibility(View.VISIBLE);
        }
    }

    public void groupHomePage(GroupInfo groupInfo){
        LinearLayout layout = findViewById(R.id.groupHomePage) ;
        layout.setVisibility(View.VISIBLE);
        TextView groupNameTextView = findViewById(R.id.groupNameTextView) ;
        TextView fullAddressTextView = findViewById(R.id.fullAddressTextView) ;

        groupNameTextView.setText(groupInfo.getGroupName());
        fullAddressTextView.setText(groupInfo.getFullAddress() + ", " + groupInfo.getAddress());
    }

    public void goToGroupCreation()
    {
        LinearLayout groupCreationLayout = findViewById(R.id.groupCreation) ;
        final Spinner addressSpinner = findViewById(R.id.groupAddressSpinner) ;
        Button createGroupButton = findViewById(R.id.createGroupButton) ;

        groupCreationLayout.setVisibility(View.VISIBLE);

        final String[] addressString = new String[1];

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (addressSpinner.getSelectedItemPosition() == 0) {
                }
                else {
                    addressString[0] = addressSpinner.getSelectedItem().toString().trim();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupCreation(addressString[0]);
            }
        });

    }

    public void groupCreation(String addressString){
        EditText groupNameEditText = findViewById(R.id.groupName) ;
        EditText fullAddressEditText = findViewById(R.id.groupFullAddress) ;

        String groupName, fullAddress ;

        groupName = groupNameEditText.getText().toString().trim() ;
        fullAddress = fullAddressEditText.getText().toString().trim() ;

        if(!groupName.equals("")&&!addressString.equals("")&&!fullAddress.equals("")){
            GroupInfo groupInfo = new GroupInfo(groupName,addressString,fullAddress,userEmail,tutorUid) ;
            myRefGroupInfo.push().setValue(groupInfo) ;
            Toast.makeText(getApplicationContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,GroupHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            intent.putExtra("user" , user) ;
            startActivity(intent);
            finish();
        }
        else{

        }
    }

    public void sendMessageRequestByGuardian(View view){
        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;
        MessageBoxInfo messageBoxInfo = new MessageBoxInfo(firebaseUser.getPhoneNumber(),firebaseUser.getUid(),userEmail,tutorUid, false ,true) ;
        myRefMessageBox.push().setValue(messageBoxInfo) ;

        messageRequestButton.setEnabled(false);
        messageRequestButton.setBackgroundColor(Color.GRAY);

    }

    public void reportIDByGuardian(View view){
        myRefReport = FirebaseDatabase.getInstance().getReference("Report") ;
        ReportInfo reportInfo = new ReportInfo(firebaseUser.getPhoneNumber(),userEmail, "this is a fake account") ;
        myRefReport.push().setValue(reportInfo) ;

        reportButton.setEnabled(false);
        reportButton.setBackgroundColor(Color.GRAY);
    }

    public void blockGroupByAdmin(View view){
        myRefBlockInfo = FirebaseDatabase.getInstance().getReference("Block") ;
        BlockInfo blockInfo = new BlockInfo("tuitionApsspl02@gmail.com",userEmail,true) ;
        myRefBlockInfo.push().setValue(blockInfo) ;

        blockButton.setEnabled(false);
        blockButton.setBackgroundColor(Color.GRAY);
    }

    public void goToBatchManagement(View view){
        Intent intent = new Intent(this, BatchCreateAndSelectActivity.class) ;
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        startActivity(intent);
        finish();
    }

    public void goToGroupTutorManagement(View view){
        Intent intent = new Intent(this, GroupTutorViewActivity.class) ;
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        startActivity(intent);
        finish();
    }

    public void goToNoticeBoardManagement(View view){
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

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
