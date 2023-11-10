package com.cosc4319.adapti_project.ui.add_event;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventHelper {
    private DatabaseReference eventDatabase;

    public static class Event {
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
    }

    public EventHelper() {
        // Initialize Firebase Database reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        eventDatabase = firebaseDatabase.getReference("events");
    }

    public void addEvent(String eventTitle, String eventDate, String eventTime, boolean isAllDay) {
        // Generate a unique key for the event
        String eventId = eventDatabase.push().getKey();
        if (isAllDay) {
            eventTime = "";
        }

        // Create an Event object to store the event data
        Event event = new Event(eventTitle, eventDate, eventTime, isAllDay);

        // Save the event data to the Firebase database under the unique key
        eventDatabase.child(eventId).setValue(event);
    }

    // Define a model class for the event

}
