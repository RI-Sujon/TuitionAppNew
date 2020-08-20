package com.example.tuitionapp_surji.tuition_post;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.example.tuitionapp_surji.notification_pack.SendNotification;
import com.example.tuitionapp_surji.notification_pack.NotificationInfo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TuitionPostViewSinglePageActivity extends AppCompatActivity {

    private TextView postTitleTV, genderPreferableTV, genderPreferableNoticeTV, mediumTV, class_nameTV, groupTV, subjectTV, studentInstituteNameTV, addressTV, contactNoTV, daysPerWeekTV, salaryTV, extraInfoTV, postTimeTV ;
    private String postTitle, genderPreferable, medium, class_name, group, subject, studentInstituteName, address, contactNo, daysPerWeek, salary, extraInfo, date, time ;
    private String response,tuitionPostUid ;

    private ArrayList<String> tutorInfo ;
    private String guardianUid, user ;

    private MaterialButton responseButton ;

    private ImageView postImage ;

    private DatabaseReference myRefResponsePost, myRefNotification ;

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
            tuitionPostUid = intent.getStringExtra("tuitionPostUid") ;
            response = intent.getStringExtra("response") ;
            if(response==null){

            }
            else if(response.equals("1")){
                responseButton.setBackgroundColor(Color.GRAY);
                responseButton.setText("ALREADY RESPONSE");
                responseButton.setEnabled(false);
                responseButton.setFocusable(false);
            }
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

        if(user.equals("tutor")) {
            myRefResponsePost = FirebaseDatabase.getInstance().getReference("ResponsePost").child(tutorInfo.get(3));
            myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Guardian");
        }

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPage();
            }
        });

        responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRefNotification = myRefNotification.child(guardianUid) ;
                NotificationInfo notificationInfo = new NotificationInfo("response" , tutorInfo.get(0), tutorInfo.get(2), tutorInfo.get(3), tuitionPostUid) ;
                myRefNotification.push().setValue(notificationInfo) ;

                ResponsePost responsePost = new ResponsePost(tuitionPostUid) ;
                myRefResponsePost.push().setValue(responsePost) ;

                responseButton.setBackgroundColor(Color.DKGRAY) ;

                SendNotification sendNotification = new SendNotification(guardianUid, "Response Post", "A tutor response to your post") ;
                sendNotification.sendNotificationOperation();
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