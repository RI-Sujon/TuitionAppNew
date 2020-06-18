package com.example.tuitionapp_surji.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tuitionapp_surji.R;
import com.example.tuitionapp_surji.verified_tutor.VerifiedTutorHomePageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class CalendarHomeActivity extends AppCompatActivity {

    private ArrayList<String> userInfo ;
    private ArrayList<CalendarEventInfo> calendarEventInfos ;
    private ListView eventListView ;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_home);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;
        
        calendarEventInfos = new ArrayList<>();
        eventListView = findViewById(R.id.events_list_view);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Events");

        
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CalendarEventInfo calendarEventInfo = snapshot.getValue(CalendarEventInfo.class);
                    //MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);
                    if(calendarEventInfo.getEventCreatorId().equals(userId))
                    calendarEventInfos.add(calendarEventInfo);
                    
                    goToEventsListView();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              viewTheSelectedEvent(calendarEventInfos.get(position));
            }
        });

    }

    private void viewTheSelectedEvent(CalendarEventInfo calendarEventInfo) {
        Intent intent = new Intent(this,ViewEventActivity.class);
        intent.putExtra("calendarEventInfo", (Serializable) calendarEventInfo);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    private void goToEventsListView() {
        CustomAdapterForCalendarEvents adapter = new CustomAdapterForCalendarEvents(this,calendarEventInfos);
        eventListView.setAdapter(adapter);
    }

    public void goBackToHomePage(View view) {
        Intent intent = new Intent(this, VerifiedTutorHomePageActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    public void addCalendarEvent(View view) {
        Intent intent = new Intent(this, CalendarSampleActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }
}
