package com.example.tuitionapp_surji.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.tuitionapp_surji.guardian.GuardianInfo;
import com.example.tuitionapp_surji.notification_pack.SendNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity
{

    private CircleImageView profile_image;
    private Uri filePath;
    private Bitmap bitmapImage ;
    private TextView username;
    private int PICK_IMAGE_REQUEST = 120;
    private FirebaseUser fuser;
    private DatabaseReference reference,candidateTutorReference, guardianReference;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("E, dd MMM yyyy") ;
    private Calendar noteCalendar = Calendar.getInstance();
    private StorageReference storageReference;
    private String imageUriString ;


    private ImageButton btn_send, img_send;
    private EditText text_send;

    private MessageAdapter messageAdapter;
    private List<Chat> mChat;

    private RecyclerView recyclerView;

    private Intent intent;
    private  String checkUser,guardianMobileNumber,tutorEmail;
    private String imageUri,gender,message_time;
    private ArrayList<String> userInfo ;
    private long counterMessage ;
    private FirebaseFirestore databaseFireStore = FirebaseFirestore.getInstance() ;

    private ValueEventListener seenListener;

    private DatabaseReference messageBlock;
    private Dialog mDialog;
    private TextView block_confirmation_btn,block_yes_btn,block_no_btn;


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
                Intent intent = new Intent(MessageActivity.this,MainMessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(checkUser.equals("guardian")){
                    intent.putExtra("user","guardian");
                }

                else {
                    intent.putExtra("user","tutor");
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                }
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mDialog = new Dialog(this);

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
        userInfo = intent.getStringArrayListExtra("userInfo") ;

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
        guardianReference = FirebaseDatabase.getInstance().getReference("Guardian");

        if(checkUser.equals("guardian"))
        {
            candidateTutorReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        CandidateTutorInfo candidateTutorInfo = snapshot.getValue(CandidateTutorInfo.class);

                        if(candidateTutorInfo.getEmailPK() != null){
                            if(candidateTutorInfo.getEmailPK().equals(tutorEmail))
                            {
                                imageUri = candidateTutorInfo.getProfilePictureUri();
                                gender = candidateTutorInfo.getGender();

                                username.setText(candidateTutorInfo.getUserName());
                                if(candidateTutorInfo.getProfilePictureUri()!=null)
                                    Picasso.get().load(candidateTutorInfo.getProfilePictureUri()).into(profile_image);

                                else
                                    profile_image.setImageResource(R.drawable.user_profile_view);

                                break;
                            }
                        }
                    }

                    readMessages(fuser.getUid(),userId,imageUri,gender);
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

                        if(guardianInfo.getPhoneNumber() != null)
                        {
                            if(guardianInfo.getPhoneNumber().equals(guardianMobileNumber))
                            {
                                username.setText(guardianInfo.getName());

                                if(guardianInfo.getProfilePicUri()!= null){
                                    imageUri = guardianInfo.getProfilePicUri();
                                    Picasso.get().load(guardianInfo.getProfilePicUri()).into(profile_image);
                                }

                                else{
                                    profile_image.setImageResource(R.drawable.user_profile_view);
                                }

                            }
                        }
                    }

                    readMessages(fuser.getUid(),userId,imageUri,gender);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        seenMessage(userId);
    }


    private void seenMessage(final String userId)
    {

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
                        hashMap.put("isSeen","yes");
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void sendMessage(final String sender, final String receiver, final String message)
    {
        DatabaseReference  messageBoxReference = FirebaseDatabase.getInstance().getReference("MessageBox");

       /* reference.child("Chats").push().setValue(hashMap);
        SendNotification sendNotification = new SendNotification(receiver,"Message","You have a new message");
        sendNotification.sendNotificationOperation();*/

        messageBoxReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                message_time = simpleDateFormat.format(noteCalendar.getTime());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String messageDay = sdf.format(d);

                String messageDate = simpleDateFormat2.format(noteCalendar.getTime());

                //System.out.println("Current week day : "+dayOfTheWeek);


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sender", sender);
                hashMap.put("receiver", receiver);
                hashMap.put("message", message);
                hashMap.put("message_time",message_time);
                hashMap.put("message_type","text");
                hashMap.put("isSeen","no");
                hashMap.put("messageDay",messageDay);
                hashMap.put("messageDate",messageDate);

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    MessageBoxInfo messageBoxInfo = snapshot.getValue(MessageBoxInfo.class);
                    if(checkUser.equals("guardian"))
                    {
                        if(messageBoxInfo.getTutorUid().equals(receiver) && messageBoxInfo.getGuardianUid().equals(sender))
                        {
                            if(!messageBoxInfo.isBlockFromGuardianSide() && !messageBoxInfo.isBlockFromTutorSide())
                            {
                                reference.child("Chats").push().setValue(hashMap);
                                SendNotification sendNotification = new SendNotification(receiver,"Message","You have a new message");
                                sendNotification.sendNotificationOperation();


                                databaseFireStore.collection("System").document("Counter")
                                        .collection("NotificationCounter").document(receiver).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult() ;

                                        counterMessage = (long) document.get("messageCounter") ;
                                        counterMessage = counterMessage + 1 ;

                                        databaseFireStore.collection("System").document("Counter")
                                                .collection("NotificationCounter").document(receiver)
                                                .update("messageCounter",counterMessage) ;
                                    }
                                }) ;

                            }

                            else if(messageBoxInfo.isBlockFromGuardianSide() || messageBoxInfo.isBlockFromTutorSide()){
                                Toast.makeText(MessageActivity.this, "You can't send messages.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MessageActivity.this, "You can't send messages.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    else if(checkUser.equals("tutor"))
                    {
                        if(messageBoxInfo.getTutorUid().equals(sender) && messageBoxInfo.getGuardianUid().equals(receiver))
                        {

                            if(!messageBoxInfo.isBlockFromGuardianSide() && !messageBoxInfo.isBlockFromTutorSide())
                            {
                                reference.child("Chats").push().setValue(hashMap);
                                SendNotification sendNotification = new SendNotification(receiver,"Message","You have a new message");
                                sendNotification.sendNotificationOperation();

                                databaseFireStore.collection("System").document("Counter")
                                        .collection("NotificationCounter").document(receiver).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult() ;

                                        counterMessage = (long) document.get("messageCounter") ;
                                        counterMessage = counterMessage + 1 ;

                                        databaseFireStore.collection("System").document("Counter")
                                                .collection("NotificationCounter").document(receiver)
                                                .update("messageCounter",counterMessage) ;
                                    }
                                }) ;

                            }

                            else if(messageBoxInfo.isBlockFromGuardianSide() || messageBoxInfo.isBlockFromTutorSide()){
                                Toast.makeText(MessageActivity.this, "You can't send messages.", Toast.LENGTH_SHORT).show();
                            }
                            else if(messageBoxInfo.isBlockFromGuardianSide() && messageBoxInfo.isBlockFromTutorSide()) {
                                Toast.makeText(MessageActivity.this, "You can't send messages.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void readMessages(final String myId, final String userId, final String imageUri, final String gender)
    {//, final String imageurl){
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

                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageUri,gender,checkUser,userInfo,userId,guardianMobileNumber,tutorEmail);//,imageurl);
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

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                Date d = new Date();
                                String messageDay = sdf.format(d);

                                String messageDate = simpleDateFormat2.format(noteCalendar.getTime());

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("sender", sender);
                                hashMap.put("receiver", receiver);
                                hashMap.put("message", imageUriString);
                                hashMap.put("message_time",message_time);
                                hashMap.put("message_type","image");
                                hashMap.put("isSeen","no");
                                hashMap.put("messageDay",messageDay);
                                hashMap.put("messageDate",messageDate);


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

        else if(checkUser.equals("guardian")){
            reference = FirebaseDatabase.getInstance().getReference("Guardian").child(fuser.getUid());
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
        reference.removeEventListener(seenListener);
        status("offline");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.block_user_menu,menu);
        /*MenuItem menuItem = menu.findItem(R.id.block_in_messenger);
        if(menuItem.getTitle().equals("Block")){
           menuItem.setTitle("Unblock");

       }*/
        return true;
    }

    /*@Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem menuItem = menu.findItem(R.id.block_in_messenger);
        if(menuItem.getTitle().equals("Block")){
            menuItem.setTitle("Unblock");

        }
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String  userId = intent.getStringExtra("userId");
        MenuItem blockMenuItem = ((Toolbar)findViewById(R.id.toolbar)).getMenu().findItem(R.id.block_in_messenger);
        MenuItem unblockMenuItem = ((Toolbar)findViewById(R.id.toolbar)).getMenu().findItem(R.id.unblock_in_messenger);

        switch (item.getItemId())
        {
            case R.id.block_in_messenger:
                blockTheUser(true);
                Intent intent = new Intent(MessageActivity.this,MessageActivity.class);

                blockMenuItem.setVisible(false);
                unblockMenuItem.setVisible(true);

                if(checkUser.equals("guardian")){
                    intent.putExtra("userId", userId);
                    intent.putExtra("tutorEmail",tutorEmail);
                    intent.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent.putExtra("userId", userId);
                    intent.putExtra("mobileNumber",guardianMobileNumber);
                    intent.putStringArrayListExtra("userInfo", userInfo) ;
                    intent.putExtra("user", checkUser);
                }
                startActivity(intent);
                finish();
                break;


            case R.id.unblock_in_messenger:
                blockTheUser(false);
                Intent intent01 = new Intent(MessageActivity.this,MessageActivity.class);
                if(checkUser.equals("guardian")){
                    intent01.putExtra("userId", userId);
                    intent01.putExtra("tutorEmail",tutorEmail);
                    intent01.putExtra("user", checkUser);
                }

                else if(checkUser.equals("tutor")){
                    intent01.putExtra("userId", userId);
                    intent01.putExtra("mobileNumber",guardianMobileNumber);
                    intent01.putStringArrayListExtra("userInfo", userInfo) ;
                    intent01.putExtra("user", checkUser);
                }
                startActivity(intent01);
                finish();
                break;


                //return true;
        }

        return false;
    }



    private void blockTheUser(final boolean data)
    {
        final String  userId = intent.getStringExtra("userId");
        messageBlock = FirebaseDatabase.getInstance().getReference("MessageBox");
        final String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDialog.setContentView(R.layout.custom_pop_up_block_the_user);
        block_confirmation_btn = mDialog.findViewById(R.id.block_confirmation_btn);
        block_yes_btn = mDialog.findViewById(R.id.block_yes_btn);
        block_no_btn = mDialog.findViewById(R.id.block_no_btn);
        mDialog.show();

        block_yes_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                messageBlock.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dS1: dataSnapshot.getChildren()){
                            MessageBoxInfo messageBoxInfo = dS1.getValue(MessageBoxInfo.class);
                            if(checkUser.equals("guardian"))
                            {
                                if(messageBoxInfo.getGuardianUid().equals(currentUser) && messageBoxInfo.getTutorUid().equals(userId)){

                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("blockFromGuardianSide",data);
                                    messageBlock.child(dS1.getKey()).updateChildren(hashMap);
                                    break;

                                }
                            }

                            else if(checkUser.equals("tutor"))
                            {
                                if (messageBoxInfo.getGuardianUid().equals(userId) && messageBoxInfo.getTutorUid().equals(currentUser)) {
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("blockFromTutorSide",data);
                                    messageBlock.child(dS1.getKey()).updateChildren(hashMap);
                                    break;
                                }
                            }
                        }

                        messageBlock.removeEventListener(this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mDialog.dismiss();
            }
        });

        block_no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


    }

    public void goTOMessengerSettings(View view) {

        Intent i = getIntent();
        final String  userId = i.getStringExtra("userId");
        Intent intent = new Intent(this, MessengerSettingsActivity.class);

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
}
