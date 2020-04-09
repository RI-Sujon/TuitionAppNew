package com.example.tuitionapp.Guardian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;
import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewingSearchingTutorProfileActivity extends AppCompatActivity {

    DatabaseReference myRefVerifiedTutor, myRefCandidateTutor;

    ArrayList<VerifiedTutorInfo> verifiedTutorInfoList ;
    ArrayList<CandidateTutorInfo> candidateTutorInfoList ;
    ArrayList<String> emailList ;
    ArrayList<String> nameList ;

    CustomerAdapterForViewingSearchingTutorProfile adapter ;

    ListView listView ;
    Button searchButton ;
    EditText searchBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_searching_tutor_profile);
        listView = findViewById(R.id.verifiedTutorList) ;
        searchButton = findViewById(R.id.searchButton) ;
        searchBar = findViewById(R.id.search_bar) ;
        setTitle("SEARCHING");

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userEmail = emailList.get(position) ;
                goToSelectedVerifiedTutorProfile(userEmail) ;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        verifiedTutorInfoList = new ArrayList<>() ;
        candidateTutorInfoList = new ArrayList<>() ;
        emailList = new ArrayList<>() ;
        nameList = new ArrayList<>() ;

        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                    verifiedTutorInfoList.add(verifiedTutorInfo) ;
                }

                myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                            candidateTutorInfoList.add(candidateTutorInfo) ;
                        }
                        myRefCandidateTutor.removeEventListener(this);
                        setListView();
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                myRefVerifiedTutor.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void setListView(){
        for(VerifiedTutorInfo vT: verifiedTutorInfoList){
            for(CandidateTutorInfo cT: candidateTutorInfoList){
                if(vT.getEmailPK().equals(cT.getEmailPK())){
                    emailList.add(vT.getEmailPK()) ;
                    nameList.add(cT.getFirstName()+" "+cT.getLastName()) ;
                }
            }
        }

        adapter = new CustomerAdapterForViewingSearchingTutorProfile(this,nameList,emailList);
        listView.setAdapter(adapter);

    }

    public void backToHomePage(View view){
        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSelectedVerifiedTutorProfile(String userEmail){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
        intent.putExtra("userEmail", userEmail) ;
        intent.putExtra("user", "guardian") ;
        startActivity(intent);
        finish();
    }
}
