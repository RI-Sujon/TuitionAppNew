package com.example.tuitionapp.TuitionPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorHomePageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TuitionPostViewActivity extends AppCompatActivity {

    private DatabaseReference myRefTuitionPost ;
    private ArrayList<TuitionPostInfo>tuitionPostInfoArrayList ;
    private CustomAdapterForTuitionPostView adapter ;

    private ListView listView ;

    private ArrayList<String> userInfo ;
    private String userEmail ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_post_view);
        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        userEmail = userInfo.get(2) ;

        myRefTuitionPost = FirebaseDatabase.getInstance().getReference("TuitionPost") ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        listView = findViewById(R.id.tuitionPostList) ;
        tuitionPostInfoArrayList = new ArrayList<>() ;

        myRefTuitionPost.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TuitionPostInfo postInfo = dS1.getValue(TuitionPostInfo.class) ;
                    tuitionPostInfoArrayList.add(postInfo) ;
                }
                myRefTuitionPost.removeEventListener(this);
                viewTuitionPost() ;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void viewTuitionPost(){
        adapter  = new CustomAdapterForTuitionPostView(this,tuitionPostInfoArrayList, userEmail) ;
        listView.setAdapter(adapter);
    }

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }
}
