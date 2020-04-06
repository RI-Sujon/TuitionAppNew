package com.example.tuitionapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;
import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CandidateTutorProfileViewActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference myRefCandidateTutorInfo  ;

    List<CandidateTutorInfo> candidateTutorInfoList  ;

    CustomerAdapterForListView adapterForListView ;

    ListView listView ;

    private String [] emailArray = new String[60];
    private String [] nameArray = new String[60];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_tutor_profile_view);
        listView = findViewById(R.id.candidateTutorList) ;
        setTitle("VIEW TUTOR PROFILE");
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        adapterForListView = new CustomerAdapterForListView(this, nameArray , emailArray);
        listView.setAdapter(adapterForListView);
    }

    @Override
    protected void onStart() {
        super.onStart();


        candidateTutorInfoList = new ArrayList<>() ;

        myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                    candidateTutorInfoList.add(candidateTutorInfo) ;
                }

                setListView() ;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void setListView(){
        for(int i=0 ; i<candidateTutorInfoList.size() ; i++){
            nameArray[i] = candidateTutorInfoList.get(i).getFirstName() + " " +  candidateTutorInfoList.get(i).getLastName() ;
            emailArray[i] = candidateTutorInfoList.get(i).getEmailPK() ;
        }

        adapterForListView = new CustomerAdapterForListView(this, nameArray , emailArray);
        listView.setAdapter(adapterForListView);

    }

    public void goToGuardianHomePageActivity(View view){
        //Intent intent = new Intent(this, GuardianHomePageActivity.class);
        //startActivity(intent);
        //finish();
    }
}

