package com.example.tuitionapp.CandidateTutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp.R;
import com.example.tuitionapp.VerifiedTutor.VerifiedTutorInfo;
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

public class TutorSignUpActivityStep2 extends AppCompatActivity {

    private ImageView imageView;
    private TextView filePathView ;
    private EditText reference1, reference2, reference3 ;
    private Button uploadButton ;
    private Button gotoNextButton ;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 120;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser ;
    private DatabaseReference myRefCandidateTutor, myRefRefer, myRefVerifiedTutor ;

    private String emailPrimaryKey ;
    private String imageUriString ;
    private String reference1str, reference2str, reference3str ;

    ArrayList<VerifiedTutorInfo> verifiedTutorInfoList = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up_step2);
        getSupportActionBar().hide();


        imageView = findViewById(R.id.imgView) ;
        filePathView = findViewById(R.id.filePathName) ;
        uploadButton = findViewById(R.id.uploadButton) ;
        gotoNextButton = findViewById(R.id.nextButton) ;
        reference1 = findViewById(R.id.reference1) ;
        reference2 = findViewById(R.id.reference2) ;
        reference3 = findViewById(R.id.reference3) ;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefCandidateTutor = FirebaseDatabase.getInstance().getReference("CandidateTutor").child(firebaseUser.getUid()) ;
        myRefVerifiedTutor = FirebaseDatabase.getInstance().getReference("VerifiedTutor") ;
        myRefRefer = FirebaseDatabase.getInstance().getReference("Refer") ;

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
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadButton.setVisibility(View.VISIBLE);
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

            final StorageReference imageRef = storageReference.child("images/" + emailPrimaryKey);

            imageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUriString = uri.toString() ;
                            }
                        });
                        gotoNextButton.setEnabled(true);
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

    public void candidateTutorRegistrationCompletion(View view){
        reference1str = reference1.getText().toString() ;
        reference2str = reference2.getText().toString() ;
        reference3str = reference3.getText().toString() ;

        int flag1=0, flag2=0, flag3=0 ;
        if(!reference1str.equals("")){
            for(VerifiedTutorInfo vt: verifiedTutorInfoList){
                if(reference1str.equals(vt.getEmailPK())){
                    flag1 = 1 ;
                }
            }
        }
        else{
            flag1 = -1 ;
        }

        if(!reference2str.equals("")){
            for(VerifiedTutorInfo vt: verifiedTutorInfoList){
                System.out.println(reference2str+"\t\t"+vt.getEmailPK());
                if(reference2str.equals(vt.getEmailPK())){
                    flag2 = 1 ;
                }
            }
        }
        else{
            flag2 = -1 ;
        }

        if(!reference3str.equals("")){
            for(VerifiedTutorInfo vt: verifiedTutorInfoList){
                if(reference3str.equals(vt.getEmailPK())){
                    flag3 = 1 ;
                }
            }
        }
        else{
            flag3 = -1 ;
        }

        if(flag1!=-1 || flag2!=-1 || flag3!=-1){
            if(flag1==0){
                Toast.makeText(getApplicationContext(), "Reference1 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            }
            else if(flag2==0){
                Toast.makeText(getApplicationContext(), "Reference2 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            }
            else if(flag3==0){
                Toast.makeText(getApplicationContext(), "Reference3 Does not match with any valid tutor ID", Toast.LENGTH_SHORT).show();
            }
            else {
                if(flag1==1){
                    ReferInfo referInfo1= new ReferInfo(firebaseUser.getEmail(),reference1str);
                    myRefRefer.push().setValue(referInfo1) ;
                }
                if(flag2==1){
                    ReferInfo referInfo2= new ReferInfo(firebaseUser.getEmail(),reference1str);
                    myRefRefer.push().setValue(referInfo2) ;
                }
                if(flag3==1){
                    ReferInfo referInfo3= new ReferInfo(firebaseUser.getEmail(),reference1str);
                    myRefRefer.push().setValue(referInfo3) ;
                }
                goToCandidateTutorProfile();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "You need minimum 1 Reference", Toast.LENGTH_SHORT).show() ;
            goToCandidateTutorProfile();
        }
    }

    public void goToCandidateTutorProfile(){
        myRefCandidateTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CandidateTutorInfo candidateTutorInfo = dataSnapshot.getValue(CandidateTutorInfo.class) ;
                candidateTutorInfo.setIdCardImageUri(imageUriString);
                myRefCandidateTutor.setValue(candidateTutorInfo) ;
                myRefCandidateTutor.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        Intent intent = new Intent(this, CandidateTutorHomePageActivity.class);
        startActivity(intent);
        finish();
    }
}
