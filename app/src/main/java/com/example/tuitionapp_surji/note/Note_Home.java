package com.example.tuitionapp_surji.note;

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

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Note_Home extends AppCompatActivity {


    private DatabaseReference myRefNote ;
    private StorageReference pdfStorage ;
    final static int PICK_PDF_CODE = 2342 ;

    private ArrayList<String> userInfo ;

    MaterialToolbar note_tool_bar;
    Button addNoteButton, addNoteAttachmentButton;
    private ArrayList<NoteInfo> addNoteInfoArrayList ;

    private TextView noteAttachmentTextView ;

    private TextView noteTitleTV, noteBodyTV ;
    private TextInputEditText noteTitleEditText ;
    private TextInputEditText notePostEditText ;
    private TextInputLayout noteTitleEditTextLayout ;
    private TextInputLayout notePostEditTextLayout ;

    private ListView noteListView ;

    private String note_title ;
    private String note_post ;
    private Uri filePath ;
    private String uriString ;
    private String pdfName ;
    //private String groupID ,user ,userEmail;
    private Menu toolbarMenu ;



    private Calendar noteCalendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormate1 = new SimpleDateFormat("h:mm a") ;
    private SimpleDateFormat simpleDateFormate2 = new SimpleDateFormat("E, dd MMM yyyy") ;
    private String noteDate;
    private String noteTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_home);

        Intent intent = getIntent() ;
       // user = intent.getStringExtra("user") ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
       // userEmail = intent.getStringExtra("userEmail") ;


        note_tool_bar = findViewById(R.id.noteTopAppBar);
        addNoteButton = findViewById(R.id.addNoteButton);
        addNoteAttachmentButton = findViewById(R.id.addNoteAttachmentButton);

        noteTitleTV = findViewById(R.id.note_title_heading) ;
        noteTitleEditText = findViewById(R.id.note_title_edit_text) ;
        noteTitleEditTextLayout = findViewById(R.id.note_titleInputLayout) ;

        noteBodyTV = findViewById(R.id.note_body_heading) ;
        notePostEditText = findViewById(R.id.note_post_edit_text) ;
        notePostEditTextLayout = findViewById(R.id.note_postInputLayout) ;

        noteAttachmentTextView = findViewById(R.id.note_attachment_text_view) ;
        noteListView = findViewById(R.id.note_list_view) ;

        myRefNote = FirebaseDatabase.getInstance().getReference("Note") ;
        pdfStorage = FirebaseStorage.getInstance().getReference() ;

        addNoteInfoArrayList = new ArrayList<>() ;

        toolbarMenu = note_tool_bar.getMenu() ;

        toolbarMenu.findItem(R.id.note_addPost).setVisible(false);
        toolbarMenu.findItem(R.id.note_addAttachment).setVisible(false);


        myRefNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dS1: dataSnapshot.getChildren()){
                    NoteInfo noteInfo = dS1.getValue(NoteInfo.class) ;
                        addNoteInfoArrayList.add(noteInfo) ;

                    goToNoteListView();
                }
                myRefNote.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPost = addNoteInfoArrayList.get(position).getNote_post() ;
                if(selectedPost==null){
                    String selectedPdfName = addNoteInfoArrayList.get(position).getNotePdfName() ;
                    String selectedPdfUri = addNoteInfoArrayList.get(position).getNotePdfUri() ;
                    downloadPDF(Note_Home.this,selectedPdfName,selectedPdfUri);
                }
            }
        });



        note_tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackPageActivity();
            }
        });

        note_tool_bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.note_post:
                        prepareForPostOperation();
                        break;

                    case R.id.note_attachment:
                        getPDF();
                        break;

                    case R.id.note_addPost:
                        addPostOperation(null);
                        break;

                    case R.id.note_addAttachment:
                        uploadPDF(null);
                        break;

                }
                return true;
            }
        });

    }

    public void goToNoteListView(){
        CustomAdapterForNote adapter = new CustomAdapterForNote(this,addNoteInfoArrayList) ;
        noteListView.setAdapter(adapter);
    }




    private void prepareForPostOperation(){
        toolbarMenu.findItem(R.id.note_post).setVisible(false);
        toolbarMenu.findItem(R.id.note_attachment).setVisible(false);
        toolbarMenu.findItem(R.id.note_addPost).setVisible(true);
        note_tool_bar.setTitle("ADD A NOTE");

        noteTitleTV.setVisibility(View.VISIBLE);
        noteBodyTV.setVisibility(View.VISIBLE);
        noteTitleEditTextLayout.setVisibility(View.VISIBLE);
        notePostEditTextLayout.setVisibility(View.VISIBLE);
        addNoteButton.setVisibility(View.VISIBLE);
        noteListView.setVisibility(View.GONE);
    }

    private void prepareForAttachmentOperation(){
        toolbarMenu.findItem(R.id.note_post).setVisible(false);
        toolbarMenu.findItem(R.id.note_attachment).setVisible(false);
        toolbarMenu.findItem(R.id.note_addAttachment).setVisible(true);
        note_tool_bar.setTitle("Add An Attachment");

        noteTitleTV.setVisibility(View.VISIBLE);
        noteBodyTV.setVisibility(View.VISIBLE);
        noteTitleEditTextLayout.setVisibility(View.VISIBLE);
        noteAttachmentTextView.setVisibility(View.VISIBLE);
        addNoteAttachmentButton.setVisibility(View.VISIBLE);
        noteListView.setVisibility(View.GONE);

        noteAttachmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });

    }


    public void addPostOperation(View view){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        note_post = notePostEditText.getText().toString() ;
        note_title = noteTitleEditText.getText().toString() ;

        noteTime = simpleDateFormate1.format(noteCalendar.getTime());
        noteDate = simpleDateFormate2.format(noteCalendar.getTime());

        if(!note_post.equals("") || !note_title.equals("")){
            //NoteInfo noteInfo = new NoteInfo(note_title, note_post) ;
          //  myRefNote.push().setValue(noteInfo);
          //  databaseReference.child("Events").push().setValue(hashMap);

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("note_title",note_title);
            hashMap.put("note_post",note_post);
            hashMap.put("noteTime",noteTime);
            hashMap.put("noteDate",noteDate);
            databaseReference.child("Note").push().setValue(hashMap);

        }
        Intent intent = new Intent(this, Note_Home.class);
       // intent.putExtra("user",user) ;
        intent.putStringArrayListExtra("userInfo",userInfo) ;
        startActivity(intent);
        finish();
    }



    private void getPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
        prepareForAttachmentOperation();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData() ;
            noteAttachmentTextView.setText(filePath.toString());
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
        note_title = noteTitleEditText.getText().toString() ;

        NoteInfo noteInfo = new NoteInfo(note_title, uriString, pdfName) ;
        myRefNote.push().setValue(noteInfo) ;

        Intent intent = new Intent(this, Note_Home.class);
        //intent.putExtra("user",user) ;
       // intent.putExtra("groupID" , groupID) ;

        intent.putStringArrayListExtra("userInfo",userInfo) ;

        startActivity(intent);
        finish();
    }

    public void goToBackPageActivity(){
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
      //  intent.putExtra("groupID", groupID) ;
        intent.putStringArrayListExtra("userInfo",userInfo) ;
       // intent.putExtra("user", user) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity();
    }



}
