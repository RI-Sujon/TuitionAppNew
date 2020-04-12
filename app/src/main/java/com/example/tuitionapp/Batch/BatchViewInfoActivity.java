package com.example.tuitionapp.Batch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorHomePageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchViewInfoActivity extends AppCompatActivity {

    private DatabaseReference myRefBatchInfo ;

    ArrayList<BatchScheduleInfo> batchScheduleInfoArrayList ;

    private EditText batchNameView ;
    private TextView noOfAvailableSeatView, paymentView ;
    private EditText [] scheduleEditText ;

    BatchInfo batchInfo ;

    private ArrayList<String>userInfo ;
    private String batchID, user ,groupID, userEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_view_info);
        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        batchID = intent.getStringExtra("batchID") ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user");

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
        }else{
            userEmail = intent.getStringExtra("userEmail") ;
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
    }

    protected void onStart() {
        super.onStart();

        batchScheduleInfoArrayList = new ArrayList<>() ;

        batchNameView = findViewById(R.id.batchName) ;
        noOfAvailableSeatView = findViewById(R.id.noOfAvailableSeat) ;
        paymentView = findViewById(R.id.payment) ;

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

    public void viewBatchInformation(){
        batchNameView.setText("Batch Name:  " + batchInfo.getBatchName());
        noOfAvailableSeatView.setText("Number Of Seat Available:  " + batchInfo.getNumberOfAvailableSeat());
        paymentView.setText("Payment:  " + batchInfo.getPayment());

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

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, BatchCreateAndSelectActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        else{
            intent.putExtra("userEmail", userEmail) ;
        }
        intent.putExtra("user", user) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
    }

}

