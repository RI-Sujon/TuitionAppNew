package com.example.tuitionappv2.batch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tuitionappv2.group.GroupHomePageActivity;
import com.example.tuitionappv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchCreateAndSelectActivity extends AppCompatActivity {

    private DatabaseReference myRefBatchInfo ;

    private ArrayList<BatchInfo> batchInfoArrayList ;
    private ArrayList<String> batchIDArrayList ;

    private CustomAdapterForSelectAndViewBatch adapter ;

    private ListView batchListView ;
    private Button createNewBatchButton, createNewBatchOptionButton;
    private EditText [] scheduleEditText ;
    private EditText batchNameEditText, paymentEditText, noOfAvailableSeatEditText ;
    private LinearLayout batchCreationLayout, homePageLayout ;


    private String user, userEmail, groupID, batchID;
    private ArrayList<String> userInfo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_create_and_select);
        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;
        groupID = intent.getStringExtra("groupID") ;

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
        }
        else{
            userEmail = intent.getStringExtra("userEmail") ;
        }

        myRefBatchInfo = FirebaseDatabase.getInstance().getReference("Batch");

        batchListView = findViewById(R.id.batchList) ;
        createNewBatchButton = findViewById(R.id.createBatchButton) ;
        createNewBatchOptionButton = findViewById(R.id.createNewBatchButton) ;

        batchInfoArrayList = new ArrayList<>() ;
        batchIDArrayList = new ArrayList<>() ;

        batchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BatchInfo newBatchInfo = batchInfoArrayList.get(position) ;
                batchID = batchIDArrayList.get(position) ;
                goToViewBatchInfo(newBatchInfo) ;
            }
        });

        myRefBatchInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag = 0 ;
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    BatchInfo batchInfo = dS1.getValue(BatchInfo.class) ;
                    if(batchInfo.getGroupIDFK().equals(groupID)){
                        batchInfoArrayList.add(batchInfo) ;
                        batchIDArrayList.add(dS1.getKey()) ;
                    }
                    goToBatchListView();
                }

                myRefBatchInfo.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        if(user.equals("guardian")||user.equals("admin")){
            createNewBatchOptionButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        batchCreationLayout = findViewById(R.id.batchCreation) ;
        homePageLayout = findViewById(R.id.batchHomePage) ;
        batchNameEditText = findViewById(R.id.batchName) ;
        noOfAvailableSeatEditText = findViewById(R.id.numberOfAvailableSeat) ;
        paymentEditText = findViewById(R.id.payment) ;

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
    }

    public void newBatchCreation(View view){
        batchCreationLayout.setVisibility(View.VISIBLE);
        homePageLayout.setVisibility(View.GONE);
    }

    public void createBatchCompletion(View view){
        String batchNameStr, paymentStr, noOfSeatAvailability ;
        ArrayList<String> schedule = new ArrayList<>() ;
        ArrayList<BatchScheduleInfo> batchScheduleInfoArrayList = new ArrayList<>();

        batchNameStr = batchNameEditText.getText().toString().trim();
        paymentStr = paymentEditText.getText().toString().trim() ;
        noOfSeatAvailability = noOfAvailableSeatEditText.getText().toString() ;

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

        BatchInfo batchInfo = new BatchInfo(batchNameStr, noOfSeatAvailability, paymentStr, batchScheduleInfoArrayList , groupID) ;
        myRefBatchInfo.push().setValue(batchInfo) ;
        Intent intent = new Intent(this, BatchCreateAndSelectActivity.class);

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;
        startActivity(intent);
        finish();
    }

    public void goToBatchListView(){
        CustomAdapterForSelectAndViewBatch adapter = new CustomAdapterForSelectAndViewBatch(this,batchInfoArrayList) ;
        batchListView.setAdapter(adapter);
    }

    public void goToViewBatchInfo(BatchInfo batchInfo){
        Intent intent = new Intent(this, BatchViewInfoActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("batchID",batchID) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID" , groupID) ;
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("groupID",groupID) ;
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
