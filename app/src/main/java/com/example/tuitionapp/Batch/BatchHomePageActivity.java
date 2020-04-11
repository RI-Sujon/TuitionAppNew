package com.example.tuitionapp.Batch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorHomePageActivity;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchHomePageActivity extends AppCompatActivity {

    private DatabaseReference myRefBatchInfo ;
    private String user ;
    private ArrayList<String>userInfo ;

    private EditText [] scheduleEditText ;
    private EditText batchName, payment, noOfAvailableSeat ;

    private String groupID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_home_page);
        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;
        groupID = intent.getStringExtra("groupID") ;

        myRefBatchInfo = FirebaseDatabase.getInstance().getReference("Batch") ;

        /*myRefBatchInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int flag = 0 ;
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    BatchInfo batchInfo = dS1.getValue(BatchInfo.class) ;
                    if(groupInfo.groupAdminEmail.equals(userEmail)){
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
        });*/
    }


    public void newBatchCreation(View view){
        LinearLayout batchCreationLayout = findViewById(R.id.batchCreation) ;
        LinearLayout homePageLayout = findViewById(R.id.batchHomePage) ;
        batchName = findViewById(R.id.batchName) ;
        noOfAvailableSeat = findViewById(R.id.numberOfAvailableSeat) ;
        payment = findViewById(R.id.payment) ;

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

        batchCreationLayout.setVisibility(View.VISIBLE);
        homePageLayout.setVisibility(View.GONE);
    }

    public void createBatchCompletion(View view){
        String batchNameStr, paymentStr, noOfSeatAvailability ;
        ArrayList<String> schedule = new ArrayList<>() ;
        ArrayList<BatchScheduleInfo> batchScheduleInfoArrayList = new ArrayList<>();

        batchNameStr = batchName.getText().toString() ;
        paymentStr = payment.getText().toString() ;
        noOfSeatAvailability = noOfAvailableSeat.getText().toString() ;

        int flag = 0 ;
        for(int i=1 ; i<8 ; i++){
            schedule.add(scheduleEditText[i].getText().toString()) ;
            if(!scheduleEditText[i].getText().toString().equals("")){
                flag = 1 ;
            }
        }

        if(flag==1){
            String time = scheduleEditText[0].getText().toString() ;
            BatchScheduleInfo batchScheduleInfo = new BatchScheduleInfo(time,schedule.get(0), schedule.get(1), schedule.get(2), schedule.get(3), schedule.get(4), schedule.get(5), schedule.get(6)) ;
            batchScheduleInfoArrayList.add(batchScheduleInfo) ;
        }

        flag = 0 ;
        for(int i=9 ; i<16 ; i++){
            schedule.add(scheduleEditText[i].getText().toString()) ;
            if(!scheduleEditText[i].getText().toString().equals("")){
                flag = 1 ;
            }
        }

        if(flag==1){
            String time = scheduleEditText[8].getText().toString() ;
            BatchScheduleInfo batchScheduleInfo = new BatchScheduleInfo(time,schedule.get(0), schedule.get(1), schedule.get(2), schedule.get(3), schedule.get(4), schedule.get(5), schedule.get(6)) ;
            batchScheduleInfoArrayList.add(batchScheduleInfo) ;
        }

        flag = 0 ;
        for(int i=17 ; i<24 ; i++){
            schedule.add(scheduleEditText[i].getText().toString()) ;
            if(!scheduleEditText[i].getText().toString().equals("")){
                flag = 1 ;
            }
        }

        if(flag==1){
            String time = scheduleEditText[16].getText().toString() ;
            BatchScheduleInfo batchScheduleInfo = new BatchScheduleInfo(time,schedule.get(0), schedule.get(1), schedule.get(2), schedule.get(3), schedule.get(4), schedule.get(5), schedule.get(6)) ;
            batchScheduleInfoArrayList.add(batchScheduleInfo) ;
        }

        BatchInfo batchInfo = new BatchInfo(batchNameStr, paymentStr, noOfSeatAvailability, batchScheduleInfoArrayList , groupID) ;
        myRefBatchInfo.push().setValue(batchInfo) ;
    }

    public void goToBatchManagement(View view){

    }

    public void goToBackPageActivity(View view){
        if(user.equals("user")){
            Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
            intent.putStringArrayListExtra("userInfo",userInfo) ;
            startActivity(intent);
            finish();
        }
    }
}
