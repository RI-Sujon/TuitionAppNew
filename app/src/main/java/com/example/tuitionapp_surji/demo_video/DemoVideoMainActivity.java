package com.example.tuitionapp_surji.demo_video;


import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.message_box.MainMessageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorProfileActivity;
/*import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;*/
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class DemoVideoMainActivity extends AppCompatActivity {

    private ArrayList<String> userInfo ;
    private String emailPrimaryKey ;


    private static final int PICK_VIDEO_REQUEST = 1;


    private ImageView choose_btn, video_view_btn, guardian_video_view_btn;
    private ImageView upload_btn, download_btn,delete_btn,guardian_download_btn;
    private ProgressBar progressBar;
    private VideoView videoView;
    private EditText video_name;
    private Uri videoUri;
    private MediaController mediaController;
    private StorageReference mStorageRef;
    private DatabaseReference videoReference, videos, downloadVideos;
    private StorageReference storageReference;
    private Bitmap bitmapVideo;
    private String videoUriString, videoUriForView, videoName, user ;
    private FirebaseUser firebaseUser;
    private Bitmap bitmap;
    private StorageMetadata metadata;
    private String tutorEmail, userEmail;
    private String name, tutorUid;
    private TextView t1,t2,t3,t4,t5;
    private Dialog mDialog;
    private TextView confirmation_btn,demo_cancel_btn,demo_OK_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_video_main);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user");
        tutorUid = intent.getStringExtra("tutorUid");
        tutorEmail = intent.getStringExtra("tutorEmail");
        userEmail = intent.getStringExtra("userEmail");
        emailPrimaryKey = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        choose_btn = findViewById(R.id.choose_btn);
        upload_btn = findViewById(R.id.upload_btn);
        delete_btn = findViewById(R.id.delete_video);
        download_btn = findViewById(R.id.download_video);
        video_view_btn = findViewById(R.id.video_view_btn);
        guardian_video_view_btn = findViewById(R.id.guardian_video_view_btn);
        guardian_download_btn = findViewById(R.id.guardian_download_video_btn);

        videoView = findViewById(R.id.Video_view);
        progressBar = findViewById(R.id.progress_bar);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);

        mDialog= new Dialog(this);
        mediaController = new MediaController(this);

        videoReference = FirebaseDatabase.getInstance().getReference("Videos");//.child(firebaseUser.getUid()) ;
        videos = FirebaseDatabase.getInstance().getReference("Videos");
        downloadVideos = FirebaseDatabase.getInstance().getReference("Videos");
        storageReference = FirebaseStorage.getInstance().getReference();


        //videoName = videoname.getText().toString();
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();


        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseVideo();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Video nameeeeeeeeeeeeeeeeeeeeee = "+videoName);
                progressBar.setVisibility(View.VISIBLE);
                uploadDemoVideo();
               // progressBar.setVisibility(View.VISIBLE);
            }
        });


        if(user.equals("guardian")){
            choose_btn.setVisibility(View.INVISIBLE);
            upload_btn.setVisibility(View.INVISIBLE);
            download_btn.setVisibility(View.INVISIBLE);
            video_view_btn.setVisibility(View.INVISIBLE);
            delete_btn.setVisibility(View.INVISIBLE);

            guardian_download_btn.setVisibility(View.VISIBLE);
            guardian_video_view_btn.setVisibility(View.VISIBLE);

            t1.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            t3.setVisibility(View.INVISIBLE);
            t4.setVisibility(View.INVISIBLE);
            t5.setVisibility(View.INVISIBLE);
        }
    }

    private  void ChooseVideo(){
        //System.out.println("Video nameeee33333333333333333333333333333333333333333333333333333333333333333333333333333eeeeeeeeeeeeeeeeee = "+videoName);
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            videoUri = data.getData();

             metadata = new StorageMetadata.Builder()
                    .setContentType("video/mp4")
                    .build();
            videoView.setVideoURI(videoUri);


        }}

    private String getFileExtension(Uri videoUri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(videoUri));
    }


    private void uploadDemoVideo()
    {
       // compressTheVideo();
        videos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag =0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DemoVideoInfo demoVideoInfo = snapshot.getValue(DemoVideoInfo.class);

                    if(demoVideoInfo.getEmailPrimaryKey().equals(firebaseUser.getEmail())){
                        flag =1;
                        break;
                    }
                }


                if (videoUri != null && flag ==0) {

                    int a ,b;
                    Random randomGenerator = new Random();
                    a=randomGenerator.nextInt(100);
                    b=randomGenerator.nextInt(100);

                    int c=a+5;
                    int d= c*2 + a+ b;
                    name = String.valueOf(d);
                    final StorageReference videoRef = storageReference.child("demoVideo/" + name +".mp4");

                    try {
                        UploadTask uploadTask = videoRef.putFile(videoUri,metadata);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Video Uploaded!!", Toast.LENGTH_LONG).show();
                                videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        videoUriString = uri.toString();
                                        updateCandidateTutorDatabase(name);
                                    }
                                });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Video Upload failed!!", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        //progressBar.incrementProgressBy(100);
                                        //int value = progressBar.getProgress();
                                        progressBar.setProgress((int) progress);
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                });

                    } catch (Exception e) {
                        System.out.println("Exception: " + e);
                    }
                }

                else if(videoUri==null){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DemoVideoMainActivity.this, "The video uri is null. Please, select another video.", Toast.LENGTH_LONG).show();
                }

                else if(flag>0){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DemoVideoMainActivity.this, "You can upload only one videos.\nYou can delete the previous one and upload another.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateCandidateTutorDatabase(String name) {

      //  DemoVideoInfo demoVideoInfo = new DemoVideoInfo(videoName,videoUriString,emailPrimaryKey);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("videoName",name);
        hashMap.put("videoUri",videoUriString);
        hashMap.put("emailPrimaryKey",emailPrimaryKey);
        //videoReference.push().setValue(demoVideoInfo);
        databaseReference.child("Videos").push().setValue(hashMap);
    }


    public void deleteTheDemoVideo(View view)
    {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        mDialog .setContentView(R.layout.custom_pop_up_delete_demo_video);
        confirmation_btn = mDialog.findViewById(R.id.confirmation_btn);
        demo_cancel_btn = mDialog.findViewById(R.id.demo_cancel_btn);
        demo_OK_btn = mDialog.findViewById(R.id.demo_OK_btn);
        mDialog.show();


        demo_OK_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                databaseReference.addValueEventListener(new ValueEventListener()
                {
                    String videoURI , childKey;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            DemoVideoInfo demoVideoInfo = dataSnapshot.getValue(DemoVideoInfo.class);
                            if(demoVideoInfo.getEmailPrimaryKey().equals(firebaseUser.getEmail())){
                                videoURI =demoVideoInfo.getVideoUri();
                                childKey=dataSnapshot.getKey();
                                break;
                            }
                        }

                        System.out.println("URI =============== "+ videoURI);
                        System.out.println("KEY =========="+ childKey);


                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(videoURI);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Toast.makeText(DemoVideoMainActivity.this, "File deleted successfully", Toast.LENGTH_SHORT).show();
                                updateDatabaseToRemoveVideoChild(childKey);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Toast.makeText(DemoVideoMainActivity.this, "Uh-oh, an error occurred!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        databaseReference.removeEventListener(this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mDialog.dismiss();
            }
        });


        demo_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });



    }

    private void updateDatabaseToRemoveVideoChild(String childKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference.child(childKey).removeValue();

    }


    public void viewTheDownloadedVideo(View view) {

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        if(!(user.equals("guardian"))){
            tutorEmail = firebaseUser.getEmail();
        }

        else if(tutorEmail==null){
            tutorEmail= userEmail;
        }


       // System.out.println("Tutor Email = "+ tutorEmail);

        downloadVideos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DemoVideoInfo demoVideoInfo = snapshot.getValue(DemoVideoInfo.class);

                    assert demoVideoInfo != null;
                    if(demoVideoInfo.getEmailPrimaryKey().equals(tutorEmail)){
                        videoUriForView=demoVideoInfo.getVideoUri();
                        //downloading(DemoVideoMainActivity.this, demoVideoInfo.getVideoUri());
                        counter++;
                        break;
                    }
                }

                if(counter==0)
                    Toast.makeText(DemoVideoMainActivity.this, "There is no uploaded demo video.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(videoUriForView!=null){
            Uri uri = Uri.parse(videoUriForView);
            videoView.setVideoURI(uri);
            videoView.start();
        }

    }

    public void downloadTheVideo(View view){

        if(!(user.equals("guardian"))){
            tutorEmail = firebaseUser.getEmail();
        }

        else if(tutorEmail==null){
            tutorEmail= userEmail;
        }


        System.out.println("Tutor Email = "+ tutorEmail);

        downloadVideos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    DemoVideoInfo demoVideoInfo = snapshot.getValue(DemoVideoInfo.class);

                    assert demoVideoInfo != null;
                    if(demoVideoInfo.getEmailPrimaryKey().equals(tutorEmail)){
                        downloading(DemoVideoMainActivity.this, demoVideoInfo.getVideoUri());
                        counter++;
                        break;
                    }
                }

                if(counter==0)
                    Toast.makeText(DemoVideoMainActivity.this, "There is no uploaded demo video.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void downloading(Context context, String imageUri) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(imageUri) ;

        int a ,b;
        Random randomGenerator = new Random();
        a=randomGenerator.nextInt(1000000);
        b=randomGenerator.nextInt(1000000);

        int c=a+b+5;
        int d= c*2;

        DownloadManager.Request request = new DownloadManager.Request(uri) ;
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) ;
        request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS, String.valueOf(d)+".mp4") ;

        downloadManager.enqueue(request) ;
    }


    public void goToHomePageActivity(View view) {
        Intent intent;
        if(user.equals("guardian")){
            intent = new Intent(this, VerifiedTutorProfileActivity.class);
            intent.putExtra("user", "guardian") ;
            intent.putExtra("tutorUid", tutorUid);
            intent.putExtra("tutorEmail", tutorEmail) ;
            intent.putExtra("context", "guardian_view") ;
        }

        else{
            intent = new Intent(this, VerifiedTutorHomePageActivity.class) ;
            intent.putExtra("user" , "tutor") ;
            intent.putStringArrayListExtra("userInfo", userInfo) ;
        }

        startActivity(intent);
        finish();


    }

    @Override
    public void onBackPressed() {

       if(user.equals("guardian")){
            Intent intent = new Intent(this, VerifiedTutorProfileActivity.class);
           intent.putExtra("user", "guardian");
           intent.putExtra("tutorUid", tutorUid);
           intent.putExtra("tutorEmail", tutorEmail);
           intent.putExtra("context", "guardian_view");
           startActivity(intent);
           finish();
        }

        else /*if(user.equals("tutor"))*/{

            Intent intent1 = getIntent() ;
            userInfo = intent1.getStringArrayListExtra("userInfo") ;
            Intent intent = new Intent(DemoVideoMainActivity.this, VerifiedTutorHomePageActivity.class);//.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putStringArrayListExtra("userInfo", userInfo) ;
           intent.putExtra("user" , "tutor") ;
           startActivity(intent);
            finish();
        }
    }


}

