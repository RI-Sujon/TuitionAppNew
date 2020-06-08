package com.example.tuitionapp_surji.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tuitionapp_surji.R;

import java.util.ArrayList;

public class CustomAdapterForCalendarEvents extends BaseAdapter {

    private Context context ;
    private ArrayList<CalendarEventInfo> calendarEventInfos ;

    public CustomAdapterForCalendarEvents(Context context, ArrayList<CalendarEventInfo> calendarEventInfos) {
        this.context = context;
        this.calendarEventInfos = calendarEventInfos;
    }

    @Override
    public int getCount() {
        return calendarEventInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomAdapterForCalendarEvents.EventViewHolder eventViewHolder;


        if(convertView==null){
            eventViewHolder = new  CustomAdapterForCalendarEvents.EventViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_adapter_for_calendar_events_list_view, null);

            eventViewHolder.eventTitle = convertView.findViewById(R.id.custom_event_title);
            eventViewHolder.date = convertView.findViewById(R.id.custom_event_date);

            convertView.setTag(eventViewHolder);
        }else{
            eventViewHolder = (CustomAdapterForCalendarEvents.EventViewHolder)convertView.getTag() ;
        }

        String title = calendarEventInfos.get(position).getEventTitle();
        String startTime = calendarEventInfos.get(position).getStartTime();
        String endTime = calendarEventInfos.get(position).getEndTime();
        String date = calendarEventInfos.get(position).getDate();

        if(title!=null && startTime!=null && endTime!=null && date !=null){
            eventViewHolder.eventTitle.setText(title);
            eventViewHolder.date.setText(date+" - "+startTime+"-"+endTime);
        }

        return convertView;
    }




    class EventViewHolder{
        TextView eventTitle;
        TextView date;
    }
}
