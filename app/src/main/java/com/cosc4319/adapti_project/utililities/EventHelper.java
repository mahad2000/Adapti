package com.cosc4319.adapti_project.utililities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventHelper {
    private DatabaseReference eventDatabase;
    private FirebaseAuth firebaseAuth;


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