//if request.auth != null;


 /* private void compressTheVideo()
    {
        String inputVideoPath = getPath(videoUri);
        Log.d("doFileUpload ", inputVideoPath);
        FFmpeg ffmpeg = FFmpeg.getInstance(this);

        try
        {
            //Load the binary
            ffmpeg.loadBinary(new LoadBinaryResponseHandler()
            {
                @Override
                public void onStart() {
                }

                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                }

                @Override
                public void onFinish() {

                }
            });
        }

        catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }

        try
        {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            outputPath = getAppDir() + "/"+String.valueOf(count) +"video_compress.mp4";
            String[] commandArray = new String[]{};
            commandArray = new String[]{"-y", "-i", inputVideoPath, "-s", "720x480", "-r", "25",
                    "-vcodec", "mpeg4", "-b:v", "300k", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputPath};

            final ProgressDialog dialog = new ProgressDialog(VideoActivity.this);
            ffmpeg.execute(commandArray, new ExecuteBinaryResponseHandler()
            {
                @Override
                public void onStart() {
                    Log.e("FFmpeg", "onStart");
                    dialog.setMessage("Compressing... please wait");
                    dialog.show();
                }
                @Override
                public void onProgress(String message) {
                    Log.e("FFmpeg onProgress? ", message);
                }
                @Override
                public void onFailure(String message) {
                    Log.e("FFmpeg onFailure? ", message);
                }
                @Override
                public void onSuccess(String message) {
                    Log.e("FFmpeg onSuccess? ", message);

                }
                @Override
                public void onFinish()
                {
                    Log.e("FFmpeg", "onFinish");
                    if (dialog.isShowing())
                        dialog.dismiss();
                    playVideoOnVideoView(Uri.parse(outputPath));
                    isCompressed = true;
                    count = count + 1;
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            // Handle if FFmpeg is already running
        }
    }
*/

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
