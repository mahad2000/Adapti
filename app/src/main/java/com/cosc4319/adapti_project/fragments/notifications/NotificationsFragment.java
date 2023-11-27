package com.cosc4319.adapti_project.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.speech.tts.TextToSpeech;

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.text.SimpleDateFormat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.utililities.Event;
import com.cosc4319.adapti_project.utililities.EventAdapter;
import com.cosc4319.adapti_project.utililities.EventHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EventHelper eventHelper;
    private TextView titleTextView;
    private TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize EventHelper
        eventHelper = new EventHelper();

        titleTextView = view.findViewById(R.id.title);

        // Retrieve events from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            eventHelper.getEvents(userId, new EventHelper.EventDataListener() {
                @Override
                public void onDataLoaded(List<Event> eventList) {
                    // Update the RecyclerView with the retrieved events
                    EventAdapter eventAdapter = new EventAdapter(eventList, getActivity());
                    recyclerView.setAdapter(eventAdapter);
                    if (eventList.size() < 1) {
                        titleTextView.setTextSize(30);
                        titleTextView.setText("No Upcoming Events");
                    }
                }
            });
        }
        textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle the error if the language is not supported or data is missing
                    }
                } else {
                    // Handle initialization failure
                }
            }
        });

        // Set up the button to read events
        Button readEventsButton = view.findViewById(R.id.readEventsButton);
        readEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readEvents();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void readEvents() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            eventHelper.getEvents(userId, new EventHelper.EventDataListener() {
                @Override
                public void onDataLoaded(List<Event> eventList) {
                    // Sort the eventList based on dates
                    Collections.sort(eventList, new Comparator<Event>() {
                        @Override
                        public int compare(Event event1, Event event2) {
                            return event1.getEventDate().compareTo(event2.getEventDate());
                        }
                    });

                    StringBuilder eventDetails = new StringBuilder();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

                    if (!eventList.isEmpty()) {
                        eventDetails.append("Your Upcoming Events:\n");
                    }

                    boolean isFirstEvent = true;
                    for (Event event : eventList) {
                        if (!isFirstEvent) {
                            eventDetails.append("Your Next Event:\n");
                        }
                        isFirstEvent = false;

                        String formattedDate = dateFormat.format(event.getEventDate());
                        eventDetails.append(event.getEventTitle())
                                .append(", Date: ").append(formattedDate);

                        if (event.isAllDay()) {
                            eventDetails.append(", Time: All Day");
                        } else {
                            eventDetails.append(", Time: ").append(event.getEventTime());
                        }
                        eventDetails.append(".\n");
                    }

                    if (!eventDetails.toString().isEmpty()) {
                        textToSpeech.speak(eventDetails.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeech.speak("No upcoming events", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            });
        }
    }

}