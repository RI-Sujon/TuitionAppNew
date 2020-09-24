package com.example.tuitionapp_surji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionapp_surji.message_box.MessageActivity;

import java.util.ArrayList;

public class MessengerSettingsActivity extends AppCompatActivity
{

    private Intent intent;
    private  String checkUser,guardianMobileNumber,tutorEmail;
    private ArrayList<String> userInfo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_settings);

        intent = getIntent();
        final String  userId = intent.getStringExtra("userId");
        checkUser = intent.getStringExtra("user");
        guardianMobileNumber = intent.getStringExtra("mobileNumber");
        tutorEmail = intent.getStringExtra("tutorEmail");
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        Toolbar toolbar= findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                final String  userId = i.getStringExtra("userId");
                Intent intent = new Intent(MessengerSettingsActivity.this, MessageActivity.class);

                if(checkUser.equals("guardian")){
                    intent.putExtra("userId", userId );
                    intent.putExtra("tutorEmail",tutorEmail);
                    intent.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent.putExtra("userId", userId);
                    intent.putExtra("mobileNumber", guardianMobileNumber);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.putExtra("user", checkUser);
                }

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        final String  userId = i.getStringExtra("userId");
        Intent intent = new Intent(this, MessageActivity.class);

        if(checkUser.equals("guardian")){
            intent.putExtra("userId", userId );
            intent.putExtra("tutorEmail",tutorEmail);
            intent.putExtra("user", checkUser);
        }

        else if(checkUser.equals("tutor")){
            intent.putExtra("userId", userId);
            intent.putExtra("mobileNumber", guardianMobileNumber);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
            intent.putExtra("user", checkUser);
        }

        startActivity(intent);
        finish();
    }
}