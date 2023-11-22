package com.cosc4319.adapti_project.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                    if ( eventList.size() < 1 ){
                        titleTextView.setText("No Upcoming Events");
                    }
                }
            });
        }


        return view;
    }
}

