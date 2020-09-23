package com.example.tuitionapp_surji.candidate_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.admin.ApproveAndBlockInfo;
import com.example.tuitionapp_surji.notification_pack.SendNotification;
import com.example.tuitionapp_surji.notification_pack.NotificationInfo;
import com.example.tuitionapp_surji.verified_tutor.TutorSignUpActivityStep3;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TutorSignUpActivityStep2 extends AppCompatActivity {

    private ImageView imageView ;
    private TextView filePathView ;
    private EditText reference1, reference2 ;
    private Button uploadButton ;

    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 120;
    private int TAKE_PICTURE = 121 ;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser ;
    private DatabaseReference myRefCandidateTutor, myRefRefer, myRefVerifiedTutor, myRefApprove, myRefNotification, myRefNotification2, myRefNotification3 ;

    private String emailPrimaryKey ;
    private String imageUriString ;
    private String reference1str, reference2str;

    private ProgressDialog progressDialog ;

    private ArrayList<VerifiedTutorInfo> verifiedTutorInfoList = new ArrayList<>() ;
    private ArrayList<String> verifiedTutorUidList = new ArrayList<>() ;
    private String reference1Uid, reference2Uid ;

    private CandidateTutorInfo candidateTutorInfo ;

    private FirebaseFirestore databaseFireStore = FirebaseFirestore.getInstance() ;
    private long counterNotification ;

    private Bitmap bitmapImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step2);
        progressDialog = new ProgressDialog(this) ;

        imageView = findViewById(R.id.imgView) ;
        filePathView = findViewById(R.id.filePathName) ;
        reference1 = findViewById(R.id.reference1) ;
        reference2 = findViewById(R.id.reference2) ;
        uploadButton = findViewById(R.id.upload_btn) ;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(firebaseUser.getUid()) ;
        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefRefer = FirebaseDatabase.getInstance().getReference("Refer").child(firebaseUser.getUid()) ;
        myRefApprove = FirebaseDatabase.getInstance().getReference("ApproveAndBlock").child(firebaseUser.getUid()) ;
        myRefNotification = FirebaseDatabase.getInstance().getReference("Notification").child("Tutor") ;
        myRefNotification3 = FirebaseDatabase.getInstance().getReference("Notification").child("Admin") ;

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

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras() ;

            try {
                bitmapImage = (Bitmap)bundle.get("data") ;
                filePath = Uri.parse("TAKE_PHOTO") ;

                imageView.setImageBitmap(bitmapImage);

                uploadButton.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                System.out.println("exception:" + e);
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            filePathView.setText(filePath.toString());
            filePathView.setVisibility(View.VISIBLE);
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmapImage);

                uploadButton.setVisibility(View.VISIBLE);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void candidateTutorRegistrationCompletion(View view) {
        if(imageUriString==null){
            Toast.makeText(getApplicationContext(), "Please Upload Your Id Card's Photo.", Toast.LENGTH_SHORT).show();
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

        NotificationInfo notificationInfo = new NotificationInfo("refer",candidateTutorInfo.getUserName(),candidateTutorInfo.getEmailPK(),firebaseUser.getUid()) ;

        if (flag1 != -1 || flag2 != -1) {
            if (flag1 == 0) {
                Toast.makeText(getApplicationContext(), "Reference1 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            } else if (flag2 == 0) {
                Toast.makeText(getApplicationContext(), "Reference2 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            } else {
                if (flag1 == 1) {
                    ReferInfo referInfo1 = new ReferInfo(reference1str, reference1Uid);
                    myRefRefer.push().setValue(referInfo1);

                    myRefNotification2 = myRefNotification.child(reference1Uid) ;
                    myRefNotification2.push().setValue(notificationInfo) ;

                    notificationInfo.setTypes("newAccount"); ;
                    myRefNotification3.push().setValue(notificationInfo) ;

                    SendNotification sendNotification = new SendNotification(reference1Uid, "New Friend", "A friend wants to join TutorApp.Do You know him?") ;
                    sendNotification.sendNotificationOperation();

                    databaseFireStore.collection("System").document("Counter")
                            .collection("NotificationCounter").document(reference1Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult() ;

                            counterNotification = (long) document.get("counter") ;
                            counterNotification = counterNotification + 1 ;

                            databaseFireStore.collection("System").document("Counter")
                                    .collection("NotificationCounter").document(reference1Uid)
                                    .update("counter",counterNotification) ;
                        }
                    }) ;

                    databaseFireStore.collection("System").document("Counter")
                            .collection("NotificationCounter").document("admin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult() ;

                            counterNotification = (long) document.get("counter") ;
                            counterNotification = counterNotification + 1 ;

                            databaseFireStore.collection("System").document("Counter")
                                    .collection("NotificationCounter").document(reference1Uid)
                                    .update("counter",counterNotification) ;
                        }
                    }) ;
                }
                if (flag2 == 1) {
                    ReferInfo referInfo2 = new ReferInfo(reference2str, reference2Uid);
                    myRefRefer.push().setValue(referInfo2);

                    myRefNotification2 = myRefNotification.child(reference2Uid) ;
                    myRefNotification2.push().setValue(notificationInfo) ;

                    SendNotification sendNotification = new SendNotification(reference2Uid, "New Friend", "A friend wants to join TutorApp.Do You know him?") ;
                    sendNotification.sendNotificationOperation();

                    databaseFireStore.collection("System").document("Counter")
                            .collection("NotificationCounter").document(reference2Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult() ;

                            counterNotification = (long) document.get("counter") ;
                            counterNotification = counterNotification + 1 ;

                            databaseFireStore.collection("System").document("Counter")
                                    .collection("NotificationCounter").document(reference2Uid)
                                    .update("counter",counterNotification) ;
                        }
                    }) ;
                }

                ApproveAndBlockInfo approveAndBlockInfo = new ApproveAndBlockInfo("waiting");
                myRefApprove.setValue(approveAndBlockInfo);

                notificationInfo.setTypes("newAccount"); ;
                myRefNotification3.push().setValue(notificationInfo) ;

                Map<String,Object> map = new HashMap<>() ;
                map.put("counter",0) ;
                map.put("oldCounter",0) ;
                map.put("messageCounter", 0) ;
                map.put("messageOldCounter", 0) ;
                databaseFireStore.collection("System").document("Counter")
                        .collection("NotificationCounter").document(firebaseUser.getUid())
                        .set(map) ;

                goToTutorSignUpActivityStep3();
            }
        }
        else {
            ApproveAndBlockInfo approveAndBlockInfo = new ApproveAndBlockInfo("waiting");
            myRefApprove.setValue(approveAndBlockInfo);

            notificationInfo.setTypes("newAccount"); ;
            myRefNotification3.push().setValue(notificationInfo) ;

            Map<String,Object> map = new HashMap<>() ;
            map.put("counter",0) ;
            map.put("oldCounter",0) ;
            map.put("messageCounter", 0) ;
            map.put("messageOldCounter", 0) ;
            databaseFireStore.collection("System").document("Counter")
                    .collection("NotificationCounter").document(firebaseUser.getUid())
                    .set(map) ;

            goToTutorSignUpActivityStep3();
        }
    }

    public void uploadFinish(View view){
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        if (filePath != null) {
            final StorageReference imageRef = storageReference.child("idCardImage/" + emailPrimaryKey);

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] data = baos.toByteArray();

                final UploadTask uploadTask = imageRef.putBytes(data);
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
        candidateTutorInfo.setIdCardImageUri(imageUriString);
        myRefCandidateTutor.setValue(candidateTutorInfo);
        uploadButton.setVisibility(View.GONE);
    }

    public void goToTutorSignUpActivityStep3 () {
        Intent intent = new Intent(this, TutorSignUpActivityStep3.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_adapter_for_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout takePhotoButton = dialog.findViewById(R.id.take_photo);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                takePhoto();
            }
        });

        LinearLayout choosePhotoButton = dialog.findViewById(R.id.choose_photo);
        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectImage();
            }
        });

        dialog.show();
    }
}
