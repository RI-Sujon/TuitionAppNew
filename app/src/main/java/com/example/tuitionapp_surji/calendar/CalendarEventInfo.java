package com.example.tuitionapp_surji.calendar;

import java.io.Serializable;

public class CalendarEventInfo implements Serializable
{
    String eventCreatorId, meetingId, eventId,eventTitle,location,description,date,startTime,endTime,attendee,weekDay;

    public CalendarEventInfo() {
    }

    public CalendarEventInfo(String eventCreatorId, String meetingId, String eventId, String eventTitle, String location, String description,
                             String date, String startTime, String endTime, String attendee, String weekDay) {
        this.eventCreatorId = eventCreatorId;
        this.meetingId = meetingId;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.location = location;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendee = attendee;
        this.weekDay = weekDay;
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

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAttendee() {
        return attendee;
    }

    public void setAttendee(String attendee) {
        this.attendee = attendee;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }
}
