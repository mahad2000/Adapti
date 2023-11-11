package com.cosc4319.adapti_project.ui.add_event;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventHelper {
    private DatabaseReference eventDatabase;
    private FirebaseAuth firebaseAuth;

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
        eventDatabase = firebaseDatabase.getReference("users"); // Reference to 'users' node

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void addEvent(String eventTitle, String eventDate, String eventTime, boolean isAllDay) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's events under their unique ID
            DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .child("events")
                    .push();

            String eventId = userEventsRef.getKey(); // Getting the unique key for the event

            if (isAllDay) {
                eventTime = "";
            }

            // Create an Event object to store the event data
            Event event = new Event(eventTitle, eventDate, eventTime, isAllDay);

            // Save the event data to the Firebase database under the user's 'events' node
            userEventsRef.setValue(event);
        } else {
            // Handle the case when no user is logged in
        }
    }


    // Define a model class for the event

}
