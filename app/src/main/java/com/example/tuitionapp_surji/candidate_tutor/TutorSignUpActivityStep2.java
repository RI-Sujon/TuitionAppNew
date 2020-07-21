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
import com.example.tuitionapp_surji.notification_pack.APIService;
import com.example.tuitionapp_surji.notification_pack.Client;
import com.example.tuitionapp_surji.notification_pack.Data;
import com.example.tuitionapp_surji.notification_pack.MyResponse;
import com.example.tuitionapp_surji.notification_pack.NotificationSender;
import com.example.tuitionapp_surji.notification_pack.TokenInfo;
import com.example.tuitionapp_surji.verified_tutor.NotificationInfo;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorSignUpActivityStep2 extends AppCompatActivity {

    private ImageView imageView;
    private TextView filePathView ;
    private EditText reference1, reference2;

    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 120;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser ;
    private DatabaseReference myRefCandidateTutor, myRefRefer, myRefVerifiedTutor, myRefApprove, myRefNotification, myRefNotification2, myRefNotificationToken ;

    private String emailPrimaryKey ;
    private String imageUriString ;
    private String reference1str, reference2str;

    private ProgressDialog progressDialog ;

    private ArrayList<VerifiedTutorInfo> verifiedTutorInfoList = new ArrayList<>() ;
    private ArrayList<String> verifiedTutorUidList = new ArrayList<>() ;
    private String reference1Uid, reference2Uid ;

    private CandidateTutorInfo candidateTutorInfo ;

    private Bitmap bitmapImage ;

    private APIService apiService;

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
        myRefRefer = FirebaseDatabase.getInstance().getReference("Refer").child(firebaseUser.getUid()) ;
        myRefApprove = FirebaseDatabase.getInstance().getReference("ApproveAndBlock").child(firebaseUser.getUid()) ;
        myRefNotification = FirebaseDatabase.getInstance().getReference("Notification") ;

        emailPrimaryKey = firebaseUser.getEmail() ;

        myRefVerifiedTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    VerifiedTutorInfo verifiedTutorInfo = dS1.getValue(VerifiedTutorInfo.class) ;
                    verifiedTutorInfoList.add(verifiedTutorInfo) ;
                    verifiedTutorUidList.add(dS1.getKey()) ;
                }
                myRefVerifiedTutor.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class);
                myRefCandidateTutor.removeEventListener(this);
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
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        myRefNotificationToken = FirebaseDatabase.getInstance().getReference("Tokens") ;

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

    public void candidateTutorRegistrationCompletion(View view) {
        if(filePath==null){
            Toast.makeText(getApplicationContext(), "Please Add Your Id Card's Photo.", Toast.LENGTH_SHORT).show();
            return;
        }

        reference1str = reference1.getText().toString();
        reference2str = reference2.getText().toString();

        int flag1 = 0, flag2 = 0;
        if (!reference1str.equals("")) {
            for (int i=0 ; i<verifiedTutorInfoList.size() ; i++) {
                if (reference1str.equals(verifiedTutorInfoList.get(i).getEmailPK())) {
                    flag1 = 1;
                    reference1Uid = verifiedTutorUidList.get(i) ;
                }
            }
        }
        else {
            flag1 = -1;
        }

        if (!reference2str.equals("")) {
            for (int i=0 ; i<verifiedTutorInfoList.size() ; i++) {
                if (reference2str.equals(verifiedTutorInfoList.get(i).getEmailPK())) {
                    flag2 = 1;
                    reference2Uid = verifiedTutorUidList.get(i) ;
                }
            }
        }
        else {
            flag2 = -1;
        }

        if (flag1 != -1 || flag2 != -1) {
            if (flag1 == 0) {
                Toast.makeText(getApplicationContext(), "Reference1 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            } else if (flag2 == 0) {
                Toast.makeText(getApplicationContext(), "Reference2 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            } else {
                if (flag1 == 1) {
                    ReferInfo referInfo1 = new ReferInfo(reference1str);
                    myRefRefer.push().setValue(referInfo1);

                    myRefNotification2 = myRefNotification.child(reference1Uid) ;
                    NotificationInfo notificationInfo = new NotificationInfo("refer",candidateTutorInfo.getUserName(),candidateTutorInfo.getEmailPK(),firebaseUser.getUid()) ;
                    myRefNotification2.push().setValue(notificationInfo) ;

                    sendNotificationOperation(reference1Uid);
                }
                if (flag2 == 1) {
                    ReferInfo referInfo2 = new ReferInfo(reference2str);
                    myRefRefer.push().setValue(referInfo2);

                    myRefNotification2 = myRefNotification.child(reference2Uid) ;
                    NotificationInfo notificationInfo = new NotificationInfo("refer",candidateTutorInfo.getUserName(),candidateTutorInfo.getEmailPK(),firebaseUser.getUid()) ;
                    myRefNotification2.push().setValue(notificationInfo) ;

                    sendNotificationOperation(reference2Uid);
                }

                uploadFinish();
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "You need minimum 1 Reference", Toast.LENGTH_SHORT).show();
            uploadFinish();
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

    public void updateCandidateTutorDatabase() {
        ApproveAndBlockInfo approveAndBlockInfo = new ApproveAndBlockInfo("waiting");
        myRefApprove.setValue(approveAndBlockInfo);

        candidateTutorInfo.setIdCardImageUri(imageUriString);
        myRefCandidateTutor.setValue(candidateTutorInfo);

        goToTutorSignUpActivityStep3();
    }

    public void sendNotificationOperation(String receiverUid){
        myRefNotificationToken.child(receiverUid).child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userToken = dataSnapshot.getValue(String.class);
                sendNotifications(userToken);
                System.out.println("\nuserToken: " + userToken);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }) ;
        updateToken();
    }

    public void sendNotifications(String userToken){
        Data data = new Data("Amar Matha","A friend wants to join TutorApp.Do You know him?");
        NotificationSender sender = new NotificationSender(data, userToken) ;
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(TutorSignUpActivityStep2.this, "Failed to Send Notification", Toast.LENGTH_LONG);
                        System.out.println("1111111111111111111");
                    }else {
                        Toast.makeText(TutorSignUpActivityStep2.this, "Hello to Send Notification", Toast.LENGTH_LONG);
                        System.out.println("222222222222222222");
                    }
                }else {
                    Toast.makeText(TutorSignUpActivityStep2.this, "Hello22222222 to Send Notification", Toast.LENGTH_LONG);
                    System.out.println("33333333333333333---->:" + response.code());
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
            }
        });
    }

    public void updateToken(){
        String refreshToken = FirebaseInstanceId.getInstance().getToken() ;
        TokenInfo token = new TokenInfo(refreshToken) ;
        myRefNotificationToken.child(firebaseUser.getUid()).setValue(token) ;
    }

    public void goToTutorSignUpActivityStep3 () {
        Intent intent = new Intent(TutorSignUpActivityStep2.this, TutorSignUpActivityStep3.class);
        startActivity(intent);
        finish();
    }
}
