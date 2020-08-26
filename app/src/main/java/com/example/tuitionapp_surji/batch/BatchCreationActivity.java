package com.example.tuitionapp_surji.batch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchCreationActivity extends AppCompatActivity {
    private DatabaseReference myRefBatchInfo ;

    private Button createNewBatchButton;

    private EditText [] scheduleEditText ;
    private TextInputEditText batchNameEditText, paymentEditText, noOfAvailableSeatEditText, extraInfoEditText ;

    private String batchNameStr, paymentStr, noOfSeatAvailability, extraInfo ;
    private ArrayList<String> schedule ;

    private String user, userEmail, groupID, type, batchID, groupName, groupAddress;
    private ArrayList<String> userInfo ;
    private ArrayList<BatchScheduleInfo> batchScheduleInfoArrayList ;

    private BatchInfo batchInfo ;

    private MaterialToolbar materialToolbar ;
    private Menu toolbarMenu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_creation);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;
        groupID = intent.getStringExtra("groupID") ;
        type = intent.getStringExtra("type") ;

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
        }
        else{
            userEmail = intent.getStringExtra("userEmail") ;
        }

        myRefBatchInfo = FirebaseDatabase.getInstance().getReference("Batch");

        createNewBatchButton = findViewById(R.id.createBatchButton) ;

        if(type!=null){
            batchID = intent.getStringExtra("batchID") ;
            groupName = intent.getStringExtra("groupName") ;
            groupAddress = intent.getStringExtra("groupAddress") ;
            createNewBatchButton.setText("UPDATE BATCH");
            myRefBatchInfo = myRefBatchInfo.child(batchID) ;
            editBatchOperation(batchID);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        schedule = new ArrayList<>() ;

        batchNameEditText = findViewById(R.id.batchNameEditText) ;
        noOfAvailableSeatEditText = findViewById(R.id.no_of_available_seat) ;
        paymentEditText = findViewById(R.id.payment) ;
        extraInfoEditText = findViewById(R.id.extra) ;

        scheduleEditText = new EditText[32] ;

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

        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==null){
                    goToBackPageActivity();
                }else {
                    goToBatchInfoActivity();
                }
            }
        });

        toolbarMenu = materialToolbar.getMenu() ;
        toolbarMenu.findItem(R.id.edit_info).setVisible(false);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.create_batch:
                        createBatchCompletion(null);
                        break;
                }
                return true;
            }
        });
    }

    public void createBatchCompletion(View view){
        ArrayList<BatchScheduleInfo> batchScheduleInfoArrayList = new ArrayList<>();

        batchNameStr = batchNameEditText.getText().toString().trim();
        paymentStr = paymentEditText.getText().toString().trim() ;
        noOfSeatAvailability = noOfAvailableSeatEditText.getText().toString() ;
        extraInfo = extraInfoEditText.getText().toString() ;

        if(batchNameStr.equals("")){
            batchNameEditText.setError("this should not be empty");
            return;
        }

        int flag = 0 ;
        for(int i=1 ; i<8 ; i++){
            schedule.add(scheduleEditText[i].getText().toString()) ;
            if(!scheduleEditText[i].getText().toString().equals("")){
                flag = 1 ;
            }
        }

        String time = scheduleEditText[0].getText().toString() ;
        BatchScheduleInfo batchScheduleInfo = new BatchScheduleInfo(time,schedule.get(0), schedule.get(1), schedule.get(2), schedule.get(3), schedule.get(4), schedule.get(5), schedule.get(6)) ;
        batchScheduleInfoArrayList.add(batchScheduleInfo) ;
        schedule.clear();

        flag = 0 ;
        for(int i=9 ; i<16 ; i++){
            schedule.add(scheduleEditText[i].getText().toString()) ;
            if(!scheduleEditText[i].getText().toString().equals("")){
                flag = 1 ;
            }
        }

        if(flag==1){
            time = scheduleEditText[8].getText().toString() ;
            batchScheduleInfo = new BatchScheduleInfo(time,schedule.get(0), schedule.get(1), schedule.get(2), schedule.get(3), schedule.get(4), schedule.get(5), schedule.get(6)) ;
            batchScheduleInfoArrayList.add(batchScheduleInfo) ;
        }

        schedule.clear();
        flag = 0 ;
        for(int i=17 ; i<24 ; i++){
            schedule.add(scheduleEditText[i].getText().toString()) ;
            if(!scheduleEditText[i].getText().toString().equals("")){
                flag = 1 ;
            }
        }

        if(flag==1){
            time = scheduleEditText[16].getText().toString() ;
            batchScheduleInfo = new BatchScheduleInfo(time,schedule.get(0), schedule.get(1), schedule.get(2), schedule.get(3), schedule.get(4), schedule.get(5), schedule.get(6)) ;
            batchScheduleInfoArrayList.add(batchScheduleInfo) ;
        }

        BatchInfo batchInfo = new BatchInfo(batchNameStr, noOfSeatAvailability, paymentStr, extraInfo, batchScheduleInfoArrayList , groupID) ;


        if(type==null){
            myRefBatchInfo.push().setValue(batchInfo) ;
            goToBackPageActivity();
        }
        else {
            myRefBatchInfo.setValue(batchInfo) ;
            goToBatchInfoActivity();
        }
    }

    public void editBatchOperation(String batchID){
        //myRefBatchInfo = myRefBatchInfo.child(batchID) ;

        myRefBatchInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                batchInfo = dataSnapshot.getValue(BatchInfo.class) ;
                myRefBatchInfo.removeEventListener(this);
                editBatchOperationStep2(); ;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void editBatchOperationStep2(){
        batchNameEditText.setText( batchInfo.getBatchName());
        noOfAvailableSeatEditText.setText(batchInfo.getNumberOfAvailableSeat());
        paymentEditText.setText(batchInfo.getPayment());
        if(batchInfo.getExtraInfo()!=null){
            extraInfoEditText.setText(batchInfo.getExtraInfo());
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

    public void goToBackPageActivity(){

        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("groupID",groupID) ;
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    public void goToBatchInfoActivity(){
        Intent intent = new Intent(this, BatchViewInfoActivity.class);
        intent.putExtra("groupID",groupID) ;
        intent.putExtra("groupName", groupName) ;
        intent.putExtra("groupAddress", groupAddress) ;
        intent.putExtra("batchID", batchID) ;
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        if(type==null){
            goToBackPageActivity();
        }else {
            goToBatchInfoActivity();
        }
    }
}
