package com.example.tuitionappv1.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tuitionappv1.R;
import com.example.tuitionappv1.verified_tutor.VerifiedTutorProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupTutorViewActivity extends AppCompatActivity {

    private DatabaseReference myRefAddTutor ;

    private Button addTutorOptionButton, addTutorButton;
    private EditText tutorEmailEditText ;
    private ListView tutorListView ;

    private String tutorEmail ;

    private ArrayList<AddTutorInfo> addTutorInfoArrayList ;

    private String groupID ,user ;
    private ArrayList<String>userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tutor_view);
        Intent intent = getIntent() ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user") ;
        if(user.equals("tutor")||user.equals("groupAdmin")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            user = "tutor" ;
        }
        else if(user.equals("groupVisitor")){
            user = "guardian" ;
        }


        addTutorButton = findViewById(R.id.addTutor) ;
        addTutorOptionButton = findViewById(R.id.addTutorOption) ;
        tutorEmailEditText = findViewById(R.id.email) ;
        tutorListView = findViewById(R.id.tutorListView) ;

        myRefAddTutor = FirebaseDatabase.getInstance().getReference("AddTutor") ;

        addTutorInfoArrayList = new ArrayList<>() ;

        myRefAddTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    AddTutorInfo addTutorInfo = dS1.getValue(AddTutorInfo.class) ;
                    if(addTutorInfo.getGroupID().equals(groupID)){
                        addTutorInfoArrayList.add(addTutorInfo) ;
                    }
                    goToTutorListView();
                }
                myRefAddTutor.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String newTutorEmail = addTutorInfoArrayList.get(position).getTutorEmail() ;
                goToSelectedVerifiedTutorProfile(newTutorEmail);
            }
        });

        addTutorOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorEmailEditText.setVisibility(View.VISIBLE);
                addTutorButton.setVisibility(View.VISIBLE);
                addTutorOptionButton.setVisibility(View.GONE);
            }
        });

        if(user.equals("guardian")||user.equals("admin")){
            addTutorOptionButton.setVisibility(View.GONE);
        }
    }

    public void addTutorOperation(View view){
        tutorEmail = tutorEmailEditText.getText().toString() ;
        AddTutorInfo addTutorInfo = new AddTutorInfo(groupID,tutorEmail) ;

        myRefAddTutor.push().setValue(addTutorInfo) ;

        Intent intent = new Intent(this, GroupTutorViewActivity.class);
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        startActivity(intent);
        finish();
    }

    public void goToTutorListView(){
        CustomAdapterForGroupTutorViewActivity adapter = new CustomAdapterForGroupTutorViewActivity(this,addTutorInfoArrayList) ;
        tutorListView.setAdapter(adapter);
    }

    public void goToSelectedVerifiedTutorProfile(String groupTutorEmail){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("groupID" , groupID) ;
        if(user.equals("tutor")){
            intent.putExtra("user","groupAdmin") ;
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        else{
            intent.putExtra("user","groupVisitor") ;
        }

        intent.putExtra("userEmail" , groupTutorEmail) ;

        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("groupID", groupID) ;
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }

        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
