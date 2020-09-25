package com.example.tuitionapp_surji.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupCreationActivity extends AppCompatActivity {
    private DatabaseReference myRefGroup ;

    private TextInputEditText groupNameEditText, fullAddressEditText, classRangeEditText, extraInfoEditText, groupImageEditText ;
    private AutoCompleteTextView addressSpinner ;
    private Button createGroupButton ;
    private String user, groupID, tutorApprovalStatus ;
    private ArrayList<String> userInfo ;

    private int PICK_IMAGE_REQUEST=200 ;
    private Uri filePath ;
    private String filePathUriString="" ;
    private Bitmap bitmapImage ;

    private GroupInfo groupInfo ;

    private String groupName, areaAddress, fullAddress, classRange, extraInfo, groupImage, type ;

    private TextInputLayout imageLayout ;
    private TextView headline ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        user = intent.getStringExtra("user") ;
        type = intent.getStringExtra("type") ;
        tutorApprovalStatus = intent.getStringExtra("tutorApprovalStatus") ;
        myRefGroup = FirebaseDatabase.getInstance().getReference("Group") ;

        createGroupButton = findViewById(R.id.createGroupButton) ;
        addressSpinner = findViewById(R.id.groupAddressSpinner2) ;
        groupNameEditText = findViewById(R.id.groupName) ;
        fullAddressEditText = findViewById(R.id.detailsAddress) ;
        classRangeEditText = findViewById(R.id.class_range) ;
        extraInfoEditText = findViewById(R.id.more_about_group) ;
        groupImageEditText = findViewById(R.id.image) ;
        imageLayout = findViewById(R.id.imageLayout) ;
        headline = findViewById(R.id.headline) ;

        List areaAddress = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.areaAddress_array))) ;
        areaAddress.remove(0) ;
        ArrayAdapter adapter = new ArrayAdapter(GroupCreationActivity.this,android.R.layout.simple_dropdown_item_1line,areaAddress);

        addressSpinner.setAdapter(adapter);

        groupImageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage() ;
            }
        });

        if(tutorApprovalStatus!=null){
            if(!tutorApprovalStatus.equals("running")){
                takeActionForNonApprovalTutor() ;
            }
        }

        if(type!=null){
            groupImageEditText.setVisibility(View.GONE);
            imageLayout.setVisibility(View.GONE);
            headline.setText("EDIT GROUP INFO");
            createGroupButton.setText("Update");
            groupID = intent.getStringExtra("groupID") ;

            myRefGroup = myRefGroup.child(groupID) ;

            myRefGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    groupInfo = dataSnapshot.getValue(GroupInfo.class) ;
                    editGroupOperationStep1();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void takeActionForNonApprovalTutor(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this) ;
        dialog.setMessage("Please Wait for Admin Approval.") ;
        dialog.setCancelable(false) ;

        dialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToBackPageActivity(null);
                    }
                }) ;

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.dismiss();
                    goToBackPageActivity(null);
                }
                return true;
            }
        }) ;

        dialog.show();

    }

    public void editGroupOperationStep1(){
        groupNameEditText.setText(groupInfo.getGroupName());
        fullAddressEditText.setText(groupInfo.getFullAddress());
        addressSpinner.setText(groupInfo.getAddress());
        classRangeEditText.setText(groupInfo.getClassRange());
        extraInfoEditText.setText(groupInfo.getExtraInfo());
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
                groupImageEditText.setText(filePath.toString());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(){
        final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("groupImage/" + userInfo.get(2));
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
                                filePathUriString = uri.toString();
                                finishingGroupCreation();
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

    public void groupCreation(View view){

        areaAddress = addressSpinner.getText().toString().trim() ;
        groupName = groupNameEditText.getText().toString().trim() ;
        fullAddress = fullAddressEditText.getText().toString().trim() ;
        classRange = classRangeEditText.getText().toString().trim() ;
        extraInfo = extraInfoEditText.getText().toString().trim() ;

        if(type==null){
            groupImage = groupImageEditText.getText().toString().trim() ;
        }
        else filePathUriString = groupInfo.getGroupImageUri() ;

        if(groupName.equals("")){
            groupNameEditText.setError("");
            return;
        }

        if(areaAddress.equals("")){
            addressSpinner.setError("");
            return;
        }

        if(fullAddress.equals("")){
            fullAddressEditText.setError("");
            return;
        }

        if(filePath==null){
            finishingGroupCreation();
        }
        else {
            uploadImage();
        }

    }

    public void finishingGroupCreation(){
        GroupInfo groupInfo = new GroupInfo(groupName,areaAddress,fullAddress,classRange,extraInfo,filePathUriString,userInfo.get(2),userInfo.get(3)) ;
        if(type==null) {
            myRefGroup = myRefGroup.push();
            groupID = myRefGroup.getKey();
            myRefGroup.setValue(groupInfo);
            Toast.makeText(getApplicationContext(), "Group Successfully Created", Toast.LENGTH_SHORT).show();
        }else {
            myRefGroup.setValue(groupInfo);
            Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
        }
        goToGroupHomePage();
    }

    public void goToGroupHomePage(){
        Intent intent = new Intent(this,GroupHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo",userInfo) ;
        intent.putExtra("user" , user) ;
        intent.putExtra("groupID", groupID) ;
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        if(type==null) {
            finish();
        }
        else goToGroupHomePage();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
