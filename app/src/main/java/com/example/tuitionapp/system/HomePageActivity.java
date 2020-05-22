package com.example.tuitionapp.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionapp.guardian.GuardianModuleStartActivity;
import com.example.tuitionapp.candidate_tutor.TutorSignInActivity;
import com.example.tuitionapp.R;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_new);
    }

    public void goToGuardianModule(View view){
        Intent intent = new Intent(this, GuardianModuleStartActivity.class) ;
        startActivity(intent);
        finish();
    }

    public void goToTutorModule(View view){
        Intent intent = new Intent(this, TutorSignInActivity.class) ;
        startActivity(intent);
        finish();
    }
}
