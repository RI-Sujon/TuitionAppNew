package com.example.tuitionapp_surji.notice_board;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitionapp_surji.group.GroupHomePageActivity;
import com.example.tuitionapp_surji.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
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

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class NoticeBoardViewAndCreateActivity extends AppCompatActivity {
    private DatabaseReference myRefNotice ;
    private StorageReference pdfStorage ;

    final static int PICK_PDF_CODE = 2342 ;

    private Button addPostButton, addAttachmentButton ;
    private TextView attachmentTextView ;

    private TextView titleTV, bodyTV ;
    private TextInputEditText noticeTitleEditText ;
    private TextInputEditText postEditText ;
    private TextInputLayout noticeTitleEditTextLayout ;
    private TextInputLayout noticePostEditTextLayout ;

    private ListView postListView ;

    private String title ;
    private String post ;
    private Uri filePath ;
    private String uriString ;
    private String pdfName ;

    private ArrayList<NoticeInfo> addNoticeInfoArrayList ;

    private String groupID ,user ,userEmail;
    private ArrayList<String>userInfo ;

    private MaterialToolbar toolbar ;
    private Menu toolbarMenu ;

    private int pageFlag = 0;

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
            //user = "guardian" ;
        }
        else {
            userEmail = intent.getStringExtra("userEmail") ;
        }

        toolbar = findViewById(R.id.topAppBar) ;
        addPostButton = findViewById(R.id.addPostButton) ;
        addAttachmentButton = findViewById(R.id.addAttachmentButton) ;

        titleTV = findViewById(R.id.title_heading) ;
        noticeTitleEditText = findViewById(R.id.title_edit_text) ;
        noticeTitleEditTextLayout = findViewById(R.id.titleInputLayout) ;

        bodyTV = findViewById(R.id.body_heading) ;
        postEditText = findViewById(R.id.post_edit_text) ;
        noticePostEditTextLayout = findViewById(R.id.postInputLayout) ;

        attachmentTextView = findViewById(R.id.attachment_text_view) ;
        postListView = findViewById(R.id.notice_list_view) ;

        myRefNotice = FirebaseDatabase.getInstance().getReference("Notice") ;
        pdfStorage = FirebaseStorage.getInstance().getReference() ;

        addNoticeInfoArrayList = new ArrayList<>() ;

        toolbarMenu = toolbar.getMenu() ;
        toolbarMenu.findItem(R.id.addPostNotice).setVisible(false);
        toolbarMenu.findItem(R.id.addAttachmentNotice).setVisible(false);
        if(!user.equals("tutor")){
            toolbarMenu.findItem(R.id.post).setVisible(false);
            toolbarMenu.findItem(R.id.attachment).setVisible(false);
        }

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
                if(selectedPost==null){
                    String selectedPdfName = addNoticeInfoArrayList.get(position).getPdfName() ;
                    String selectedPdfUri = addNoticeInfoArrayList.get(position).getPdfUri() ;
                    downloadPDF(NoticeBoardViewAndCreateActivity.this,selectedPdfName,selectedPdfUri);
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPageActivity();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.post:
                        prepareForPostOperation();
                        break;

                    case R.id.attachment:
                        getPDF();
                        break;

                    case R.id.addPostNotice:
                        addPostOperation(null);
                        break;

                    case R.id.addAttachmentNotice:
                        uploadPDF(null);
                        break;

                }
                return true;
            }
        });
    }

    private void prepareForPostOperation(){
        pageFlag = 1 ;
        toolbarMenu.findItem(R.id.post).setVisible(false);
        toolbarMenu.findItem(R.id.attachment).setVisible(false);
        toolbarMenu.findItem(R.id.addPostNotice).setVisible(true);
        toolbar.setTitle("Add A Post");

        titleTV.setVisibility(View.VISIBLE);
        bodyTV.setVisibility(View.VISIBLE);
        noticeTitleEditTextLayout.setVisibility(View.VISIBLE);
        noticePostEditTextLayout.setVisibility(View.VISIBLE);
        addPostButton.setVisibility(View.VISIBLE);
        postListView.setVisibility(View.GONE);
    }

    private void prepareForAttachmentOperation(){
        pageFlag = 2 ;
        toolbarMenu.findItem(R.id.post).setVisible(false);
        toolbarMenu.findItem(R.id.attachment).setVisible(false);
        toolbarMenu.findItem(R.id.addAttachmentNotice).setVisible(true);
        toolbar.setTitle("Add An Attachment");

        titleTV.setVisibility(View.VISIBLE);
        bodyTV.setVisibility(View.VISIBLE);
        noticeTitleEditTextLayout.setVisibility(View.VISIBLE);
        attachmentTextView.setVisibility(View.VISIBLE);
        addAttachmentButton.setVisibility(View.VISIBLE);
        postListView.setVisibility(View.GONE);

        attachmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });

    }

    public void goToNoticeListView(){
        CustomAdapterForNoticeBoard adapter = new CustomAdapterForNoticeBoard(this,addNoticeInfoArrayList) ;
        postListView.setAdapter(adapter);
    }

    public void addPostOperation(View view){
        post = postEditText.getText().toString() ;
        title = noticeTitleEditText.getText().toString() ;

        if(!post.equals("") || !title.equals("")){
            NoticeInfo noticeInfo = new NoticeInfo(groupID, title, post) ;
            myRefNotice.push().setValue(noticeInfo) ;
        }
        Intent intent = new Intent(this, NoticeBoardViewAndCreateActivity.class);
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        intent.putStringArrayListExtra("userInfo",userInfo) ;

        startActivity(intent);
        finish();
    }

    private void getPDF() {
        prepareForAttachmentOperation();
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

        DownloadManager.Request request = new DownloadManager.Request(uri) ;
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) ;
        request.setDestinationInExternalFilesDir(context,DIRECTORY_DOWNLOADS,selectedPdfName) ;

        downloadManager.enqueue(request) ;
    }

    public void addAttachmentOperationCompletion(){
        title = noticeTitleEditText.getText().toString() ;

        NoticeInfo noticeInfo = new NoticeInfo(groupID, title, uriString, pdfName) ;
        myRefNotice.push().setValue(noticeInfo) ;

        Intent intent = new Intent(this, NoticeBoardViewAndCreateActivity.class);
        intent.putExtra("user",user) ;
        intent.putExtra("groupID" , groupID) ;

        intent.putStringArrayListExtra("userInfo",userInfo) ;

        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(){
        if(pageFlag==0){
            if(user.equals("tutor")){
                Intent intent = new Intent(this, GroupHomePageActivity.class);
                intent.putExtra("groupID", groupID) ;
                intent.putStringArrayListExtra("userInfo",userInfo) ;
                intent.putExtra("user", user) ;
                startActivity(intent);
            }
            finish();
        }
        else {
            toolbarMenu.findItem(R.id.post).setVisible(true);
            toolbarMenu.findItem(R.id.attachment).setVisible(true);
            toolbar.setTitle("NoticeBoard");
            titleTV.setVisibility(View.GONE);
            bodyTV.setVisibility(View.GONE);
            noticeTitleEditText.setText("");
            noticeTitleEditTextLayout.setVisibility(View.GONE);

            if(pageFlag==1){
                toolbarMenu.findItem(R.id.addPostNotice).setVisible(false);

                noticePostEditTextLayout.setVisibility(View.GONE);
                addPostButton.setVisibility(View.GONE);
                postListView.setVisibility(View.VISIBLE);

                postEditText.setText("");
                pageFlag = 0 ;
            }
            else if(pageFlag==2){
                toolbarMenu.findItem(R.id.addAttachmentNotice).setVisible(false);

                attachmentTextView.setText("Select a PDF File");
                attachmentTextView.setVisibility(View.GONE);
                addAttachmentButton.setVisibility(View.GONE);
                postListView.setVisibility(View.VISIBLE);
                pageFlag = 0 ;
            }
        }

    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity();
    }
}
