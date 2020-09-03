package com.example.tuitionapp_surji.message_box;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.guardian.GuardianHomePageActivity;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ImageLargeViewActivity extends AppCompatActivity {

    private String imageUri, checkUser, guardianMobileNumber, tutorEmail;
    private ImageView imageView;
    private ArrayList<String> userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_large_view);

        Intent intent = getIntent();
        imageUri = intent.getStringExtra("imageUri");
        checkUser = intent.getStringExtra("user");
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        guardianMobileNumber = intent.getStringExtra("mobileNumber");
        tutorEmail = intent.getStringExtra("tutorEmail");


        imageView = findViewById(R.id.image_larger_view);

        Picasso.get().load(imageUri).into(imageView);


    }

    public void downloadImage(View view) {
        downloading(ImageLargeViewActivity.this, imageUri);
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
        request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS, String.valueOf(d)+".jpg") ;

        downloadManager.enqueue(request) ;
    }

    public void goBackToMessageActivity(View view) {

        final String  userId = getIntent().getStringExtra("userId");

        Intent intent = new Intent(ImageLargeViewActivity.this, MessageActivity.class);


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
    }
}