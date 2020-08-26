package com.example.tuitionapp_surji.batch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
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
    private EditText[] editTextHeading = new EditText[4] ;

    private String user, groupID, batchID , groupName, groupAddress, type;
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
        type = intent.getStringExtra("type") ;

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
                                if(type==null) {
                                    createStudentTable();
                                }
                                else if(type.equals("edit")){
                                    editTableOperation();
                                }
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

                    case R.id.editTable:
                        goForEditTable();
                        break;

                }

                return true;
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
    }

    public void createStudentTable(){
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT) ;
        TableRow.LayoutParams layoutForTextViews = new TableRow.LayoutParams(20, TableRow.LayoutParams.MATCH_PARENT);

        layoutParams.bottomMargin = 2 ;
        for(int i=0 ; i<4 ; i++){
            textViewHeading[i] = new TextView(this) ;
            textViewHeading[i].setTextSize(12);
            textViewHeading[i].setTextColor(Color.WHITE);
            textViewHeading[i].setGravity(Gravity.CENTER);
            textViewHeading[i].setTypeface(null, Typeface.BOLD_ITALIC);

        }
        textViewHeading[0].setText(studentInfoArrayList.get(0).getSerialNo());
        textViewHeading[0].setBackgroundColor(Color.GRAY) ;
        textViewHeading[1].setText(studentInfoArrayList.get(0).getColumn1());
        textViewHeading[1].setBackgroundColor(Color.rgb(0,80,0)) ;
        textViewHeading[2].setText(studentInfoArrayList.get(0).getColumn2());
        textViewHeading[2].setBackgroundColor(Color.rgb(0,100,0)) ;
        textViewHeading[3].setText(studentInfoArrayList.get(0).getColumn3());
        textViewHeading[3].setBackgroundColor(Color.rgb(0,80,0)) ;

        tableRows[0] = new TableRow(this) ;
        for(int i=0 ; i<4 ; i++){
            tableRows[0].addView(textViewHeading[i]);
        }
        tableRows[0].setLayoutParams(layoutParams);
        tableLayout.addView(tableRows[0]);

        for(int i=0; i<studentInfoArrayList.size()-1 ; i++){
            tableRows[i] = new TableRow(this) ;
            for(int j=0 ; j<4 ; j++){
                int k = i*4+j;
                textViews[k] = new EditText(this) ;
                if(j==0){
                    textViews[k].setBackgroundColor(Color.WHITE);
                    textViews[k].setText(studentInfoArrayList.get(i+1).getSerialNo()) ;
                    textViews[k].setTypeface(null,Typeface.BOLD);
                    textViews[k].setGravity(Gravity.CENTER);
                    textViews[k].setFocusable(false);
                }
                else if(j==1){
                    textViews[k].setBackgroundColor(Color.rgb(30,136,229));
                    textViews[k].setText(studentInfoArrayList.get(i+1).getColumn1());
                    textViews[k].setFocusable(false);
                }
                else if(j==2){
                    textViews[k].setBackgroundColor(Color.rgb(144,202,249));
                    textViews[k].setText(studentInfoArrayList.get(i+1).getColumn2());
                    textViews[k].setFocusable(false);
                }
                else if(j==3){
                    textViews[k].setBackgroundColor(Color.rgb(30,136,229));
                    textViews[k].setText(studentInfoArrayList.get(i+1).getColumn3());
                    textViews[k].setFocusable(false);
                }
                textViews[k].setTextSize(12);
                textViews[k].setLayoutParams(layoutForTextViews);
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
        toolbarMenu.findItem(R.id.editTable).setVisible(false);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT) ;
        TableRow.LayoutParams layoutForEditText = new TableRow.LayoutParams(20,
                TableRow.LayoutParams.MATCH_PARENT);

        layoutParams.bottomMargin = 2 ;

        InputFilter [] filter = new InputFilter[1] ;
        filter[0] = new InputFilter.LengthFilter(20) ;

        InputFilter [] filter2 = new InputFilter[1] ;
        filter2[0] = new InputFilter.LengthFilter(35) ;


        for(int i=0 ; i<4 ; i++){
            editTextHeading[i] = new EditText(this) ;
            editTextHeading[i].setTextSize(12);
            editTextHeading[i].setTextColor(Color.WHITE);
            editTextHeading[i].setGravity(Gravity.CENTER);
            editTextHeading[i].setTypeface(null, Typeface.BOLD_ITALIC);
            editTextHeading[i].setMaxLines(2);
        }
        editTextHeading[0].setText("SL");
        editTextHeading[0].setBackgroundColor(Color.GRAY) ;

        editTextHeading[1].setText("STUDENT NAME");
        editTextHeading[1].setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editTextHeading[1].setBackgroundColor(Color.rgb(0,80,0)) ;
        editTextHeading[1].setFilters(filter);

        editTextHeading[2].setText("STUDENT ADDRESS");
        editTextHeading[2].setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editTextHeading[2].setBackgroundColor(Color.rgb(0,100,0)) ;
        editTextHeading[2].setFilters(filter);

        editTextHeading[3].setText("PHONE NUMBER");
        editTextHeading[3].setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editTextHeading[3].setBackgroundColor(Color.rgb(0,80,0)) ;
        editTextHeading[3].setFilters(filter);

        tableRows[0] = new TableRow(this) ;
        for(int i=0 ; i<4 ; i++){
            tableRows[0].addView(editTextHeading[i]);
        }
        tableRows[0].setLayoutParams(layoutParams);
        tableLayout.addView(tableRows[0]);

        for(int i=1; i<21 ; i++){
            tableRows[i] = new TableRow(this) ;
            for(int j=0 ; j<4 ; j++){
                int k = (i-1)*4+j;
                editTexts[k] = new EditText(this) ;

                editTexts[k].setLayoutParams(layoutForEditText);

                if(j==0){
                    editTexts[k].setBackgroundColor(Color.WHITE);
                    editTexts[k].setText(String.valueOf(i));
                    editTexts[k].setTypeface(null,Typeface.BOLD);
                    editTexts[k].setGravity(Gravity.CENTER);
                    editTexts[k].setPadding(5,25,5,25);
                    editTexts[k].setEnabled(false);
                }
                else if(j==1){
                    editTexts[k].setBackgroundColor(Color.rgb(75,163,239));
                    editTexts[k].setPadding(5,5,5,5);
                }
                else if(j==2){
                    editTexts[k].setBackgroundColor(Color.rgb(144,202,249));
                    editTexts[k].setPadding(5,5,5,5);
                }
                else if(j==3){
                    editTexts[k].setBackgroundColor(Color.rgb(75,143,239));
                    editTexts[k].setPadding(5,5,5,5);
                }
                editTexts[k].setTextSize(13);
                editTexts[k].setMaxLines(2);
                editTexts[k].setFilters(filter2);
                tableRows[i].addView(editTexts[k]);
            }
            tableRows[i].setLayoutParams(layoutParams);

            tableLayout.addView(tableRows[i]);
        }

        tableLayout.setStretchAllColumns(true);
        //tableLayout.setShrinkAllColumns(true);
    }

    public void operationForEditTable(){
        editTextHeading[0].setText(studentInfoArrayList.get(0).getSerialNo());
        editTextHeading[1].setText(studentInfoArrayList.get(0).getColumn1());
        editTextHeading[2].setText(studentInfoArrayList.get(0).getColumn2());
        editTextHeading[3].setText(studentInfoArrayList.get(0).getColumn3());

        int studentInfoArrayIndex = 1 ;

        for(int i=0; studentInfoArrayIndex<studentInfoArrayList.size() ; i++){
            for(int j=0 ; j<4 ; j++){
                int k = i*4+j;

                if(j==0 && studentInfoArrayList.get(studentInfoArrayIndex).getSerialNo().equals(String.valueOf(i+1))){
                    editTexts[k].setText(studentInfoArrayList.get(studentInfoArrayIndex).getSerialNo()) ;
                    studentInfoArrayIndex++ ;
                }else if(j==0){
                    break;
                }

                if(j==1){
                    editTexts[k].setText(studentInfoArrayList.get(studentInfoArrayIndex-1).getColumn1());
                }
                else if(j==2){
                    editTexts[k].setText(studentInfoArrayList.get(studentInfoArrayIndex-1).getColumn2());
                }
                else if(j==3){
                    editTexts[k].setText(studentInfoArrayList.get(studentInfoArrayIndex-1).getColumn3());
                }
            }
        }
    }

    public void createStudentInfoTableCompletion(){
        myRefStudentInfo2 = myRefStudentInfo.child(batchID) ;

        ArrayList<StudentInfo> studentInfoArrayList = new ArrayList<>() ;

        StudentInfo studentInfo = new StudentInfo(editTextHeading[0].getText().toString(), editTextHeading[1].getText().toString(), editTextHeading[2].getText().toString(), editTextHeading[3].getText().toString()) ;

        studentInfoArrayList.add(studentInfo) ;
        //myRefStudentInfo2.push().setValue(studentInfo);

        for(int i=0; i<20 ; i++){
            String sL = editTexts[i*4+0].getText().toString() ;
            String name = editTexts[i*4+1].getText().toString() ;
            String address = editTexts[i*4+2].getText().toString() ;
            String phoneNumber = editTexts[i*4+3].getText().toString() ;

            if(name.equals("")&&address.equals("")&&phoneNumber.equals("")) continue;

            studentInfo = new StudentInfo(sL, name, address, phoneNumber) ;
            studentInfoArrayList.add(studentInfo) ;
            //myRefStudentInfo2.push().setValue(studentInfo);
        }

        myRefStudentInfo2.setValue(studentInfoArrayList) ;

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

    public void editTableOperation(){
        createNewStudentTable();
        operationForEditTable();
    }

    public void goForEditTable(){
        Intent intent = new Intent(this, BatchStudentInfoTableActivity.class);
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("batchID", batchID) ;
        intent.putExtra("user", user) ;
        intent.putExtra("groupID", groupID) ;
        intent.putExtra("groupName" , groupName) ;
        intent.putExtra("groupAddress" , groupAddress) ;
        intent.putExtra("type", "edit") ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity();
    }
}
