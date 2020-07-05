package com.example.tuitionapp_surji.candidate_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.verified_tutor.TutorSignUpActivityStep3;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorInfo;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TutorSignUpActivityStep2 extends AppCompatActivity {

    private ImageView imageView;
    private TextView filePathView ;
    private EditText reference1, reference2;

    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 120;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser ;
    private DatabaseReference myRefCandidateTutor, myRefRefer, myRefVerifiedTutor, myRefApprove ;


    private String emailPrimaryKey ;
    private String imageUriString ;
    private String reference1str, reference2str;

    private ProgressDialog progressDialog ;

    private ArrayList<VerifiedTutorInfo> verifiedTutorInfoList = new ArrayList<>() ;

    private Bitmap bitmapImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step2_new);
        progressDialog = new ProgressDialog(this) ;

        imageView = findViewById(R.id.imgView) ;
        filePathView = findViewById(R.id.filePathName) ;
        reference1 = findViewById(R.id.reference1) ;
        reference2 = findViewById(R.id.reference2) ;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(firebaseUser.getUid()) ;
        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefRefer = FirebaseDatabase.getInstance().getReference("Refer") ;
        myRefApprove = FirebaseDatabase.getInstance().getReference("ApproveAndBlock").child(firebaseUser.getUid()) ;

        emailPrimaryKey = firebaseUser.getEmail() ;

        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                    verifiedTutorInfoList.add(verifiedTutorInfo) ;
                }
                myRefVerifiedTutor.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
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
            filePathView.setVisibility(View.VISIBLE);
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmapImage);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void candidateTutorRegistrationCompletion(View view) throws InterruptedException {
        reference1str = reference1.getText().toString();
        reference2str = reference2.getText().toString();

        int flag1 = 0, flag2 = 0;
        if (!reference1str.equals("")) {
            for (VerifiedTutorInfo vt : verifiedTutorInfoList) {
                if (reference1str.equals(vt.getEmailPK())) {
                    flag1 = 1;
                }
            }
        } else {
            flag1 = -1;
        }

        if (!reference2str.equals("")) {
            for (VerifiedTutorInfo vt : verifiedTutorInfoList) {
                System.out.println(reference2str + "\t\t" + vt.getEmailPK());
                if (reference2str.equals(vt.getEmailPK())) {
                    flag2 = 1 ;
                }
            }
        } else {
            flag2 = -1;
        }

        if (reference1str.equals("nadim_mama")) {
            updateCandidateTutorDatabase();
        }

        if (flag1 != -1 || flag2 != -1) {
            if (flag1 == 0) {
                Toast.makeText(getApplicationContext(), "Reference1 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            } else if (flag2 == 0) {
                Toast.makeText(getApplicationContext(), "Reference2 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            } else {
                if (flag1 == 1) {
                    ReferInfo referInfo1 = new ReferInfo(firebaseUser.getEmail(), reference1str);
                    myRefRefer.push().setValue(referInfo1);
                }
                if (flag2 == 1) {
                    ReferInfo referInfo2 = new ReferInfo(firebaseUser.getEmail(), reference2str);
                    myRefRefer.push().setValue(referInfo2);
                }

                uploadImage();
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
            }
        } else {

            Toast.makeText(getApplicationContext(), "You need minimum 1 Reference", Toast.LENGTH_SHORT).show();
            uploadImage();
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

        }
    }

    private void uploadFinish(){
        if (filePath != null) {
            final StorageReference imageRef = storageReference.child("idCardImage/" + emailPrimaryKey);

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
                                imageUriString = uri.toString();
                                updateCandidateTutorDatabase();
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

    public void uploadImage () {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                uploadFinish();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    uploadFinish();
                }
                break;

            default:
                break;
        }
    }

    public void updateCandidateTutorDatabase() {

        ApproveAndBlockInfo approveAndBlockInfo = new ApproveAndBlockInfo("waiting");
        myRefApprove.setValue(approveAndBlockInfo);

        myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CandidateTutorInfo candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class);
                candidateTutorInfo.setIdCardImageUri(imageUriString);
                myRefCandidateTutor.setValue(candidateTutorInfo);

                goToTutorSignUpActivityStep3();
                myRefCandidateTutor.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void goToTutorSignUpActivityStep3 () {
        Intent intent = new Intent(TutorSignUpActivityStep2.this, TutorSignUpActivityStep3.class);
        startActivity(intent);
        finish();
    }
}
