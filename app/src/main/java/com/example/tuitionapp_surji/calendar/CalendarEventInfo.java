package com.example.tuitionapp_surji.calendar;

public class CalendarEventInfo
{
    String eventCreatorId, meetingId, eventId;

    public CalendarEventInfo() {
    }

    public CalendarEventInfo(String eventCreatorId, String meetingId, String eventId) {
        this.eventCreatorId = eventCreatorId;
        this.meetingId = meetingId;
        this.eventId = eventId;
    }

    public String getEventCreatorId() {
        return eventCreatorId;
    }

    public void setEventCreatorId(String eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
