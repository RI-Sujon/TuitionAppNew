package com.example.tuitionapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tuitionapp.CandidateTutor.CandidateTutorInfo;

import com.example.tuitionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CandidateTutorProfileAdminViewActivity extends AppCompatActivity {

    DatabaseReference myRefCandidateTutorInfo, myRefApproveInfo;

    List<CandidateTutorInfo> candidateTutorInfoList ;
    List<ApproveInfo>  approveInfoList ;

    CustomerAdapterForListView adapterForListView ;

    ListView listView ;

    private String [] emailArray ;
    private String [] nameArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminclass_candidate_tutor_profile_list_view);
        listView = findViewById(R.id.candidateTutorList) ;
        setTitle("VIEW TUTOR PROFILE");
        myRefCandidateTutorInfo = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefApproveInfo = FirebaseDatabase.getInstance().getReference("Approve") ;
        //adapterForListView = new CustomerAdapterForListView(this, nameArray , emailArray);
        //listView.setAdapter(adapterForListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        candidateTutorInfoList = new ArrayList<>() ;
        approveInfoList = new ArrayList<>() ;

        myRefCandidateTutorInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                    candidateTutorInfoList.add(candidateTutorInfo) ;
                }
                myRefApproveInfo.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            ApproveInfo approveInfo = dS1.getValue(ApproveInfo.class) ;
                            approveInfoList.add(approveInfo) ;
                        }
                        myRefApproveInfo.removeEventListener(this);
                        setListView() ;
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void setListView(){
        String [] nameArrayHelper = new String[candidateTutorInfoList.size()] ;
        emailArray = new String[candidateTutorInfoList.size()] ;
        int k = 0 ;
        for(int i=0 ; i<candidateTutorInfoList.size() ; i++) {
            int flag = 0;
            for (int j = 0; j < approveInfoList.size(); j++) {
                String s1 = candidateTutorInfoList.get(i).getEmailPK();
                String s2 = approveInfoList.get(j).getCandidateTutorEmail();
                if (s1.equals(s2)) {
                    flag = -1;
                }
            }
            if (flag == 0) {
                nameArrayHelper[k] = candidateTutorInfoList.get(i).getFirstName() + " " + candidateTutorInfoList.get(i).getLastName();
                emailArray[k] = candidateTutorInfoList.get(i).getEmailPK();
                k++ ;
            }
        }
        nameArray = new String[k] ;
        for (int i = 0; i < k; i++) {
            nameArray[i] = nameArrayHelper[i] ;
        }

        adapterForListView = new CustomerAdapterForListView(this, nameArray , emailArray);
        listView.setAdapter(adapterForListView);

    }

    public void backToHomePage(View view){
        Intent intent = new Intent(this, AdminHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}

