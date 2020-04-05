package com.example.tuitionapp.Guardian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.TutorAccountInfo;
import com.example.tuitionapp.VerifiedTutor.TutorTuitionInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GuardianTutorProfileViewActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference myRefAccountInfo , myRefTuitionInfo ;

    List<TutorAccountInfo> accountInfoList  ;
    List<TutorTuitionInfo> tuitionInfoList  ;

    TextView[] infoBox = new TextView[6] ;

    int postIndex = 0 ;

    private String allProfileInfoString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_tutor_profile_view);
        setTitle("VIEW TUTOR PROFILE");
        myRefAccountInfo = FirebaseDatabase.getInstance().getReference("AccountInfo") ;
        myRefTuitionInfo = FirebaseDatabase.getInstance().getReference("TuitionInfo") ;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //allProfileInfoString = new String[6] ;

        accountInfoList = new ArrayList<>() ;
        tuitionInfoList = new ArrayList<>() ;

        infoBox[0] = findViewById(R.id.info0) ;
        infoBox[1] = findViewById(R.id.info1) ;
        infoBox[2] = findViewById(R.id.info2) ;
        infoBox[3] = findViewById(R.id.info3) ;
        infoBox[4] = findViewById(R.id.info4) ;
        infoBox[5] = findViewById(R.id.info5) ;

        for(int i=0 ; i<6 ; i++){

        }

        myRefAccountInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    TutorAccountInfo tutorAccountInfo = dS1.getValue(TutorAccountInfo.class) ;
                    accountInfoList.add(tutorAccountInfo) ;
                }

                myRefTuitionInfo.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            TutorTuitionInfo tutorTuitionInfo = dS1.getValue(TutorTuitionInfo.class) ;
                            tuitionInfoList.add(tutorTuitionInfo) ;
                        }
                        viewProfile() ;
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



    public void viewProfile(){
        for(int i=0 ; i<tuitionInfoList.size() && i<6 ; i++){
            String str1 = tuitionInfoList.get(i).toString() ;
            str1 = str1.replace(",","\n") ;
            str1 = str1.replace("|",", ") ;
            System.out.println(str1);

            for(int j=0 ; j<accountInfoList.size() ; j++){
                String s1,s2 ;
                s1 = tuitionInfoList.get(i).getEmailPrimaryKey() ;
                s2 = accountInfoList.get(j).getEmail() ;
                if(s1.equals(s2)){
                    String str2 = accountInfoList.get(j).toString() ;
                    str2 = str2.replace(",","\n") ;
                    str2 = str2.replace("|",", ") ;
                    allProfileInfoString = str2 + "\n\n" + str1 ;
                    infoBox[i].setText(allProfileInfoString);
                    infoBox[i].setVisibility(View.VISIBLE);
                    break;
                }
            }

        }
    }

    public void goToGuardianHomePageActivity(View view){
        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
