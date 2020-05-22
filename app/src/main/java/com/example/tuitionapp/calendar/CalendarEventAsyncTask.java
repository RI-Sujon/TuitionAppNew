package com.example.tuitionapp_nadim.calendar;

import android.os.AsyncTask;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class CalendarEventAsyncTask extends AsyncTask<Void, Void, String>
{

    String title;
    String location;
    String description;
    String date;
    String startTime;
    String endTime;
    String attendee;
    ArrayList<String> allAttendees = new ArrayList<>(50);
    String[] strings;

    com.google.api.services.calendar.Calendar service;

    String s1,s2;

    CalendarSampleActivity parent;



    public CalendarEventAsyncTask(CalendarSampleActivity parent,Calendar service,String title, String location, String description, String date, String startTime,
                                  String endTime, String attendee) {

        this.parent = parent;
        this.service = service;
        this.title = title;
        this.location = location;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendee = attendee;

    }


    @Override
    protected String doInBackground(Void... voids)
    {

        strings = attendee.split(",");

        for(String s:strings){
            allAttendees.add(s);
        }

        Event event = new Event()
                .setSummary(title)
                .setLocation(location)
                .setDescription(description);


        DateTime startDateTime = new DateTime( date +"T"+startTime+"+06:00" );//"2020-05-05T11:00:00+06:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Dhaka");
        event.setStart(start);

        DateTime endDateTime = new DateTime(date +"T"+endTime+"+06:00");//"2020-05-05T12:00:00+06:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Dhaka");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));

      /*  s1 = "rahimsumon29@gmail.com";
        s2 = "nadimahmed1028@gmail.com";

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(s1),
                new EventAttendee().setEmail(s2),
        };*/

        EventAttendee attendees[];

        attendees = new EventAttendee[allAttendees.size()];

        for(int i=0; i<allAttendees.size(); i++){
            System.out.println(allAttendees.get(i));
            attendees[i] = new EventAttendee().setEmail(allAttendees.get(i));
            //new EventAttendee().setEmail(allAttendees.get(i));
        }
        event.setAttendees(Arrays.asList(attendees));




        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };


        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";

        try {
            event = service.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String meetingId = event.getHangoutLink();
        //System.out.println(meetingId);

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        parent.setMeetingId(meetingId);

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");


        System.out.printf("Event created: %s\n", event.getHtmlLink());
        return meetingId;
    }

  /*  @Override
    protected void onPostExecute(String meetingId) {
        super.onPostExecute(meetingId);

        //parent.ReturnThreadResult(meetingId);

    }*/
}



/*try {
                        service = new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                                .setApplicationName("Event Calendar")
                                .build();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Event event = new Event()
                            .setSummary("Google I/O 2015")
                            .setLocation("800 Howard St., San Francisco, CA 94103")
                            .setDescription("A chance to hear more about Google's developer products.");

                    DateTime startDateTime = new DateTime("2020-04-28T09:00:00-07:00");
                    EventDateTime start = new EventDateTime()
                            .setDateTime(startDateTime)
                            .setTimeZone("America/Los_Angeles");
                    event.setStart(start);

                    DateTime endDateTime = new DateTime("2020-04-28T17:00:00-07:00");
                    EventDateTime end = new EventDateTime()
                            .setDateTime(endDateTime)
                            .setTimeZone("America/Los_Angeles");
                    event.setEnd(end);

                    String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
                    event.setRecurrence(Arrays.asList(recurrence));

                    EventAttendee[] attendees = new EventAttendee[] {
                            new EventAttendee().setEmail("bsse1028@iit.du.ac.bd"),
                            new EventAttendee().setEmail("nadimahmed1028@gmail.com"),
                    };
                    event.setAttendees(Arrays.asList(attendees));

                    EventReminder[] reminderOverrides = new EventReminder[] {
                            new EventReminder().setMethod("email").setMinutes(24 * 60),
                            new EventReminder().setMethod("popup").setMinutes(10),
                    };
                    Event.Reminders reminders = new Event.Reminders()
                            .setUseDefault(false)
                            .setOverrides(Arrays.asList(reminderOverrides));
                    event.setReminders(reminders);

                    String calendarId = "primary";

                    try {
                        event = service.events().insert(calendarId, event).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.printf("Event created: %s\n", event.getHtmlLink());
*/