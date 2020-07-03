package com.example.tuitionapp_surji.message_box;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity
{

    CircleImageView profile_image;
    private Uri filePath;
    private Bitmap bitmapImage ;
    TextView username;
    private int PICK_IMAGE_REQUEST = 120;
    FirebaseUser fuser;
    DatabaseReference reference,candidateTutorReference;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
    private Calendar noteCalendar = Calendar.getInstance();
    private StorageReference storageReference;
    private String imageUriString ;


    ImageButton btn_send, img_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;
    private  String checkUser,guardianMobileNumber,tutorEmail;
    private String imageUri,gender,message_time;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,MainMessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        img_send = findViewById(R.id.img_send);
        text_send = findViewById(R.id.text_send);

        storageReference = FirebaseStorage.getInstance().getReference();

        intent = getIntent();
        final String  userId = intent.getStringExtra("userId");
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
            seenMessage(userId);
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
            seenMessage(userId);
        }


    }


    private void seenMessage(final String userId){

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userId))
                    {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen",true);
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }

                reference.removeEventListener(seenListener);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        message_time = simpleDateFormat.format(noteCalendar.getTime());
       // System.out.println("Time ===================================="+ message_time);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("message_time",message_time);
        hashMap.put("message_type","text");
        hashMap.put("isSeen",false);


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


    public void selectImageForMessage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
           // filePathView.setText(filePath.toString());
           // filePathView.setVisibility(View.VISIBLE);
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              //  imageView.setImageBitmap(bitmapImage);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        String userId = intent.getStringExtra("userId");
        uploadFinish(fuser.getUid(), userId);

        System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
    }



    private void uploadFinish(final String sender, final String receiver){

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        if (filePath != null) {

            int a ,b;
            Random randomGenerator = new Random();
            a=randomGenerator.nextInt(1000000);
            b=randomGenerator.nextInt(1000000);

            System.out.println("Random1 =============   "+a);
            System.out.println("Random2 =============   "+b);

            int c=a+b;

            final StorageReference imageRef = storageReference.child("messageBoxImage/" + c);
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUriString = uri.toString();



                                message_time = simpleDateFormat.format(noteCalendar.getTime());
                                // System.out.println("Time ===================================="+ message_time);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("sender", sender);
                                hashMap.put("receiver", receiver);
                                hashMap.put("message", imageUriString);
                                hashMap.put("message_time",message_time);
                                hashMap.put("message_type","image");


                                reference.child("Chats").push().setValue(hashMap);

                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                               // progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image Upload failed!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                               // progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }




        }
    }

    private void status(String status){

        if(checkUser.equals("tutor")){
            reference = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(fuser.getUid());
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("status",status);
            reference.updateChildren(hashMap);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
