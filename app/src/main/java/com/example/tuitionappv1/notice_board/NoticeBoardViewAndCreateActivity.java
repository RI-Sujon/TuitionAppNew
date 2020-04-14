package com.example.tuitionappv1.notice_board;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionappv1.group.GroupHomePageActivity;
import com.example.tuitionappv1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class NoticeBoardViewAndCreateActivity extends AppCompatActivity {
    private DatabaseReference myRefNotice ;
    private StorageReference pdfStorage ;

    final static int PICK_PDF_CODE = 2342;

    private Button addPostOptionButton, addAttachmentOptionButton, addPostButton, addAttachmentButton ;
    private EditText postEditText ;
    private TextView attachmentTextView ;

    private ListView postListView ;

    private String post ;
    private Uri filePath ;
    private String uriString ;
    private String pdfName ;

    private ArrayList<NoticeInfo> addNoticeInfoArrayList ;

    private String groupID ,user ,userEmail;
    private ArrayList<String>userInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board_view_and_create);

        Intent intent = getIntent() ;
        groupID = intent.getStringExtra("groupID") ;
        user = intent.getStringExtra("user") ;
        if(user.equals("tutor")||user.equals("groupAdmin")){
            userInfo = intent.getStringArrayListExtra("userInfo") ;
            user = "tutor" ;
        }
        else if(user.equals("groupVisitor")){
            user = "guardian" ;
        }
        else {
            userEmail = intent.getStringExtra("userEmail") ;
        }

        addPostButton = findViewById(R.id.addPostButton) ;
        addAttachmentButton = findViewById(R.id.addAttachmentButton) ;
        addPostOptionButton = findViewById(R.id.addPostOption) ;
        addAttachmentOptionButton = findViewById(R.id.addAttachmentOption) ;
        postEditText = findViewById(R.id.post) ;
        attachmentTextView = findViewById(R.id.attachment) ;
        postListView = findViewById(R.id.tutorListView) ;

        myRefNotice = FirebaseDatabase.getInstance().getReference("Notice") ;
        pdfStorage = FirebaseStorage.getInstance().getReference() ;

        addNoticeInfoArrayList = new ArrayList<>() ;

        myRefNotice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    NoticeInfo noticeInfo = dS1.getValue(NoticeInfo.class) ;
                    if(noticeInfo.getGroupID().equals(groupID)){
                        addNoticeInfoArrayList.add(noticeInfo) ;
                    }
                    goToNoticeListView();
                }
                myRefNotice.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPost = addNoticeInfoArrayList.get(position).getPost() ;
                if(selectedPost.equals("")){
                    String selectedPdfName = addNoticeInfoArrayList.get(position).getPdfName() ;
                    String selectedPdfUri = addNoticeInfoArrayList.get(position).getPdfUri() ;
                    downloadPDF(NoticeBoardViewAndCreateActivity.this,selectedPdfName,selectedPdfUri);
                }
            }
        });

        addPostOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEditText.setVisibility(View.VISIBLE);
                addPostButton.setVisibility(View.VISIBLE);
                addPostOptionButton.setVisibility(View.GONE);
                addAttachmentOptionButton.setVisibility(View.GONE);
            }
        });
        addAttachmentOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachmentTextView.setVisibility(View.VISIBLE);
                addAttachmentButton.setVisibility(View.VISIBLE);
                addPostOptionButton.setVisibility(View.GONE);
                addAttachmentOptionButton.setVisibility(View.GONE);
                getPDF();
            }
        });

        if(user.equals("guardian")||user.equals("admin")){
            addPostOptionButton.setVisibility(View.GONE);
            addAttachmentOptionButton.setVisibility(View.GONE);
        }
    }

    public void goToNoticeListView(){
        CustomAdapterForNoticeBoard adapter = new CustomAdapterForNoticeBoard(this,addNoticeInfoArrayList) ;
        postListView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addPostOperation(View view){
        post = postEditText.getText().toString() ;
        if(!post.equals("")){
            NoticeInfo noticeInfo = new NoticeInfo(groupID, post, "","") ;
            myRefNotice.push().setValue(noticeInfo) ;
        }
        Intent intent = new Intent(this, NoticeBoardViewAndCreateActivity.class);
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        else if(user.equals("guardian")){
            intent.putExtra("userEmail" , userEmail) ;
        }
        startActivity(intent);
        finish();
    }

    private void getPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData() ;
            attachmentTextView.setText(filePath.toString());
        }
    }

    public void uploadPDF(View view) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            pdfName = String.valueOf(System.currentTimeMillis()) + ".pdf";
            final StorageReference pdfRef = pdfStorage.child("pdfNotice/" + pdfName );

            pdfRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(Uri uri) {
                            uriString = uri.toString() ;
                            addAttachmentOperationCompletion();
                        }
                    });
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

    public void downloadPDF(Context context, String selectedPdfName, String selectedPdfUri){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(selectedPdfUri) ;
        System.out.println("kkkkkkkkk"+ selectedPdfName);
        DownloadManager.Request request = new DownloadManager.Request(uri) ;
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) ;
        request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,selectedPdfName) ;

        downloadManager.enqueue(request) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addAttachmentOperationCompletion(){
        NoticeInfo noticeInfo = new NoticeInfo(groupID,"" ,uriString,pdfName) ;

        myRefNotice.push().setValue(noticeInfo) ;

        Intent intent = new Intent(this, NoticeBoardViewAndCreateActivity.class);
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        else if(user.equals("guardian")){
            intent.putExtra("userEmail" , userEmail) ;
        }
        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, GroupHomePageActivity.class);
        intent.putExtra("groupID", groupID) ;
        if(user.equals("tutor")){
            intent.putStringArrayListExtra("userInfo",userInfo) ;
        }
        intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }
}
