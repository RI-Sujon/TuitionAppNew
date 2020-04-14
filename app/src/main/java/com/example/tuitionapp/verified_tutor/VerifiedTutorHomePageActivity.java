package com.example.tuitionapp.verified_tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuitionapp.batch.BatchStudentInfoTableActivity;
import com.example.tuitionapp.group.GroupHomePageActivity;
import com.example.tuitionapp.R;
import com.example.tuitionapp.system.HomePageActivity;
import com.example.tuitionapp.tuition_post.TuitionPostViewActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VerifiedTutorHomePageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private GoogleSignInClient mGoogleSignInClient ;
    private FirebaseUser user ;

    ArrayList<String> userInfo ;
    ImageView userProfilePicImageView ;
    TextView userNameTextView, userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_tutor_home_page);
        //getSupportActionBar().hide();

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        userProfilePicImageView = findViewById(R.id.profilePicImageView) ;
        userNameTextView = findViewById(R.id.userName) ;
        userEmailTextView  = findViewById(R.id.userEmail) ;

        userNameTextView.setText(userInfo.get(0));
        Picasso.get().load(userInfo.get(1)).into(userProfilePicImageView) ;
        userEmailTextView.setText(userInfo.get(2));

        mAuth = FirebaseAuth.getInstance() ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        user = mAuth.getCurrentUser() ;

    }

    public void goToMessageBox(View view){
        Intent intent = new Intent(this, BatchStudentInfoTableActivity.class) ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorNotificationActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorNotificationActivity.class) ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorGroupActivity(View view){
        Intent intent = new Intent(this, GroupHomePageActivity.class) ;
        intent.putExtra("userEmail",user.getEmail()) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorProfileActivity(View view){
        Intent intent = new Intent(this, VerifiedTutorProfileActivity.class) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void goToVerifiedTutorViewPostActivity(View view){
        Intent intent = new Intent(this, TuitionPostViewActivity.class) ;
        intent.putExtra("user" , "tutor") ;
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void signOut(View view) {
        mAuth.signOut();
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        Intent intent = new Intent(this, HomePageActivity.class) ;
        startActivity(intent);
        finish();
    }
}
