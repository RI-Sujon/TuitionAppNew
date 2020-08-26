package com.example.tuitionapp_surji.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.batch.BatchStudentInfoTableActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GroupProfileActivity extends AppCompatActivity {

    private TextView groupNameTV, locationTV, classRangeTV, extraInfoTV ;
    private ImageView groupImageTV;

    private String groupImageString, groupID, groupName, groupAddress, groupFullAddress, classRange, extraInfo, user ;
    private ArrayList<String> tutorInfo ;

    private int PICK_IMAGE_REQUEST=200 ;
    private Uri filePath ;
    private Bitmap bitmapImage ;

    private DatabaseReference myRefGroup ;

    private MaterialToolbar materialToolbar ;
    private Menu toolbarMenu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        Intent intent = getIntent() ;

        groupNameTV = findViewById(R.id.group_name) ;
        groupImageTV = findViewById(R.id.groupProfileImage) ;
        locationTV = findViewById(R.id.location) ;
        classRangeTV = findViewById(R.id.class_range) ;
        extraInfoTV = findViewById(R.id.extraInfo) ;

        groupID = intent.getStringExtra("groupID") ;
        groupName = intent.getStringExtra("groupName") ;
        groupAddress = intent.getStringExtra("groupAddress") ;
        groupFullAddress = intent.getStringExtra("groupFullAddress") ;
        classRange = intent.getStringExtra("classRange") ;
        extraInfo = intent.getStringExtra("extraInfo") ;
        groupImageString = intent.getStringExtra("groupImage") ;

        user = intent.getStringExtra("user") ;
        tutorInfo = intent.getStringArrayListExtra("userInfo") ;

        groupNameTV.setText(groupName);
        if(groupFullAddress!=null || !groupFullAddress.equals("")) {
            locationTV.setText(groupFullAddress + ", " + groupAddress);
        }else {
            locationTV.setText(groupAddress);
        }
        classRangeTV.setText(classRange);
        extraInfoTV.setText(extraInfo);


        if(!groupImageString.equals("")){
            Picasso.get().load(groupImageString).into(groupImageTV);
        }
        else groupImageTV.setImageResource(R.drawable.group_icon);
    }

    @Override
    protected void onStart() {
        super.onStart();

        materialToolbar = findViewById(R.id.topAppBar) ;

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPage();
            }
        });

        toolbarMenu = materialToolbar.getMenu() ;

        if(!user.equals("tutor")){
            toolbarMenu.findItem(R.id.edit_info).setVisible(false);
            toolbarMenu.findItem(R.id.change_pic).setVisible(false) ;
        }


        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_info:
                        editGroupInformation();
                        break;

                    case R.id.change_pic:
                        changeGroupPicOperation();
                        break;

                }
                return true;
            }
        });
    }

    public void editGroupInformation(){
        Intent intent = new Intent(this, GroupCreationActivity.class);
        intent.putStringArrayListExtra("userInfo",tutorInfo) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID" , groupID) ;
        intent.putExtra("type", "edit") ;
        startActivity(intent);
        //finish();
    }

    public void changeGroupPicOperation(){
        selectImage();
    }

    public void selectImage() {
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
                uploadImage();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(){
        final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("groupImage/" + tutorInfo.get(2));
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
                                groupImageString = uri.toString();
                                updateGroupInfoDatabase();
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
    }

    private void updateGroupInfoDatabase(){
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group").child(groupID) ;
        GroupInfo groupInfo = new GroupInfo(groupName,groupAddress,groupFullAddress,classRange,extraInfo,groupImageString,tutorInfo.get(2),tutorInfo.get(3)) ;

        myRefGroup.setValue(groupInfo) ;

        Intent intent = new Intent(this,GroupHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo",tutorInfo) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
    }

    public void goToBackPage(){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToBackPage();
    }
}