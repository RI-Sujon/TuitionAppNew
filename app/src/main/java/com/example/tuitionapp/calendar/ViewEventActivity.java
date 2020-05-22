package com.example.tuitionapp_nadim.calendar;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuitionapp_nadim.R;

import java.util.ArrayList;

public class ViewEventActivity extends AppCompatActivity{

    ArrayList<String> eventInfoList;// = (ArrayList<String>) getIntent().getSerializableExtra("key");
    TextView title, dateTime,googleMeet, attendee, location, description, reminder ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        title = findViewById(R.id.set_title);
        dateTime = findViewById(R.id.set_datetime);
        googleMeet = findViewById(R.id.set_meet);
        attendee = findViewById(R.id.set_attendee);
        location = findViewById(R.id.set_location);
        description = findViewById(R.id.set_description);
        reminder = findViewById(R.id.set_reminder);

        /*  eventInfoList.add(eventTitle_txt);
        eventInfoList.add(date_txt);
        eventInfoList.add(startTime_txt);
        eventInfoList.add(endTime_txt);
        eventInfoList.add(meetingId);
        eventInfoList.add(attendees_txt);
        eventInfoList.add(location_txt);
        eventInfoList.add(description_txt);*/

        eventInfoList = (ArrayList<String>) getIntent().getSerializableExtra("eventInfo");
        //textView.setText(String.valueOf(eventInfoList));

        title.setText(eventInfoList.get(0));
        dateTime.setText(eventInfoList.get(1)+" , "+eventInfoList.get(2)+"-"+ eventInfoList.get(3));
        attendee.setText(eventInfoList.get(5));
        location.setText(eventInfoList.get(6));
        description.setText(eventInfoList.get(7));
        reminder.setText("10 minutes before");


        //System.out.println(eventInfoList.get(4));

       /* googleMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string = eventInfoList.get(4);
                Uri conference = Uri.parse(string);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, conference);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe)
                {
                    startActivity(mapIntent);
                }
            }
        });*/

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
