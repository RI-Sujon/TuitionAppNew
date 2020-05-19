package com.example.tuitionapp_nadim.batch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tuitionapp_nadim.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchStudentInfoTableActivity extends AppCompatActivity {
    private DatabaseReference myRefStudentInfo, myRefStudentInfo1, myRefStudentInfo2 ;

    private ArrayList<StudentInfo>studentInfoArrayList ;

    private TableLayout tableLayout ;
    private TableRow[] tableRows = new TableRow[21] ;
    private EditText[] editTexts = new EditText[80] ;
    private TextView[] textViews = new TextView[80] ;
    private TextView[] textViewHeading = new TextView[4] ;

    private String user, groupID, batchID , groupName, groupAddress;
    private ArrayList<String>userInfo ;

    private MaterialToolbar materialToolbar ;
    private Menu toolbarMenu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_student_info_table);
        Intent intent = getIntent() ;
        batchID = intent.getStringExtra("batchID") ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user");
        groupName = intent.getStringExtra("groupName") ;
        groupAddress = intent.getStringExtra("groupAddress") ;

        if(user.equals("tutor")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
        }

        myRefStudentInfo = FirebaseDatabase.getInstance().getReference("StudentInfo").child(groupID);
        studentInfoArrayList = new ArrayList<>() ;

        tableLayout = findViewById(R.id.tableLayout) ;

        myRefStudentInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    myRefStudentInfo1 = myRefStudentInfo.child(batchID) ;
                    myRefStudentInfo1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot dS1:dataSnapshot.getChildren()){
                                    StudentInfo studentInfo = dS1.getValue(StudentInfo.class);
                                    studentInfoArrayList.add(studentInfo) ;
                                }
                                createStudentTable();
                            }
                            else {
                                createNewStudentTable();
                            }
                            myRefStudentInfo1.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }) ;
                }
                else createNewStudentTable();
                myRefStudentInfo.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPageActivity();
            }
        });

        toolbarMenu = materialToolbar.getMenu() ;
        toolbarMenu.findItem(R.id.create_table).setVisible(false);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.create_table:
                        createStudentInfoTableCompletion();
                        break;
                }

                return true;
            }
        });
    }

    public void createStudentTable(){
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT) ;
        layoutParams.bottomMargin = 2 ;
        for(int i=0 ; i<4 ; i++){
            textViews[i] = new TextView(this) ;
            textViews[i].setTextSize(12);
            textViews[i].setTextColor(Color.WHITE);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTypeface(null, Typeface.BOLD_ITALIC);
        }
        textViews[0].setText("SL NO");
        textViews[0].setBackgroundColor(Color.GRAY) ;
        textViews[1].setText("STUDENT NAME");
        textViews[1].setBackgroundColor(Color.rgb(0,80,0)) ;
        textViews[2].setText("STUDENT ADDRESS");
        textViews[2].setBackgroundColor(Color.rgb(94,53,177)) ;
        textViews[3].setText("PHONE NUMBER");
        textViews[3].setBackgroundColor(Color.rgb(0,80,0)) ;

        tableRows[0] = new TableRow(this) ;
        for(int i=0 ; i<4 ; i++){
            tableRows[0].addView(textViews[i]);
        }
        tableRows[0].setLayoutParams(layoutParams);
        tableLayout.addView(tableRows[0]);

        for(int i=1; i<=studentInfoArrayList.size() ; i++){
            tableRows[i] = new TableRow(this) ;
            for(int j=0 ; j<4 ; j++){
                int k = (i-1)*4+j;
                textViews[k] = new EditText(this) ;
                if(j==0){
                    textViews[k].setBackgroundColor(Color.WHITE);
                    textViews[k].setText(studentInfoArrayList.get(i-1).getSerialNo()) ;
                    textViews[k].setTypeface(null,Typeface.BOLD);
                    textViews[k].setGravity(Gravity.CENTER);
                    textViews[k].setFocusable(false);
                }
                else if(j==1){
                    textViews[k].setBackgroundColor(Color.rgb(30,136,229));
                    textViews[k].setText(studentInfoArrayList.get(i-1).getStudentName());
                    textViews[k].setFocusable(false);
                }
                else if(j==2){
                    textViews[k].setBackgroundColor(Color.rgb(144,202,249));
                    textViews[k].setText(studentInfoArrayList.get(i-1).getStudentAddress());
                    textViews[k].setFocusable(false);
                }
                else if(j==3){
                    textViews[k].setBackgroundColor(Color.rgb(30,136,229));
                    textViews[k].setText(studentInfoArrayList.get(i-1).getStudentMobileNumber());
                    textViews[k].setFocusable(false);
                }
                textViews[k].setTextSize(12);
                tableRows[i].addView(textViews[k]);
            }
            tableRows[i].setLayoutParams(layoutParams);

            tableLayout.addView(tableRows[i]);
        }

        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

    }

    public void createNewStudentTable(){

        toolbarMenu.findItem(R.id.create_table).setVisible(true);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT) ;
        layoutParams.bottomMargin = 2 ;
        for(int i=0 ; i<4 ; i++){
            textViews[i] = new TextView(this) ;
            textViews[i].setTextSize(12);
            textViews[i].setTextColor(Color.WHITE);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTypeface(null, Typeface.BOLD_ITALIC);
        }
        textViews[0].setText("SL NO");
        textViews[0].setBackgroundColor(Color.GRAY) ;
        textViews[1].setText("STUDENT NAME");
        textViews[1].setBackgroundColor(Color.rgb(0,80,0)) ;
        textViews[2].setText("STUDENT ADDRESS");
        textViews[2].setBackgroundColor(Color.rgb(0,100,0)) ;
        textViews[3].setText("PHONE NUMBER");
        textViews[3].setBackgroundColor(Color.rgb(0,80,0)) ;

        tableRows[0] = new TableRow(this) ;
        for(int i=0 ; i<4 ; i++){
            tableRows[0].addView(textViews[i]);
        }
        tableRows[0].setLayoutParams(layoutParams);
        tableLayout.addView(tableRows[0]);

        for(int i=1; i<21 ; i++){
            tableRows[i] = new TableRow(this) ;
            for(int j=0 ; j<4 ; j++){
                int k = (i-1)*4+j;
                editTexts[k] = new EditText(this) ;
                if(j==0){
                    editTexts[k].setBackgroundColor(Color.WHITE);
                    editTexts[k].setText(String.valueOf(i));
                    editTexts[k].setTypeface(null,Typeface.BOLD);
                    editTexts[k].setGravity(Gravity.CENTER);
                }
                else if(j==1){
                    editTexts[k].setBackgroundColor(Color.rgb(75,163,239));
                }
                else if(j==2){
                    editTexts[k].setBackgroundColor(Color.rgb(144,202,249));
                }
                else if(j==3){
                    editTexts[k].setBackgroundColor(Color.rgb(75,163,239));
                }
                editTexts[k].setTextSize(13);
                tableRows[i].addView(editTexts[k]);
            }
            tableRows[i].setLayoutParams(layoutParams);

            tableLayout.addView(tableRows[i]);
        }

        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
    }

    public void createStudentInfoTableCompletion(){
        myRefStudentInfo2 = myRefStudentInfo.child(batchID) ;
        for(int i=0; i<20 ; i++){
            String sL = editTexts[i*4+0].getText().toString() ;
            String name = editTexts[i*4+1].getText().toString() ;
            String address = editTexts[i*4+2].getText().toString() ;
            String phoneNumber = editTexts[i*4+3].getText().toString() ;

            if(name.equals("")&&address.equals("")&&phoneNumber.equals("")) continue;

            StudentInfo studentInfo = new StudentInfo(sL, name, address, phoneNumber) ;

            myRefStudentInfo2.push().setValue(studentInfo);
        }

        Intent intent = new Intent(this, BatchStudentInfoTableActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("batchID",batchID);
        intent.putExtra("user", user) ;
        intent.putExtra("groupID", groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress" , groupAddress) ;
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(){
        Intent intent = new Intent(this, BatchViewInfoActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("batchID", batchID) ;
        intent.putExtra("user", user) ;
        intent.putExtra("groupID", groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress" , groupAddress) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity();
    }
}
