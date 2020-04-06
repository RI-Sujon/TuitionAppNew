package com.example.tuitionapp.VerifiedTutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tuitionapp.TuitionPost.TuitionPostInfo;
import com.example.tuitionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TutorViewPostActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference databaseReference ;

    List<TuitionPostInfo> postList  ;

    TextView [] postBox = new TextView[6] ;

    int postIndex = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_post);
        setTitle("VIEW POST");
        databaseReference = FirebaseDatabase.getInstance().getReference("TuitionPost") ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        postList = new ArrayList<>() ;

        postBox[0] = findViewById(R.id.post0) ;
        postBox[1] = findViewById(R.id.post1) ;
        postBox[2] = findViewById(R.id.post2) ;
        postBox[3] = findViewById(R.id.post3) ;
        postBox[4] = findViewById(R.id.post4) ;
        postBox[5] = findViewById(R.id.post5) ;

        for(int i=0 ; i<6 ; i++){

        }

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TuitionPostInfo postInfo = dS1.getValue(TuitionPostInfo.class) ;
                    postList.add(postInfo) ;
                }

                viewPost() ;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void viewPost(){
        for(int i=postList.size()-1,j=0 ; i>=0 && j<6 ; i--,j++){
            String str = postList.get(i).toString() ;
            System.out.println(str);

            str = str.substring(17,str.length()-1) ;
            str = str.replace(",","\n") ;
            str = str.replace("|",", ") ;
            System.out.println(str);

            postBox[j].setText(str);
            postBox[j].setVisibility(View.VISIBLE);
        }
    }

    public void goToTutorHomePageActivity(View view){
        Intent intent = new Intent(this, TutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
