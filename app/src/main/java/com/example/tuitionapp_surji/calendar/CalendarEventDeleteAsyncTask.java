package com.example.tuitionapp_surji.calendar;

import android.os.AsyncTask;

import com.google.api.services.calendar.Calendar;

import java.io.IOException;

public class CalendarEventDeleteAsyncTask extends AsyncTask<Void, Void, String>
{
    private com.google.api.services.calendar.Calendar service;
    private CalendarViewEventActivity calendarViewEventActivity;
    String eventId;

    /*
    public EventDeleteAsyncTask() {
    }*/

    public CalendarEventDeleteAsyncTask(CalendarViewEventActivity calendarViewEventActivity, Calendar service, String eventId) {
        this.service = service;
        this.calendarViewEventActivity = calendarViewEventActivity;
        this.eventId = eventId;
    }

    @Override
    protected String doInBackground(Void... voids) {

        System.out.println("Event Id =========== "+ eventId);
        String calendarId = "primary";

        try {
            service.events().delete(calendarId, "eventId").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  eventId;
    }
}
