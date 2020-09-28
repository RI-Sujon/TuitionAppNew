package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.message_box.MessageActivity;
import com.example.tuitionapp_surji.message_box.MessageBoxInfo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GuardianInformationViewActivity extends AppCompatActivity {

    private TextView nameTextView, addressTextView ;
    private CircularImageView profilePic ;
    private String user, guardianUid ;
    private ArrayList<String> tutorInfo ;
    private GuardianInfo guardianInfo ;
    private DatabaseReference myRefMessageBox, myRefGuardianInfo ;
    private MaterialToolbar materialToolbar ;
    private MaterialButton editButton, sendMessageButton ;
    private MessageBoxInfo messageBoxInfo;
    private Dialog mDialog;
    private TextView request_acceptation_btn,request_accept_yes_btn,request_accept_no_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_information_view);

        Intent intent = getIntent() ;
        user = intent.getStringExtra("user") ;

        nameTextView = findViewById(R.id.guardianName) ;
        addressTextView = findViewById(R.id.guardianAddress) ;
        sendMessageButton = findViewById(R.id.sendMessageRequestButton) ;
        profilePic = findViewById(R.id.profile_image) ;
        editButton = findViewById(R.id.editButton) ;

        myRefMessageBox = FirebaseDatabase.getInstance().getReference("MessageBox") ;
        mDialog = new Dialog(this);

        materialToolbar = findViewById(R.id.topAppBar) ;
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHomePage();
            }
        });

        if(user.equals("guardian")){
            myRefGuardianInfo = FirebaseDatabase.getInstance().getReference("Guardian").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) ;
            editButton.setVisibility(View.VISIBLE);
        }
        else{

            sendMessageButton.setVisibility(View.VISIBLE);
            tutorInfo = intent.getStringArrayListExtra("tutorInfo") ;
            guardianUid = intent.getStringExtra("guardianUid") ;
            myRefGuardianInfo = FirebaseDatabase.getInstance().getReference("Guardian").child(guardianUid) ;

            myRefMessageBox.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        MessageBoxInfo messageBoxInfo = dataSnapshot.getValue(MessageBoxInfo.class);

                        if (messageBoxInfo.getGuardianUid().equals(guardianUid) && messageBoxInfo.getTutorUid().equals(tutorInfo.get(3)))
                        {
                            if(!messageBoxInfo.isMessageFromGuardianSide() && !messageBoxInfo.isMessageFromTutorSide()){
                                sendMessageButton.setText("Send Request");
                            }

                            else if(messageBoxInfo.isMessageFromGuardianSide() && messageBoxInfo.isMessageFromTutorSide()){
                                sendMessageButton.setText("Send Message");
                            }

                            else if(!(messageBoxInfo.isMessageFromGuardianSide()) && messageBoxInfo.isMessageFromTutorSide()){
                                sendMessageButton.setText("Request Sent");
                            }

                            else if(messageBoxInfo.isMessageFromGuardianSide() && !messageBoxInfo.isMessageFromTutorSide()){
                                sendMessageButton.setText("Respond Request");
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        myRefGuardianInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                guardianInfo = dataSnapshot.getValue(GuardianInfo.class) ;
                putGuardianInfo();
                myRefGuardianInfo.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }

    public void putGuardianInfo(){
        nameTextView.setText(guardianInfo.getName());
        addressTextView.setText(guardianInfo.getAddress());
        if(guardianInfo.getProfilePicUri()!=null){
            if(!guardianInfo.getProfilePicUri().equals("1")){
                Picasso.get().load(guardianInfo.getProfilePicUri()).into(profilePic);
                profilePic.setBorderColor(Color.rgb(65,105,225));
            }
        }
    }

    public void sendMessageToGuardianOperation(View view)
    {
        myRefMessageBox.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String string = String.valueOf(sendMessageButton.getText());
                int flag = 0;
                Log.e("String  ",string);

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    final String snapshotKey = snapshot.getKey();
                    Log.e("DataSnapshotKey ",snapshotKey);

                    MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);

                    if (messageBoxInfo1.getGuardianUid().equals(guardianUid) && messageBoxInfo1.getTutorUid().equals(tutorInfo.get(3)))
                    {
                        flag=1;
                        if(flag==1 && string.equals("Send Request")){

                                Log.e("Message Button Text ","The text is Send Message. ");
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("messageFromTutorSide",true);
                                myRefMessageBox.child(snapshot.getKey()).updateChildren(hashMap);
                                sendMessageButton.setText("Request Sent");
                                myRefMessageBox.removeEventListener(this);
                        }

                        else if(string.equals("Request Sent")){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("messageFromTutorSide",false);
                            myRefMessageBox.child(snapshot.getKey()).updateChildren(hashMap);
                            sendMessageButton.setText("Send Request");
                            Log.e("Message Button Text ","The text is Request Sent. ");
                            myRefMessageBox.removeEventListener(this);
                        }

                        else if(string.equals("Send Message"))
                        {
                            Log.e("Message Button Text ","The text is Message. ");
                            Log.e("DataSnapshotKey ",snapshotKey);

                            Intent intent = new Intent(GuardianInformationViewActivity.this, MessageActivity.class);

                                intent.putExtra("userId", guardianUid);
                                intent.putExtra("mobileNumber",guardianInfo.getPhoneNumber());
                                intent.putStringArrayListExtra("userInfo", tutorInfo) ;
                                intent.putExtra("user", user);

                                startActivity(intent);
                                finish();
                        }

                        else if(string.equals("Respond Request"))
                        {
                            Log.e("Message Button Text ","The text is Respond. ");
                            Log.e("DataSnapshotKey ",snapshotKey);


                            mDialog.setContentView(R.layout.custom_pop_up_accept_message_request);
                            request_acceptation_btn = mDialog.findViewById(R.id.request_acceptation_btn);
                            request_accept_yes_btn = mDialog.findViewById(R.id.request_accept_yes_btn);
                            request_accept_no_btn = mDialog.findViewById(R.id.request_accept_no_btn);
                            mDialog.show();


                            request_accept_yes_btn.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    HashMap<String,Object> hashMap = new HashMap<>();
                                    hashMap.put("messageFromTutorSide",true);
                                    myRefMessageBox.child(snapshotKey).updateChildren(hashMap);
                                    mDialog.dismiss();
                                }
                            });

                            request_accept_no_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });

                            myRefMessageBox.removeEventListener(this);

                        }
                        break;
                    }
                }

                Log.e("Flag  ", String.valueOf(flag));
                if(flag==0 && string.equals("Send Request"))
                {

                    messageBoxInfo = new MessageBoxInfo(guardianInfo.getPhoneNumber(),
                            guardianUid, tutorInfo.get(2), tutorInfo.get(3), false,
                            true,false,false,
                            false);
                    Log.e("Message Button Text ","Send Message");
                    myRefMessageBox.push().setValue(messageBoxInfo) ;
                    sendMessageButton.setText("Request Sent");
                    myRefMessageBox.removeEventListener(this);
                }


                myRefMessageBox.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void editGuardianProfileInfo(View view){
        Intent intent = new Intent(this, GuardianInformationActivity.class);
        intent.putExtra("type", "edit");
        intent.putExtra("name", guardianInfo.getName()) ;
        intent.putExtra("address" , guardianInfo.getAddress()) ;
        intent.putExtra("guardianProfilePicUri" , guardianInfo.getProfilePicUri()) ;
        startActivity(intent);
    }

    public void backToHomePage(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToHomePage();
    }
}