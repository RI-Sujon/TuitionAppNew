package com.example.tuitionapp.verified_tutor;

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

import com.example.tuitionapp.candidate_tutor.CandidateTutorInfo;
import com.example.tuitionapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private TextView filePathView ;
    private ImageView profilePictureImageView ;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 122;
    private StorageReference storageReference;
    private DatabaseReference myRefVerifiedTutor, myRefCandidateTutor ;

    private String profilePictureUri ;
    String userName, userProfilePicUri, userEmail, userUid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_set_profile_picture);
        getSupportActionBar().hide();

        Intent intent = getIntent() ;
        userEmail = intent.getStringExtra("userEmail") ;
        userUid = intent.getStringExtra("userUid") ;

        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor").child(userUid) ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(userUid) ;
        storageReference = FirebaseStorage.getInstance().getReference() ;

        filePathView = findViewById(R.id.filePathName) ;
        profilePictureImageView = findViewById(R.id.profileImageView) ;

        myRefCandidateTutor.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CandidateTutorInfo candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                userName = candidateTutorInfo.getFirstName() + " " + candidateTutorInfo.getLastName() ;

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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
            filePathView.setText(filePath.toString());
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
            final StorageReference imageRef = storageReference.child("profilePicture/" + userEmail);

            imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profilePictureUri = uri.toString() ;
                            userProfilePicUri = profilePictureUri ;
                            storeUriInVerifiedTutorDatabase();
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

    public void storeUriInVerifiedTutorDatabase(){
        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                VerifiedTutorInfo verifiedTutorInfo = dataSnapshot.getValue(VerifiedTutorInfo.class) ;
                verifiedTutorInfo.setProfilePictureUri(profilePictureUri);
                myRefVerifiedTutor.setValue(verifiedTutorInfo) ;
                myRefVerifiedTutor.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userProfilePicUri", userProfilePicUri);
        intent.putExtra("userName", userEmail);
        intent.putExtra("userUid", userUid);
        startActivity(intent);
        finish();
    }


    public void goToVerifiedTutorHomePage(View view){
        ArrayList<String> userInfo = new ArrayList<>() ;
        userInfo.add(userName) ;
        userInfo.add(userProfilePicUri) ;
        userInfo.add(userEmail) ;
        userInfo.add(userUid) ;

        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        //intent.putExtra("userName", userName);
        //intent.putExtra("userProfilePicUri", userProfilePicUri);
        //intent.putExtra("userEmail", userEmail);
        //intent.putExtra("userUid", userUid);
        startActivity(intent);
        finish();
    }
}
