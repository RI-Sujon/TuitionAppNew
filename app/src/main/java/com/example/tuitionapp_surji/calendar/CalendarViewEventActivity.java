package com.example.tuitionapp_surji.calendar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp_surji.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewEventActivity extends AppCompatActivity
{

    private Serializable eventInfoList;// = (ArrayList<String>) getIntent().getSerializableExtra("key");
    private TextView title, dateTime,googleMeet, attendee, location, description, reminder ;
    private ArrayList<String> userInfo ;
    private DatabaseReference reference;
    private ArrayList<CalendarEventInfo> calendarEventInfos;

    private CalendarEventInfo calendarEventInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view_event);

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




        eventInfoList =  getIntent().getSerializableExtra("eventInfo");
        calendarEventInfo = (CalendarEventInfo) getIntent().getSerializableExtra("calendarEventInfo");
        //textView.setText(String.valueOf(eventInfoList));




        title.setText(calendarEventInfo.getEventTitle());
        dateTime.setText(calendarEventInfo.getDate()+"  .  "+calendarEventInfo.getStartTime()+" - "+ calendarEventInfo.getEndTime());
        attendee.setText(calendarEventInfo.getAttendee());
        location.setText(calendarEventInfo.getLocation());
        description.setText(calendarEventInfo.getDescription());
        reminder.setText("10 minutes before");




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
            }
        });

    }

    public void goBackToCalendarEvent(View view) {
        Intent intent = new Intent(this, CalendarHomeActivity.class);
        intent.putStringArrayListExtra("userInfo", userInfo) ;
        startActivity(intent);
        finish();
    }

  /*  public void goToMeet(View view) {
        Uri conference = Uri.parse(eventInfoList.get(4));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, conference);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe)
        {
            startActivity(mapIntent);
        }
    }*/


}


 /*  eventInfoList.add(eventTitle_txt);
        eventInfoList.add(date_txt);
        eventInfoList.add(startTime_txt);
        eventInfoList.add(endTime_txt);
        eventInfoList.add(meetingId);
        eventInfoList.add(attendees_txt);
        eventInfoList.add(location_txt);
        eventInfoList.add(description_txt);*/

   /*   title.setText(eventInfoList.get(0));
        dateTime.setText(eventInfoList.get(1)+" , "+eventInfoList.get(2)+"-"+ eventInfoList.get(3));
        attendee.setText(eventInfoList.get(5));
        location.setText(eventInfoList.get(6));
        description.setText(eventInfoList.get(7));
        reminder.setText("10 minutes before");*/