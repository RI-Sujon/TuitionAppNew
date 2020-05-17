package com.example.tuitionapp_sujon.verified_tutor;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_sujon.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp_sujon.R;
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

import java.io.IOException;
import java.util.ArrayList;

public class VerifiedTutorSetProfilePicture extends AppCompatActivity {

    private ImageView profilePictureImageView ;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 122;
    private StorageReference storageReference;
    private DatabaseReference myRefCandidateTutor ;
    private FirebaseUser firebaseUser ;

    private String profilePictureUri ;
    private String tutorName, tutorProfilePicUri="", tutorEmail, tutorUid, tutorGender;
    private CandidateTutorInfo candidateTutorInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_set_profile_picture);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tutorEmail = firebaseUser.getEmail() ;
        tutorUid = firebaseUser.getUid() ;

        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(tutorUid) ;
        storageReference = FirebaseStorage.getInstance().getReference() ;

        profilePictureImageView = findViewById(R.id.profileImageView) ;

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePictureImageView.setImageBitmap(bitmap);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(View view) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference imageRef = storageReference.child("profilePicture/" + tutorEmail);

            imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePictureUri = uri.toString() ;
                            tutorProfilePicUri = profilePictureUri ;
                            finishingRegistration(null);
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Upload failed!!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }

    public void finishingRegistration(final View view){
        if(view==null){
            candidateTutorInfo.setProfilePictureUri(profilePictureUri);
            myRefCandidateTutor.setValue(candidateTutorInfo) ;
        }

        goToVerifiedTutorHomePage();
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
        userInfo.add(tutorUid) ;
        userInfo.add(tutorGender) ;

        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }
}
