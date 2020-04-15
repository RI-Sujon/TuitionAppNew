package com.example.tuitionappv2.batch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tuitionappv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchStudentInfoTableActivity extends AppCompatActivity {
    DatabaseReference myRefStudentInfo, myRefStudentInfo1, myRefStudentInfo2 ;

    private ArrayList<StudentInfo>studentInfoArrayList ;

    TableLayout tableLayout ;
    TableRow[] tableRows = new TableRow[21] ;
    EditText[] editTexts = new EditText[80] ;
    TextView[] textViews = new TextView[80] ;
    TextView[] textViewHeading = new TextView[4] ;

    private String user, groupID, batchID ;
    private ArrayList<String>userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_student_info_table);
        Intent intent = getIntent() ;
        batchID = intent.getStringExtra("batchID") ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user");

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
                            System.out.println("llllllllllllllllllll: " + dataSnapshot.getKey());
                            if(dataSnapshot.exists()){
                                for(DataSnapshot dS1:dataSnapshot.getChildren()){
                                    System.out.println("ppppppppppppppppppppppppppppppp: " + dS1.getKey());
                                    StudentInfo studentInfo = dS1.getValue(StudentInfo.class);
                                    System.out.println("ppkkkkkkkkkkkkkkkkkkpp: " + studentInfo.getStudentName());
                                    studentInfoArrayList.add(studentInfo) ;
                                }
                                createStudentTable();
                            }
                            else createNewStudentTable();
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
        textViews[2].setBackgroundColor(Color.BLUE) ;
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
                    textViews[k].setBackgroundColor(Color.GREEN);
                    textViews[k].setText(studentInfoArrayList.get(i-1).getStudentName());
                    textViews[k].setFocusable(false);
                }
                else if(j==2){
                    textViews[k].setBackgroundColor(Color.CYAN);
                    textViews[k].setText(studentInfoArrayList.get(i-1).getStudentAddress());
                    textViews[k].setFocusable(false);
                }
                else if(j==3){
                    textViews[k].setBackgroundColor(Color.GREEN);
                    textViews[k].setText(studentInfoArrayList.get(i-1).getStudentMobileNumber());
                    textViews[k].setFocusable(false);
                }
                textViews[k].setTextSize(13);
                tableRows[i].addView(textViews[k]);
            }
            tableRows[i].setLayoutParams(layoutParams);

            tableLayout.addView(tableRows[i]);
        }

        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
    }

    public void createNewStudentTable(){
        Button createButton = findViewById(R.id.createStudentInfoButton) ;
        createButton.setVisibility(View.VISIBLE);
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
        textViews[2].setBackgroundColor(Color.BLUE) ;
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
                    editTexts[k].setBackgroundColor(Color.GREEN);
                }
                else if(j==2){
                    editTexts[k].setBackgroundColor(Color.CYAN);
                }
                else if(j==3){
                    editTexts[k].setBackgroundColor(Color.GREEN);
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

    public void createStudentInfoTableCompletion(View view){
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
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, BatchViewInfoActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("batchID", batchID) ;
        intent.putExtra("user", user) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
