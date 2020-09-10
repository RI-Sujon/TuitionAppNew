package com.example.tuitionapp_surji.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.notification_pack.NotificationInfo;
import com.example.tuitionapp_surji.notification_pack.SendNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupTutorAddActivity extends AppCompatActivity {

    private DatabaseReference myRefAddTutor, myRefCandidateTutor, myRefApproveAndBlock, myRefNotification ;

    private Button addTutorButton;
    private TextInputEditText tutorEmailEditText ;
    private String tutorEmail ;

    private String groupID ,user , groupName ;
    private ArrayList<String>userInfo ;

    private ProgressBar progressBar ;

    private int toastFlag = -1 ;

    private FirebaseFirestore databaseFireStore = FirebaseFirestore.getInstance() ;
    private long counterNotification ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tutor_add);
        Intent intent = getIntent() ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user") ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        groupName = intent.getStringExtra("groupName") ;

        addTutorButton = findViewById(R.id.addTutor) ;
        tutorEmailEditText = findViewById(R.id.group_tutor_email) ;
        progressBar = findViewById(R.id.progress_bar) ;

        myRefAddTutor = FirebaseDatabase.getInstance().getReference("AddTutor") ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor") ;
        myRefApproveAndBlock = FirebaseDatabase.getInstance().getReference("ApproveAndBlock") ;
        myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Tutor") ;

    }

    public void addTutorOperation(View view){
        toastFlag = -1 ;

        progressBar.setVisibility(View.VISIBLE);

        tutorEmail = tutorEmailEditText.getText().toString() ;

        myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRefCandidateTutor.removeEventListener(this);
                for(final DataSnapshot dS1: dataSnapshot.getChildren()){
                    CandidateTutorInfo candidateTutorInfo = dS1.getValue(CandidateTutorInfo.class) ;
                    if(candidateTutorInfo.getEmailPK().equals(tutorEmail)){
                        toastFlag = 1 ;
                        myRefApproveAndBlock = myRefApproveAndBlock.child(dS1.getKey());
                        myRefApproveAndBlock.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                myRefApproveAndBlock.removeEventListener(this);
                                if (dataSnapshot2.exists()) {
                                    ApproveAndBlockInfo approveAndBlockInfo = dataSnapshot2.getValue(ApproveAndBlockInfo.class);
                                    if (approveAndBlockInfo.getStatus().equals("running")) {
                                        myRefAddTutor = myRefAddTutor.child(dS1.getKey()) ;
                                        AddTutorInfo addTutorInfo = new AddTutorInfo(groupID, tutorEmail);
                                        myRefAddTutor.setValue(addTutorInfo);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Tutor Added Successfully", Toast.LENGTH_SHORT).show();
                                        sendNotification(dS1.getKey());
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Does not match with any verified tutor's email ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Does not match with any verified tutor's email ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;
                    }
                }
                if(toastFlag==-1){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Does not match with any tutor's email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendNotification(final String tutorUid){
        myRefNotification = myRefNotification.child(tutorUid).child(groupID) ;
        NotificationInfo notificationInfo = new NotificationInfo("groupTutor", groupName,"", groupID) ;
        myRefNotification.setValue(notificationInfo) ;

        SendNotification sendNotification = new SendNotification(tutorUid, "Group Tutor", "You have been added to " + groupName) ;
        sendNotification.sendNotificationOperation();

        databaseFireStore.collection("System").document("Counter")
                .collection("NotificationCounter").document(tutorUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult() ;

                counterNotification = (long) document.get("counter") ;
                counterNotification = counterNotification + 1 ;

                databaseFireStore.collection("System").document("Counter")
                        .collection("NotificationCounter").document(tutorUid)
                        .update("counter",counterNotification) ;
            }
        }) ;

        goToBackPageActivity(null);
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
