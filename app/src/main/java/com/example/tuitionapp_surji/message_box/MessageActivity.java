package com.example.tuitionapp_surji.message_box;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity
{

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference,candidateTutorReference;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;
    private  String checkUser,guardianMobileNumber,tutorEmail;
    private String imageUri,gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image=findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        checkUser = intent.getStringExtra("user");
        guardianMobileNumber = intent.getStringExtra("mobileNumber");
        tutorEmail = intent.getStringExtra("tutorEmail");
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), userId, msg);
                }

                else{
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

                text_send.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("MessageBox");//.child(userId);
        candidateTutorReference = FirebaseDatabase.getInstance().getReference("CandidateTutor");


        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 User user =  dataSnapshot.getValue(User.class);

                if(checkUser.equals("guardian")){
                    username.setText("Tutor");
                }

                else if(checkUser.equals("tutor")){
                    username.setText("Guardian");
                }

                profile_image.setImageResource(R.mipmap.ic_launcher);

             *//*   if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }

                else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }*//*

                readMessages(fuser.getUid(),userId);//,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

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

                            username.setText(candidateTutorInfo.getUserName());
                            if(candidateTutorInfo.getGender().equals("MALE"))
                            {
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(profile_image);
                                else
                                    profile_image.setImageResource(R.drawable.male_pic);
                            }

                            else if(candidateTutorInfo.getGender().equals("FEMALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(profile_image);
                                else
                                    profile_image.setImageResource(R.drawable.female_pic);
                            }


                            break;
                        }

                    }
                    //System.out.println("Uri ===================="+imageUri);

                    readMessages(fuser.getUid(),userId,imageUri,gender);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else if(checkUser.equals("tutor"))
        {
            candidateTutorReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                   /* for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);
                        if(candidateTutorInfo.getEmailPK().equals(tutorEmail))
                        {

                            username.setText(candidateTutorInfo.getUserName());
                            if(candidateTutorInfo.getGender().equals("MALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(profile_image);
                                else
                                    profile_image.setImageResource(R.drawable.male_pic);
                            }

                            else if(candidateTutorInfo.getGender().equals("FEMALE")){
                                if(candidateTutorInfo.getProfilePictureUri()!= null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(profile_image);
                                else
                                    profile_image.setImageResource(R.drawable.female_pic);
                            }
                        }
                    }
                */
                   username.setText("Guardian");
                   profile_image.setImageResource(R.drawable.man);
                    readMessages(fuser.getUid(),userId,imageUri,gender);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);


        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessages(final String myId, final String userId, final String imageUri, final String gender){//, final String imageurl){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ){

                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageUri,gender);//,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
