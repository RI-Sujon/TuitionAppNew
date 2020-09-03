package com.example.tuitionapp_surji.message_box;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.guardian.GuardianInfo;
import com.example.tuitionapp_surji.guardian.GuardianInformationViewActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRequestActivity extends AppCompatActivity {

    private  String checkUser,guardianMobileNumber,tutorEmail;
    ArrayList<String> userInfo ;
    String  userId;

    FirebaseUser fuser;
    CircleImageView circleImageView, messageRequesterProfileImage;
    TextView acceptButton, declineButton,messageRequesterName ;

    private DatabaseReference candidateTutorReference, guardianReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_request);


        circleImageView = findViewById(R.id.requester_profile_id);
        acceptButton = findViewById(R.id.accept_button);
        declineButton = findViewById(R.id.decline_button);

        messageRequesterName =findViewById(R.id.message_requester_name);
        messageRequesterProfileImage =findViewById(R.id.message_requester_profile_image);

        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        guardianReference = FirebaseDatabase.getInstance().getReference("Guardian");


        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        checkUser = intent.getStringExtra("user");
        guardianMobileNumber = intent.getStringExtra("mobileNumber");
        tutorEmail = intent.getStringExtra("tutorEmail");
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        fuser = FirebaseAuth.getInstance().getCurrentUser();




        Toolbar toolbar= findViewById(R.id.message_request_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageRequestActivity.this,MainMessageActivity.class);
                if(checkUser.equals("guardian")){
                    intent.putExtra("user","guardian");
                }

                else {
                    intent.putExtra("user","tutor");
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                }
                startActivity(intent);
                finish();
            }
        });

        if(checkUser.equals("guardian")){
            candidateTutorReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                        CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                        if (candidateTutorInfo.getEmailPK().equals(tutorEmail))
                        {
                            messageRequesterName.setText(candidateTutorInfo.getUserName());

                            if(candidateTutorInfo.getGender().equals("MALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(messageRequesterProfileImage);
                                else
                                   messageRequesterProfileImage.setImageResource(R.drawable.male_pic);
                            }

                            else if(candidateTutorInfo.getGender().equals("FEMALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(messageRequesterProfileImage);
                                else
                                    messageRequesterProfileImage.setImageResource(R.drawable.female_pic);
                            }
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        else if(checkUser.equals("tutor")){
            guardianReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GuardianInfo guardianInfo = snapshot.getValue(GuardianInfo.class);
                        if (guardianInfo.getPhoneNumber().equals(guardianMobileNumber))
                        {

                          messageRequesterName.setText(guardianInfo.getName());

                            if(!guardianInfo.getProfilePicUri().equals("1")){
                                Picasso.get().load(guardianInfo.getProfilePicUri()).into(messageRequesterProfileImage);
                            }

                            else{
                                messageRequesterProfileImage.setImageResource(R.drawable.man);
                            }

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }


    public void goToSelectedUserProfile(View view)
    {
        Intent intent;
        if(checkUser.equals("guardian")){
            intent= new Intent(this, VerifiedTutorProfileActivity.class);

            intent.putExtra("user", "guardian") ;
            intent.putExtra("tutorUid",userId);
            intent.putExtra("userEmail", tutorEmail) ;
            intent.putExtra("context","messenger");

            startActivity(intent);
            finish();
        }

        else{
             intent = new Intent(this, GuardianInformationViewActivity.class);
             intent.putStringArrayListExtra("tutorInfo", userInfo) ;
             intent.putExtra("user","tutor");
             intent.putExtra("guardianUid",userId);
             startActivity(intent);
             finish();
        }

    }

    public void acceptTheMessageRequest(View view) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MessageBox");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MessageBoxInfo messageBoxInfo = snapshot.getValue(MessageBoxInfo.class);
                    HashMap<String, Object> hashMap = new HashMap<>();

                    if(checkUser.equals("guardian"))
                    {
                        assert messageBoxInfo != null;
                        if( fuser.getUid().equals(messageBoxInfo.getGuardianUid()) && userId.equals(messageBoxInfo.getTutorUid()) )
                        {
                            System.out.println("Update ======================================================== Update");
                            hashMap.put("messageFromGuardianSide",true);
                            snapshot.getRef().updateChildren(hashMap);
                            break;
                        }

                    }

                    else{
                        assert messageBoxInfo != null;
                        if( fuser.getUid().equals(messageBoxInfo.getTutorUid()) && userId.equals(messageBoxInfo.getGuardianUid()) )
                        {
                            System.out.println("Update ======================================================== Update");
                            hashMap.put("messageFromTutorSide",true);
                            snapshot.getRef().updateChildren(hashMap);
                            break;
                        }
                    }

                }

                Intent intent = new Intent(MessageRequestActivity.this,MainMessageActivity.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(checkUser.equals("guardian")){
                    intent.putExtra("user","guardian");
                }

                else {
                    intent.putExtra("user","tutor");
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                }
                startActivity(intent);
                finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void declineTheMessageRequest(View view) {

        Intent intent = new Intent(MessageRequestActivity.this,MainMessageActivity.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(checkUser.equals("guardian")){
            intent.putExtra("user","guardian");
        }

        else {
            intent.putExtra("user","tutor");
            intent.putStringArrayListExtra("userInfo", userInfo) ;
        }
        startActivity(intent);
        finish();
    }
}