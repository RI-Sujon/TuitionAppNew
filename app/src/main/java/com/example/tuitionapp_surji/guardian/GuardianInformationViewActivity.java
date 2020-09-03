package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;
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

public class GuardianInformationViewActivity extends AppCompatActivity {

    private TextView nameTextView, addressTextView ;
    private CircularImageView profilePic ;
    private ImageView sendMessageButton ;

    private String user, guardianUid ;
    private ArrayList<String> tutorInfo ;
    private GuardianInfo guardianInfo ;

    private DatabaseReference myRefMessageBox, myRefGuardianInfo ;

    private MaterialToolbar materialToolbar ;

    private Button editButton ;

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
                profilePic.setBorderColor(Color.MAGENTA);
            }
        }
    }

    public void sendMessageToGuardianOperation(View view){
        final MessageBoxInfo messageBoxInfo = new MessageBoxInfo(guardianInfo.getPhoneNumber(),
                guardianUid, tutorInfo.get(2), tutorInfo.get(3), false, true,false,false);

        myRefMessageBox.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);

                    if(messageBoxInfo1.getTutorUid()!=null){
                        if (messageBoxInfo1.getGuardianUid().equals(guardianUid) && messageBoxInfo1.getTutorUid().equals(tutorInfo.get(3))) {
                            flag = 1;
                        }
                    }
                }
                if (flag == 0) {
                    myRefMessageBox.push().setValue(messageBoxInfo);
                    sendMessageButton.setBackgroundColor(Color.GRAY);
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