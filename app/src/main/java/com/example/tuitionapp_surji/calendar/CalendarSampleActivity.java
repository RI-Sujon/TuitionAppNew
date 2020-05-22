package com.example.tuitionapp_surji.calendar;

/*
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CalendarSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
*/

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import com.example.tuitionapp_surji.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class CalendarSampleActivity extends Activity  {

    /**
     * Logging level for HTTP requests/responses.
     *
     * <p>
     * To turn on, set to {@link Level#CONFIG} or {@link Level#ALL} and run this from command line:
     * </p>
     *
     * <pre>
     adb shell setprop log.tag.HttpTransport DEBUG
     * </pre>
     */
    private static final Level LOGGING_LEVEL = Level.OFF;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final String TAG = "CalendarSampleActivity";

    private static final int CONTEXT_EDIT = 0;

    private static final int CONTEXT_DELETE = 1;

    private static final int CONTEXT_BATCH_ADD = 2;

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;

    static final int REQUEST_AUTHORIZATION = 1;

    static final int REQUEST_ACCOUNT_PICKER = 2;

    private final static int ADD_OR_EDIT_CALENDAR_REQUEST = 3;

    private static final int ADD_EVENT= 4;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();

    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    GoogleAccountCredential credential;

    CalendarModel model = new CalendarModel();

    ArrayAdapter<CalendarInfo> adapter;

    com.google.api.services.calendar.Calendar client;

    int numAsyncTasks;

    private ListView listView;

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    com.google.api.services.calendar.Calendar service;

    String meetingId;

    ArrayList<String> eventInfoList = new ArrayList<>();





    MaterialEditText eventTitle, location, description, date, startTime, endTime, attendees;
    Button submitButton;

    String eventTitle_txt, location_txt, description_txt, date_txt, startTime_txt, endTime_txt, attendees_txt;
    DatePicker datePicker;
    TimePicker startTimePicker;
    TimePicker endTimePicker;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // enable logging
        Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);

        setContentView(R.layout.activity_calendar_sample);
        //listView = findViewById(R.id.list);
        //registerForContextMenu(listView);


        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("Google-CalendarAndroidSample")
                .build();


        eventTitle = findViewById(R.id.eventTitle);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        datePicker = findViewById(R.id.datePicker);
        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);
        attendees = findViewById(R.id.attendees);




    }

    void refreshView()
    {
        adapter = new ArrayAdapter<CalendarInfo>(
                this, android.R.layout.simple_list_item_1, model.toSortedArray()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // by default it uses toString; override to use summary instead
                TextView view = (TextView) super.getView(position, convertView, parent);
                CalendarInfo calendarInfo = getItem(position);
                view.setText(calendarInfo.summary);
                return view;
            }
        };
        //listView.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (checkGooglePlayServicesAvailable())
        {
            haveGooglePlayServices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    AsyncLoadCalendars.run(this);
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        AsyncLoadCalendars.run(this);
                    }
                }
                break;
            case ADD_OR_EDIT_CALENDAR_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Calendar calendar = new Calendar();
                    calendar.setSummary(data.getStringExtra("summary"));
                    String id = data.getStringExtra("id");
                    if (id == null) {
                        new AsyncInsertCalendar(this, calendar).execute();
                    } else {
                        calendar.setId(id);
                        new AsyncUpdateCalendar(this, id, calendar).execute();
                    }
                }
                break;

            case ADD_EVENT:
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                    //new CalendarEventAsyncTask(client).execute();
                }
                break;

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                AsyncLoadCalendars.run(this);
                break;
            case R.id.menu_accounts:
                chooseAccount();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_EDIT, 0, R.string.edit);
        menu.add(0, CONTEXT_DELETE, 0, R.string.delete);
        menu.add(0, CONTEXT_BATCH_ADD, 0, R.string.batchadd);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        int calendarIndex = (int) info.id;
        if (calendarIndex < adapter.getCount()) {
            final CalendarInfo calendarInfo = adapter.getItem(calendarIndex);
            switch (item.getItemId()) {
                case CONTEXT_EDIT:
                    startAddOrEditCalendarActivity(calendarInfo);
                    return true;
                case CONTEXT_DELETE:
                    new AlertDialog.Builder(this).setTitle(R.string.delete_title)
                            .setMessage(calendarInfo.summary)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    new AsyncDeleteCalendar(CalendarSampleActivity.this, calendarInfo).execute();
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create()
                            .show();
                    return true;
                case CONTEXT_BATCH_ADD:
                    List<Calendar> calendars = new ArrayList<Calendar>();
                    for (int i = 0; i < 3; i++) {
                        Calendar cal = new Calendar();
                        cal.setSummary(calendarInfo.summary + " [" + (i + 1) + "]");
                        calendars.add(cal);
                    }
                    new AsyncBatchInsertCalendars(this, calendars).execute();
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }


    /** Check that Google Play services APK is installed and up to date. */
    private boolean checkGooglePlayServicesAvailable() {
        GoogleApiAvailability g1 = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = g1.isGooglePlayServicesAvailable(this);
        if (GoogleApiAvailability.getInstance().isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }

    private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        } else {
            // load calendars
            AsyncLoadCalendars.run(this);
        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    public void onAddClick(View view) {
        startAddOrEditCalendarActivity(null);
    }

    private void startAddOrEditCalendarActivity(CalendarInfo calendarInfo) {
        Intent intent = new Intent(this, AddOrEditCalendarActivity.class);
        if (calendarInfo != null) {
            intent.putExtra("id", calendarInfo.id);
            intent.putExtra("summary", calendarInfo.summary);
        }
        startActivityForResult(intent, ADD_OR_EDIT_CALENDAR_REQUEST);
    }


    public void onAddEventClick(View view) {
        startAddOrEditEventCalendarActivity(null);
    }


    public void startAddOrEditEventCalendarActivity(CalendarInfo calendarInfo){

        Intent intent = new Intent(this, CalendarEventActivity.class);
        if (calendarInfo != null) {
            intent.putExtra("id", calendarInfo.id);
            intent.putExtra("summary", calendarInfo.summary);
        }

        startActivityForResult(intent, ADD_EVENT);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

    }



    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                Dialog dialog = googleApiAvailability.getErrorDialog(CalendarSampleActivity.this,
                        connectionStatusCode,  REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }

            //apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createEvent(View view)
    {
        eventTitle_txt =eventTitle.getText().toString();
        location_txt = location.getText().toString();
        description_txt = description.getText().toString();
        date_txt = getDate();
        startTime_txt = getTime(startTimePicker);
        endTime_txt = getTime(endTimePicker);
        attendees_txt = attendees.getText().toString();



      new CalendarEventAsyncTask(this,client,eventTitle_txt, location_txt, description_txt, date_txt, startTime_txt,
                endTime_txt, attendees_txt).execute();


        System.out.println(meetingId);

        eventInfoList.add(eventTitle_txt);
        eventInfoList.add(date_txt);
        eventInfoList.add(startTime_txt);
        eventInfoList.add(endTime_txt);
        eventInfoList.add(meetingId);
        eventInfoList.add(attendees_txt);
        eventInfoList.add(location_txt);
        eventInfoList.add(description_txt);

     Intent intent = new Intent(this,ViewEventActivity.class);
     intent.putExtra("eventInfo", eventInfoList);
     startActivity(intent);
     finish();
    }

    /*public void ReturnThreadResult(String meetingId) {
        this.meetingId= meetingId;
    }*/

    public  void setMeetingId(String meetingId){
        this.meetingId = meetingId;
    }

    public void GoToConference(View view)
    {
        //String meetingId = event.getHangoutLink();
        Uri conference = Uri.parse(meetingId);
        Intent meetingIntent = new Intent(Intent.ACTION_VIEW, conference);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(meetingIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe)
        {
            startActivity(meetingIntent);
        }
    }



    public String getDate()
    {
        StringBuilder stringBuilder  = new  StringBuilder();

        stringBuilder.append(datePicker.getYear()+"-");

        if(datePicker.getMonth() < 10){
            int a =datePicker.getMonth()+1;
                    stringBuilder.append("0"+a+"-");
        }
        else
            stringBuilder.append(datePicker.getMonth()+1+"-");


        if(datePicker.getDayOfMonth() <10){

            stringBuilder.append("0"+datePicker.getDayOfMonth());
        }

        else
            stringBuilder.append(datePicker.getDayOfMonth());


        return stringBuilder.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public String getTime(TimePicker timePicker)
    {
        StringBuilder stringBuilder  = new  StringBuilder();

        if(timePicker.getHour()<10){
            stringBuilder.append("0"+timePicker.getHour()+":");
        }

        else
            stringBuilder.append(timePicker.getHour()+":");


        if(timePicker.getMinute()<10){
            stringBuilder.append("0"+timePicker.getMinute());
        }

        else
            stringBuilder.append(timePicker.getMinute());

        stringBuilder.append(":00");

        return stringBuilder.toString();
    }




}

























