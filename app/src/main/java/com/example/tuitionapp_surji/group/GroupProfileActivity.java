package com.example.tuitionapp_surji.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class GroupProfileActivity extends AppCompatActivity {

    private TextView groupNameTV, locationTV, classRangeTV, extraInfoTV ;
    private ImageView groupImageTV;

    private String groupImageString ;

    private MaterialToolbar materialToolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        Intent intent = getIntent() ;

        groupNameTV = findViewById(R.id.group_name) ;
        groupImageTV = findViewById(R.id.groupProfileImage) ;
        locationTV = findViewById(R.id.location) ;
        classRangeTV = findViewById(R.id.class_range) ;
        extraInfoTV = findViewById(R.id.extraInfo) ;

        groupNameTV.setText(intent.getStringExtra("groupName"));
        locationTV.setText(intent.getStringExtra("location"));
        classRangeTV.setText(intent.getStringExtra("classRange"));
        extraInfoTV.setText(intent.getStringExtra("extraInfo"));

        groupImageString = intent.getStringExtra("groupImage") ;

        if(!groupImageString.equals("")){
            Picasso.get().load(groupImageString).into(groupImageTV);
        }
        else groupImageTV.setImageResource(R.drawable.group_icon);
    }

    @Override
    protected void onStart() {
        super.onStart();

        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPage();
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