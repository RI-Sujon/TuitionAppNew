package com.example.tuitionapp_surji.calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tuitionapp_surji.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalendarViewEventActivity extends AppCompatActivity
{

    private Serializable eventInfoList;// = (ArrayList<String>) getIntent().getSerializableExtra("key");
    private TextView title, dateTime,googleMeet, attendee, location, description, reminder ;
    private ArrayList<String> userInfo ;
    private DatabaseReference reference;
    private ArrayList<CalendarEventInfo> calendarEventInfos;

    private CalendarEventInfo calendarEventInfo;
    private String eventId;
    private com.google.api.services.calendar.Calendar service;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();

    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    private GoogleAccountCredential credential;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view_event);

        Toolbar toolbar= findViewById(R.id.event_view_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent() ;
        userInfo = intent.getStringArrayListExtra("userInfo") ;

        calendarEventInfos= new ArrayList<>();
        title = findViewById(R.id.set_title);
        dateTime = findViewById(R.id.set_datetime);
        googleMeet = findViewById(R.id.set_meet);
        attendee = findViewById(R.id.set_attendee);
        location = findViewById(R.id.set_location);
        description = findViewById(R.id.set_description);
        reminder = findViewById(R.id.set_reminder);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Events");//.child(userId);
        eventInfoList =  getIntent().getSerializableExtra("eventInfo");
        calendarEventInfo = (CalendarEventInfo) getIntent().getSerializableExtra("calendarEventInfo");
        //textView.setText(String.valueOf(eventInfoList));
        title.setText(calendarEventInfo.getEventTitle());
        dateTime.setText(calendarEventInfo.getDate()+"  .  "+calendarEventInfo.getStartTime()+" - "+ calendarEventInfo.getEndTime());
        attendee.setText(calendarEventInfo.getAttendee());
        location.setText(calendarEventInfo.getLocation());
        description.setText(calendarEventInfo.getDescription());
        reminder.setText("10 minutes before");


        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR)).setBackOff(new ExponentialBackOff());
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        System.out.println("Email               ===== "+firebaseUser.getEmail());
        credential.setSelectedAccountName(firebaseUser.getEmail());

        eventId = calendarEventInfo.getEventId();
        service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("Google-CalendarAndroidSample")
                .build();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarViewEventActivity.this, CalendarHomeActivity.class);
                intent.putStringArrayListExtra("userInfo", userInfo) ;
                startActivity(intent);
                finish();
            }
        });




        googleMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string = calendarEventInfo.getMeetingId();
                Uri conference = Uri.parse(string);
                Intent meetingIntent = new Intent(Intent.ACTION_VIEW, conference);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(meetingIntent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe)
                {
                    startActivity(meetingIntent);
                }

                else{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(
                            "https://play.google.com/store/apps/details?id=com.google.android.apps.meetings"));
                    intent.setPackage("com.android.vending");
                    startActivity(intent);
                }
            }
        });

    }

    /*  reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CalendarEventInfo calendarEventInfo = snapshot.getValue(CalendarEventInfo.class);
                    //MessageBoxInfo messageBoxInfo1 = snapshot.getValue(MessageBoxInfo.class);
                    if(calendarEventInfo.getEventCreatorId().equals(userId))
                    //System.out.println("Titleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee : "+calendarEventInfo.getEventTitle());
                    calendarEventInfos.add(calendarEventInfo);
                }

                reference.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    public void goBackToCalendarEvent(View view) {
        Intent intent = new Intent(this, CalendarHomeActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CalendarHomeActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_calendar_event_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_calendar_event:
                try {
                    deleteEvent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
                */
                return true;
        }

        return false;
    }

    private void deleteEvent() throws IOException {

        CalendarEventDeleteAsyncTask calendarEventDeleteAsyncTask = new CalendarEventDeleteAsyncTask(this,service,eventId);
        calendarEventDeleteAsyncTask.execute();
        Intent intent = new Intent(this, CalendarHomeActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }


}


