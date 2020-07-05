package com.example.tuitionapp_surji.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.tuitionapp_surji.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GuardianInformationActivity extends AppCompatActivity {

    private CircularImageView imageView ;

    private DatabaseReference myRefGuardian ;
    private TextInputEditText name, address ;

    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 120;
    private StorageReference storageReference;
    private Bitmap bitmapImage ;

    private ProgressDialog progressDialog ;


    private String nameString, addressString, mobileNumberString, profilePicUriString ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_information);
        progressDialog = new ProgressDialog(this) ;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;
        myRefGuardian = FirebaseDatabase.getInstance().getReference().child("Guardian").child(user.getUid()) ;
        storageReference = FirebaseStorage.getInstance().getReference() ;

        mobileNumberString = user.getPhoneNumber() ;

        name = findViewById(R.id.guardianName) ;
        address = findViewById(R.id.guardianAddress) ;

        imageView = findViewById(R.id.imgView) ;
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
            profilePicUriString = filePath.toString() ;
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmapImage);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFinish(){
        if (filePath != null) {
            final StorageReference imageRef = storageReference.child("guardianProfilePic/" + mobileNumberString );
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
                                GuardianInfo guardianInfo = new GuardianInfo(nameString, addressString, mobileNumberString, uri.toString() ) ;

                                myRefGuardian.setValue(guardianInfo) ;

                                goToGuardianHomePageActivity();
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

    public void registerGuardianInfo(View view){
        nameString = name.getText().toString() ;
        addressString = address.getText().toString() ;

        if(nameString.equals("")){
           name.setError("Please write your name.");
           return;
        }

        if(profilePicUriString!=null){
            uploadFinish();
        }
        else {
            GuardianInfo guardianInfo = new GuardianInfo(nameString, addressString, mobileNumberString, "1" ) ;

            myRefGuardian.setValue(guardianInfo) ;

            goToGuardianHomePageActivity();
        }
    }

    private void goToGuardianHomePageActivity(){
        Intent intent = new Intent(this, GuardianHomePageActivity.class);
        startActivity(intent);
        finish() ;
    }
}