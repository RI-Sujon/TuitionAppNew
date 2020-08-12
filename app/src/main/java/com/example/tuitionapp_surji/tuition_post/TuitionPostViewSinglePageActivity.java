package com.example.tuitionapp_surji.tuition_post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TuitionPostViewSinglePageActivity extends AppCompatActivity {

    private TextView postTitleTV, genderPreferableTV, genderPreferableNoticeTV, mediumTV, class_nameTV, groupTV, subjectTV, studentInstituteNameTV, addressTV, contactNoTV, daysPerWeekTV, salaryTV, extraInfoTV, postTimeTV ;
    private String postTitle, genderPreferable, medium, class_name, group, subject, studentInstituteName, address, contactNo, daysPerWeek, salary, extraInfo, date, time ;

    private ArrayList<String> tutorInfo ;
    private String guardianUid, user ;

    private MaterialButton responseButton ;

    private ImageView postImage ;

    private DatabaseReference myRefMessageBox ;
    private MessageBoxInfo messageBoxInfo ;

    private MaterialToolbar materialToolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post_view_single_page);

        Intent intent = getIntent() ;

        postTitleTV = findViewById(R.id.postTitle) ;
        genderPreferableTV = findViewById(R.id.tutorGenderPreferable) ;
        genderPreferableNoticeTV = findViewById(R.id.genderPreferableNotices) ;
        mediumTV = findViewById(R.id.medium) ;
        class_nameTV = findViewById(R.id.class_name) ;
        groupTV = findViewById(R.id.group) ;
        subjectTV = findViewById(R.id.subject) ;
        studentInstituteNameTV = findViewById(R.id.studentInstitute) ;
        addressTV = findViewById(R.id.location) ;
        contactNoTV = findViewById(R.id.contactNo) ;
        daysPerWeekTV = findViewById(R.id.days_per_week) ;
        salaryTV = findViewById(R.id.salary) ;
        extraInfoTV = findViewById(R.id.extraInfo) ;
        postTimeTV = findViewById(R.id.postTime) ;

        postImage = findViewById(R.id.postPic) ;
        responseButton = findViewById(R.id.responseButton) ;

        user = intent.getStringExtra("user") ;

        if(user.equals("tutor")){
            tutorInfo = intent.getStringArrayListExtra("tutorInfo") ;
            guardianUid = intent.getStringExtra("guardianUid") ;
        }
        else {
            responseButton.setVisibility(View.GONE);
        }

        contactNo = intent.getStringExtra("contactNo") ;


        postTitleTV.setText(intent.getStringExtra("postTitle"));
        genderPreferableTV.setText(intent.getStringExtra("tutorGenderPreferable"));
        mediumTV.setText(intent.getStringExtra("medium"));
        class_nameTV.setText(intent.getStringExtra("class_name"));
        groupTV.setText(intent.getStringExtra("group"));
        subjectTV.setText(intent.getStringExtra("subject"));
        studentInstituteNameTV.setText(intent.getStringExtra("studentInstituteName"));
        addressTV.setText(intent.getStringExtra("address"));
        contactNoTV.setText(intent.getStringExtra("contactNo"));
        daysPerWeekTV.setText(intent.getStringExtra("daysPerWeek"));
        salaryTV.setText(intent.getStringExtra("salary"));
        extraInfoTV.setText(intent.getStringExtra("extraInfo"));
        postTimeTV.setText(intent.getStringExtra("postTime"));

        if(intent.getStringExtra("tutorGenderPreferable").equals("Only Male")){
            genderPreferableNoticeTV.setText("*This post only for male tutor.");
        }
        else if(intent.getStringExtra("tutorGenderPreferable").equals("Only Female")){
            genderPreferableNoticeTV.setText("*This post only for female tutor.");
        }
        else genderPreferableNoticeTV.setVisibility(View.GONE);


        if(intent.getStringExtra("medium").equals("English Medium")){
            postImage.setImageResource(R.drawable.logo_english_medium);
        }
        else if(intent.getStringExtra("group").equals("Science")){
            postImage.setImageResource(R.drawable.logo_science);
        }
        else if(intent.getStringExtra("group").equals("Commerce")){
            postImage.setImageResource(R.drawable.logo_commerce);
        }
        else if(intent.getStringExtra("group").equals("Arts")){
            postImage.setImageResource(R.drawable.logo_humanities);
        }
        else if(intent.getStringExtra("class_name").equals("CLASS 8")){
            postImage.setImageResource(R.drawable.logo_class8);
        }
        else if(intent.getStringExtra("class_name").equals("CLASS 7")){
            postImage.setImageResource(R.drawable.logo_class7);
        }
        else if(intent.getStringExtra("class_name").equals("CLASS 6")){
            postImage.setImageResource(R.drawable.logo_class6);
        }
        else if(intent.getStringExtra("class_name").equals("CLASS 5")){
            postImage.setImageResource(R.drawable.logo_class5);
        }
        else if(intent.getStringExtra("class_name").equals("CLASS 4")|intent.getStringExtra("class_name").equals("CLASS 3")||
                intent.getStringExtra("class_name").equals("CLASS 2")|intent.getStringExtra("class_name").equals("CLASS 1")
                        |intent.getStringExtra("class_name").equals("NURSERY")|intent.getStringExtra("class_name").equals("PLAY")){
            postImage.setImageResource(R.drawable.logo_primary);
        }
        else {
            postImage.setImageResource(R.drawable.logo_else_class);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        materialToolbar = findViewById(R.id.topAppBar) ;

        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPage();
            }
        });

        responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBoxInfo = new MessageBoxInfo(contactNo,
                        guardianUid, tutorInfo.get(2), tutorInfo.get(3), false, true);

                myRefMessageBox.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int flag = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);
                            if (messageBoxInfo1.getGuardianUid().equals(guardianUid)
                                    && messageBoxInfo1.getTutorUid().equals(tutorInfo.get(3))) {
                                flag = 1;
                            }
                        }
                        if (flag == 0)
                            myRefMessageBox.push().setValue(messageBoxInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                responseButton.setBackgroundColor(Color.GRAY) ;
            }
        });
    }

    public void goToBackPage(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToBackPage();
    }
}