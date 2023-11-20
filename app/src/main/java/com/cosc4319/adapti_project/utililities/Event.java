package com.cosc4319.adapti_project.utililities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private String eventTitle;
    private Date eventDate;
    private boolean allDay;
    private String eventTime;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String eventTitle, Date eventDate, String eventTime, boolean allDay) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.allDay = allDay;
    }

    public static Event createFromFirebaseData(String eventTitle, String eventDateString, String eventTime, boolean isAllDay) {
        Date eventDate = parseDateString(eventDateString);
        return new Event(eventTitle, eventDate, eventTime, isAllDay);
    }

    private static Date parseDateString(String dateString) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getDay() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        return dayFormat.format(eventDate); // Extracts the day
    }

    public String getMonth() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        return monthFormat.format(eventDate); // Extracts the month
    }
}
