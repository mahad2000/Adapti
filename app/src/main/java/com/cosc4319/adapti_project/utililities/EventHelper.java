package com.cosc4319.adapti_project.utililities;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventHelper {
    private final DatabaseReference eventDatabase;
    private final FirebaseAuth firebaseAuth;

    public interface EventDataListener {
        void onDataLoaded(List<Event> eventList);
    }

    public EventHelper() {
        // Initialize Firebase Database reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        eventDatabase = firebaseDatabase.getReference("users"); // Reference to 'users' node

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void addEvent(String eventTitle, String eventDateString, String eventTime, boolean isAllDay) {
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

            // Parse the eventDate string to Date type
            Date eventDate = parseDateString(eventDateString);

            // Create an Event object to store the event data
            Event event = new Event(eventTitle, eventDate, eventTime, isAllDay);

            // Save the event data to the Firebase database under the user's 'events' node
            userEventsRef.setValue(event);
        } else {
            // Handle the case when no user is logged in
        }
    }

    public void getEvents(String userId, final EventDataListener listener) {
        DatabaseReference userEventsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("events");

        userEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                if (listener != null) {
                    listener.onDataLoaded(eventList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private Date parseDateString(String dateString) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
