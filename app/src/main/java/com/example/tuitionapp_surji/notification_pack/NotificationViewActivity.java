package com.example.tuitionapp_surji.notification_pack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationViewActivity extends AppCompatActivity {

    private DatabaseReference myRefNotification ;
    private ArrayList<NotificationInfo>notificationInfoArrayList ;
    private ArrayList<String> notificationInfoUidList ;
    private ArrayList<NotificationInfo>notificationInfoArrayList2 ;
    private ArrayList<String> notificationInfoUidList2 ;
    private FirebaseUser firebaseUser ;

    private ListView listView ;

    private ArrayList<String>userInfo ;
    private String user ;
    private MaterialToolbar materialToolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_notification);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(user.equals("tutor")) {
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Tutor").child(userInfo.get(3));
        }
        else{
            myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Guardian").child(firebaseUser.getUid());
        }

        listView = findViewById(R.id.listView) ;

        notificationInfoArrayList = new ArrayList<>() ;
        notificationInfoUidList = new ArrayList<>() ;

        notificationInfoArrayList2 = new ArrayList<>() ;
        notificationInfoUidList2 = new ArrayList<>() ;

        myRefNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    NotificationInfo notificationInfo = dS1.getValue(NotificationInfo.class) ;
                    notificationInfoArrayList.add(notificationInfo) ;
                    notificationInfoUidList.add(dS1.getKey()) ;
                }

                for(int i=notificationInfoArrayList.size()-1; i>=0 ; i--){
                        notificationInfoArrayList2.add(notificationInfoArrayList.get(i)) ;
                        notificationInfoUidList2.add(notificationInfoUidList.get(i)) ;
                }

                myRefNotification.removeEventListener(this);
                addReferenceNotification() ;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tutorUid = notificationInfoArrayList.get(position).getMessage3() ;
                String tutorEmail = notificationInfoArrayList.get(position).getMessage2() ;
                if(user.equals("guardian")){
                    goToSelectedTutorProfile(tutorUid, tutorEmail);
                }
                else{
                    goToSelectedTutorProfile(tutorUid, tutorEmail );
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHomePage();
            }
        });
    }


    public void addReferenceNotification(){
        CustomAdapterForNotificationView adapter = new CustomAdapterForNotificationView(this, notificationInfoArrayList2, userInfo, notificationInfoUidList2, user);
        listView.setAdapter(adapter);
    }

    public void goToSelectedTutorProfile(String tutorUid, String tutorEmail){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("tutorUid", tutorUid) ;

        if(user.equals("tutor")) {
            intent.putExtra("user", "referFriend");
        }
        else {
            intent.putExtra("user", user);
            intent.putExtra("tutorEmail", tutorEmail);
        }
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        //finish();
    }

    public void backToHomePage(){
        //Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        //intent.putStringArrayListExtra("userInfo", userInfo) ;
        //startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        backToHomePage(); ;
    }
}
