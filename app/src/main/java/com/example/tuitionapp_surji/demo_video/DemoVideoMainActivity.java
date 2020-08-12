package com.example.tuitionapp_surji.demo_video;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class DemoVideoMainActivity extends AppCompatActivity {

    private ArrayList<String> userInfo ;
    private String emailPrimaryKey ;


    private static final int PICK_VIDEO_REQUEST = 1;


    private Button choosebtn;
    private Button uploadbtn, download_btn;
    private   ProgressBar progressBar;
    private VideoView videoView;
    private EditText videoname;
    private Uri videoUri;
    private MediaController mediaController;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef, videoReference;
    private StorageReference storageReference;
    private Bitmap bitmapVideo;
    private String videoUriString, videoName ;
    private FirebaseUser firebaseUser;
    Bitmap bitmap;
    StorageMetadata metadata;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video_main);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        emailPrimaryKey = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        choosebtn = findViewById(R.id.choose_btn);
        uploadbtn = findViewById(R.id.upload_btn);
        videoView = findViewById(R.id.Video_view);
        progressBar = findViewById(R.id.progress_bar);
        videoname = findViewById(R.id.video_name);
        download_btn = findViewById(R.id.download_video);


        mediaController = new MediaController(this);

        videoReference = FirebaseDatabase.getInstance().getReference("Videos");//.child(firebaseUser.getUid()) ;
        mStorageRef = FirebaseStorage.getInstance().getReference("videos");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("videos");
        storageReference = FirebaseStorage.getInstance().getReference();

        videoName = videoname.getText().toString();
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();


        choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseVideo();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Video nameeeeeeeeeeeeeeeeeeeeee = "+videoName);
                //UploadVideo();
                progressBar.setVisibility(View.VISIBLE);
                uploadFinish();
               // progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    private  void ChooseVideo(){
        //System.out.println("Video nameeee33333333333333333333333333333333333333333333333333333333333333333333333333333eeeeeeeeeeeeeeeeee = "+videoName);
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            videoUri = data.getData();

             metadata = new StorageMetadata.Builder()
                    .setContentType("video/mp4")
                    .build();
            videoView.setVideoURI(videoUri);


        }}

    private String getFileExtension(Uri videoUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(videoUri));
    }

    /*private void UploadVideo() {

        progressBar.setVisibility(View.VISIBLE);
        if (videoUri != null){
            StorageReference reference = mStorageRef.child(System.currentTimeMillis() +
                    "." +getFileExtension(videoUri));

            reference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Upload successful",Toast.LENGTH_SHORT).show();
                            DemoVideoInfo member = new DemoVideoInfo(videoname.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString(), emailPrimaryKey);
                            String UploadId = mDataBaseRef.push().getKey();
                            mDataBaseRef.child(UploadId).setValue(member);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


        }else {
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }


    }
*/

    private void uploadFinish(){

        if (videoUri != null) {
            final StorageReference videoRef = storageReference.child("demoVideo/" + videoname.getText().toString()+"1.mp4");

            try {
                UploadTask uploadTask = videoRef.putFile(videoUri,metadata);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Video Uploaded!!", Toast.LENGTH_SHORT).show();
                        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                videoUriString = uri.toString();
                                updateCandidateTutorDatabase();
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Video Upload failed!!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                //progressBar.text("Uploaded " + (int) progress + "%");
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress((int) progress);
                            }
                        });

            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
    }


    public void updateCandidateTutorDatabase() {

        DemoVideoInfo demoVideoInfo = new DemoVideoInfo(videoName,videoUriString,emailPrimaryKey);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name",videoname.getText().toString());
        hashMap.put("videoUri",videoUriString);
        hashMap.put("emailPrimaryKey",emailPrimaryKey);
        //videoReference.push().setValue(demoVideoInfo);
        databaseReference.child("Videos").push().setValue(hashMap);
    }


    public void goToHomePageActivity(View view) {

        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void viewTheDownloadedVideo(View view) {
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/tuitionapp-d7ee7.appspot.com/o/demoVideo%2F1.mp4?alt=media&token=3dae9f0f-73ff-452c-a93c-a00bf00d6cb2");
        videoView.setVideoURI(uri);
        videoView.start();
    }
}

//if request.auth != null;