package com.cosc4319.adapti_project.utililities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private String eventTitle;
    private String eventDate;
    private boolean isAllDay;
    private String eventTime;

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String eventTitle, String eventDate, String eventTime, boolean isAllDay) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.isAllDay = isAllDay;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public String getEventTime() {return eventTime; }

    public String getDay() {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(eventDate);
            return new SimpleDateFormat("dd").format(date); // Extracts the day
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMonth() {
        try {
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(eventDate);
            return new SimpleDateFormat("MM").format(date); // Extracts the month
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
