package com.example.tuitionapp_surji.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.tuitionapp_surji.guardian.GuardianModuleStartActivity;
import com.example.tuitionapp_surji.candidate_tutor.TutorSignInActivity;
import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.tuition_post.TuitionPostViewSinglePageActivity;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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

    double x1, x2, y1, y2 ;

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1<x2){
                Intent intent = new Intent(HomePageActivity.this, TuitionPostViewSinglePageActivity.class);
                startActivity(intent);
                finish();
            }else if(x1>x2){
                Intent intent = new Intent(HomePageActivity.this, GuardianModuleStartActivity.class);
                startActivity(intent);
                finish();
            }
            break;
        }
        return false;
    }
}
