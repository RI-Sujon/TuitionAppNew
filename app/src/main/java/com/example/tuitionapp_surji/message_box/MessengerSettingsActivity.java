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
import com.example.tuitionapp_surji.guardian.GuardianInfo;
import com.example.tuitionapp_surji.guardian.GuardianInformationViewActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerSettingsActivity extends AppCompatActivity
{

    private Intent intent;
    private  String checkUser,guardianMobileNumber,tutorEmail;
    private ArrayList<String> userInfo ;
    private CircleImageView messenger_settings_profile_pic;
    private TextView messenger_settings_user_name, block_in_messenger_settings,
            delete_conversation,search_conversation, something_wrong;
    private DatabaseReference candidateTutorReference, guardianReference, messageBoxReference;
    private String imageUri,gender, userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_settings);

        Toolbar toolbar= findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                final String  userId = i.getStringExtra("userId");
                Intent intent = new Intent(MessengerSettingsActivity.this, MessageActivity.class);

                if(checkUser.equals("guardian")){
                    intent.putExtra("userId", userId );
                    intent.putExtra("tutorEmail",tutorEmail);
                    intent.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent.putExtra("userId", userId);
                    intent.putExtra("mobileNumber", guardianMobileNumber);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.putExtra("user", checkUser);
                }

                startActivity(intent);
                finish();
            }
        });


        intent = getIntent();
        userId = intent.getStringExtra("userId");
        checkUser = intent.getStringExtra("user");
        guardianMobileNumber = intent.getStringExtra("mobileNumber");
        tutorEmail = intent.getStringExtra("tutorEmail");
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        messenger_settings_profile_pic = findViewById(R.id.messenger_settings_profile_pic);
        messenger_settings_user_name = findViewById(R.id.messenger_settings_user_name);
        block_in_messenger_settings = findViewById(R.id.block_in_messenger_settings);
        delete_conversation = findViewById(R.id.delete_conversation);
        search_conversation = findViewById(R.id.search_conversation);
        something_wrong = findViewById(R.id.something_wrong);

        messageBoxReference = FirebaseDatabase.getInstance().getReference("MessageBox");
        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");
        guardianReference = FirebaseDatabase.getInstance().getReference("Guardian");


        if(checkUser.equals("guardian"))
        {
            candidateTutorReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                        if(candidateTutorInfo.getEmailPK().equals(tutorEmail))
                        {
                            imageUri = candidateTutorInfo.getProfilePictureUri();
                            gender = candidateTutorInfo.getGender();

                            messenger_settings_user_name.setText(candidateTutorInfo.getUserName());
                            if(candidateTutorInfo.getGender().equals("MALE"))
                            {
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(messenger_settings_profile_pic);
                                else
                                    messenger_settings_profile_pic.setImageResource(R.drawable.user_profile_view);
                            }

                            else if(candidateTutorInfo.getGender().equals("FEMALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(messenger_settings_profile_pic);
                                else
                                    messenger_settings_profile_pic.setImageResource(R.drawable.user_profile_view);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        else if(checkUser.equals("tutor"))
        {
            guardianReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        GuardianInfo guardianInfo = snapshot.getValue(GuardianInfo.class);
                        if(guardianInfo.getPhoneNumber().equals(guardianMobileNumber))
                        {
                            messenger_settings_user_name.setText(guardianInfo.getName());
                            imageUri = guardianInfo.getProfilePicUri();

                            if(imageUri!=null){
                                Picasso.get().load(guardianInfo.getProfilePicUri()).into(messenger_settings_profile_pic);
                            }

                            else{
                                messenger_settings_profile_pic.setImageResource(R.drawable.user_profile_view);
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

    @Override
    public void onBackPressed()
    {
        Intent i = getIntent();
        final String  userId = i.getStringExtra("userId");
        Intent intent = new Intent(this, MessageActivity.class);

        if(checkUser.equals("guardian")){
            intent.putExtra("userId", userId );
            intent.putExtra("tutorEmail",tutorEmail);
            intent.putExtra("user", checkUser);
        }

        else if(checkUser.equals("tutor")){
            intent.putExtra("userId", userId);
            intent.putExtra("mobileNumber", guardianMobileNumber);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
            intent.putExtra("user", checkUser);
        }

        startActivity(intent);
        finish();
    }

    public void goToSelectedUserProfile(View view) {
        Intent intent;
        if(checkUser.equals("guardian")){
            intent= new Intent(this, VerifiedTutorProfileActivity.class);

            intent.putExtra("user", "guardian") ;
            intent.putExtra("tutorUid",userId);
            intent.putExtra("userEmail", tutorEmail) ;
            intent.putExtra("context","messenger");

        }

        else{
            intent = new Intent(this, GuardianInformationViewActivity.class);
            intent.putStringArrayListExtra("tutorInfo", userInfo) ;
            intent.putExtra("user","tutor");
            intent.putExtra("guardianUid",userId);
        }
        startActivity(intent);
        finish();
    }
}