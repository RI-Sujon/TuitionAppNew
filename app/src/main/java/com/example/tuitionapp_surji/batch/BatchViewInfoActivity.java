package com.example.tuitionapp_surji.batch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchViewInfoActivity extends AppCompatActivity {

    private DatabaseReference myRefBatchInfo ;

    private ArrayList<BatchScheduleInfo> batchScheduleInfoArrayList ;

    private TextView batchNameView ;
    private TextView noOfAvailableSeatView, paymentView, extraInfo;
    private TextView [] scheduleEditText ;
    private Button studentInfoButton ;

    private BatchInfo batchInfo ;

    private ArrayList<String>userInfo ;
    private String batchID, user , groupID , groupName, groupAddress ;

    private MaterialToolbar materialToolbar ;
    private Menu toolbarMenu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_view_info);

        Intent intent = getIntent() ;
        batchID = intent.getStringExtra("batchID") ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user") ;
        groupName = intent.getStringExtra("groupName") ;
        groupAddress = intent.getStringExtra("groupAddress") ;

        studentInfoButton = findViewById(R.id.studentInfoButton) ;
        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setTitle(groupName);
        materialToolbar.setSubtitle(groupAddress);

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            studentInfoButton.setVisibility(View.VISIBLE);
        }

        myRefBatchInfo = FirebaseDatabase.getInstance().getReference("Batch").child(batchID) ;

        myRefBatchInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                batchInfo = dataSnapshot.getValue(BatchInfo.class) ;
                myRefBatchInfo.removeEventListener(this);
                viewBatchInformation() ;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPageActivity();
            }
        });

    }

    protected void onStart() {
        super.onStart();

        batchScheduleInfoArrayList = new ArrayList<>() ;

        batchNameView = findViewById(R.id.batchName) ;
        noOfAvailableSeatView = findViewById(R.id.no_of_available_seat) ;
        paymentView = findViewById(R.id.payment) ;
        extraInfo = findViewById(R.id.extraInfo) ;

        scheduleEditText = new TextView[32] ;

        scheduleEditText[0] = findViewById(R.id.time1) ;
        scheduleEditText[1] = findViewById(R.id.T11) ;
        scheduleEditText[2] = findViewById(R.id.T12) ;
        scheduleEditText[3] = findViewById(R.id.T13) ;
        scheduleEditText[4] = findViewById(R.id.T14) ;
        scheduleEditText[5] = findViewById(R.id.T15) ;
        scheduleEditText[6] = findViewById(R.id.T16) ;
        scheduleEditText[7] = findViewById(R.id.T17) ;
        scheduleEditText[8] = findViewById(R.id.time2) ;
        scheduleEditText[9] = findViewById(R.id.T21) ;
        scheduleEditText[10] = findViewById(R.id.T22) ;
        scheduleEditText[11] = findViewById(R.id.T23) ;
        scheduleEditText[12] = findViewById(R.id.T24) ;
        scheduleEditText[13] = findViewById(R.id.T25) ;
        scheduleEditText[14] = findViewById(R.id.T26) ;
        scheduleEditText[15] = findViewById(R.id.T27) ;
        scheduleEditText[16] = findViewById(R.id.time3) ;
        scheduleEditText[17] = findViewById(R.id.T31) ;
        scheduleEditText[18] = findViewById(R.id.T32) ;
        scheduleEditText[19] = findViewById(R.id.T33) ;
        scheduleEditText[20] = findViewById(R.id.T34) ;
        scheduleEditText[21] = findViewById(R.id.T35) ;
        scheduleEditText[22] = findViewById(R.id.T36) ;
        scheduleEditText[23] = findViewById(R.id.T37) ;


        toolbarMenu = materialToolbar.getMenu() ;
        toolbarMenu.findItem(R.id.create_batch).setVisible(false);

        if(user.equals("guardian")){
            toolbarMenu.findItem(R.id.edit_info).setVisible(false);
        }

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_info:
                        editBatchInformation();
                        break;
                }
                return true;
            }
        });
    }

    public void viewBatchInformation(){
        batchNameView.setText( batchInfo.getBatchName());
        noOfAvailableSeatView.setText(batchInfo.getNumberOfAvailableSeat());
        paymentView.setText(batchInfo.getPayment());
        if(batchInfo.getExtraInfo()!=null){
            extraInfo.setText(batchInfo.getExtraInfo());
        }

        batchScheduleInfoArrayList = batchInfo.getBatchScheduleInfoList() ;

        int i=0 ;
        if(!batchScheduleInfoArrayList.isEmpty()){
            for(BatchScheduleInfo batchScheduleInfo : batchScheduleInfoArrayList){
                scheduleEditText[i].setText(batchScheduleInfo.getTime());
                scheduleEditText[i+1].setText(batchScheduleInfo.getSaturdaySubject());
                scheduleEditText[i+2].setText(batchScheduleInfo.getSundaySubject());
                scheduleEditText[i+3].setText(batchScheduleInfo.getMondaySubject());
                scheduleEditText[i+4].setText(batchScheduleInfo.getTuesdaySubject());
                scheduleEditText[i+5].setText(batchScheduleInfo.getWednesdaySubject());
                scheduleEditText[i+6].setText(batchScheduleInfo.getThursdaySubject());
                scheduleEditText[i+7].setText(batchScheduleInfo.getFridaySubject());
                i = i + 8 ;
            }
        }
    }

    public void editBatchInformation(){
        Intent intent = new Intent(this, BatchCreationActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("batchID",batchID) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID" , groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress" , groupAddress) ;
        intent.putExtra("type", "edit") ;

        startActivity(intent);
        finish();
    }

    public void goToAddStudentInfo(View view){
        Intent intent = new Intent(this, BatchStudentInfoTableActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("batchID",batchID) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID" , groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress" , groupAddress) ;
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("user", user) ;
        intent.putExtra("groupID", groupID) ;
        intent.putExtra("viewType", "batchView") ;
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed(){
        goToBackPageActivity();
    }

}

