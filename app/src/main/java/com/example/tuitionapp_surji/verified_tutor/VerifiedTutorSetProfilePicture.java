package com.example.tuitionapp_surji.verified_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tuitionapp_surji.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_surji.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
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
import java.util.ArrayList;

public class VerifiedTutorSetProfilePicture extends AppCompatActivity {

    private ImageView profilePictureImageView2, profilePictureImageView, backButton ;

    private RelativeLayout viewAndChangeProfilePicLayout, registrationProfilePicLayout ;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 122;
    private StorageReference storageReference;
    private DatabaseReference myRefCandidateTutor ;
    private FirebaseUser firebaseUser ;

    private String tutorName, tutorProfilePicUri="", tutorUid2, tutorGender;
    private CandidateTutorInfo candidateTutorInfo ;

    private String intentFlag, user, groupID, tutorEmail, tutorUid, profilePicUri ;
    private ArrayList<String> userInfo ;

    private MaterialButton changeProfilePicButton ;

    private Bitmap bitmapImage ;

    private String contextType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_set_profile_picture);

        Intent intent = getIntent() ;
        intentFlag = intent.getStringExtra("intentFlag") ;

        viewAndChangeProfilePicLayout = findViewById(R.id.view_and_change_profile_pic_layout) ;
        registrationProfilePicLayout = findViewById(R.id.registration_profile_picture_layout) ;

        if(intentFlag.equals("profile")){
            viewAndChangeProfilePicLayout.setVisibility(View.VISIBLE);
            user = intent.getStringExtra("user") ;
            tutorUid = intent.getStringExtra("tutorUid") ;
            tutorEmail = intent.getStringExtra("tutorEmail") ;
            groupID = intent.getStringExtra("groupID") ;
            profilePicUri = intent.getStringExtra("profilePicUri") ;

            contextType = intent.getStringExtra("context") ;

            userInfo = intent.getStringArrayListExtra("userInfo") ;

            profilePictureImageView = findViewById(R.id.profile_picture) ;
            changeProfilePicButton = findViewById(R.id.changeProfilePicButton) ;
            backButton = findViewById(R.id.backButton) ;

            if(user.equals("tutor")){
                tutorEmail = userInfo.get(2) ;
                changeProfilePicButton.setVisibility(View.VISIBLE);

                myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(userInfo.get(3)) ;
                storageReference = FirebaseStorage.getInstance().getReference() ;

                myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }

            if(profilePicUri==null){
            }
            else if(profilePicUri.equals("")){

            }
            else {
                Picasso.get().load(profilePicUri).into(profilePictureImageView);
            }

            changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(null);
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToTutorProfile();
                }
            });

        }
        else {
            registrationProfilePicLayout.setVisibility(View.VISIBLE);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            tutorEmail = firebaseUser.getEmail() ;
            tutorUid2 = firebaseUser.getUid() ;

            myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(tutorUid2) ;
            storageReference = FirebaseStorage.getInstance().getReference() ;

            profilePictureImageView2 = findViewById(R.id.profileImageView) ;

            myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                    tutorName = candidateTutorInfo.getUserName();
                    tutorGender = candidateTutorInfo.getUserName();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    public void selectImage(View view) {
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
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(intentFlag.equals("registration")){
                    profilePictureImageView2.setImageBitmap(bitmapImage);
                }
                else {
                    uploadImage(null);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(View view) {

        final StorageReference imageRef = storageReference.child("profilePicture/" + tutorEmail);

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                tutorProfilePicUri = uri.toString();
                                finishingRegistration(null);
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image Upload failed!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
        else selectImage(null);
    }

    public void finishingRegistration(View view){
        if(view==null){
            if(intentFlag.equals("registration")){
                candidateTutorInfo.setProfilePictureUri(tutorProfilePicUri);
                myRefCandidateTutor.setValue(candidateTutorInfo) ;
                goToVerifiedTutorHomePage();

            }
            else {
                candidateTutorInfo.setProfilePictureUri(tutorProfilePicUri);
                myRefCandidateTutor.setValue(candidateTutorInfo) ;
                backToTutorProfile() ;
            }
        }
        else goToVerifiedTutorHomePage();
    }


    public void goToVerifiedTutorHomePage(){
        ArrayList<String> userInfo = new ArrayList<>() ;
        userInfo.add(tutorName) ;

        if(tutorProfilePicUri==null){
            if(firebaseUser.getPhotoUrl()==null){
                userInfo.add("") ;
            }
            else {
                userInfo.add(firebaseUser.getPhotoUrl().toString()) ;
            }
        }
        else {
            userInfo.add(tutorProfilePicUri) ;
        }

        if(tutorEmail.charAt(0)=='-'){
            tutorEmail = tutorEmail.substring(1,tutorEmail.length()) ;
        }

        userInfo.add(tutorEmail) ;
        userInfo.add(tutorUid2) ;
        userInfo.add(tutorGender) ;

        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    private void backToTutorProfile(){
        if(user.equals("tutor")){
            Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
            intent.putExtra("user", user) ;
            intent.putStringArrayListExtra("userInfo", userInfo) ;
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        if(intentFlag.equals("profile")){
            backToTutorProfile();
        }
    }
}
