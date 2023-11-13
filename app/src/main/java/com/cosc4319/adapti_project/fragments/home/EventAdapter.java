package com.cosc4319.adapti_project.fragments.home;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    // Include necessary methods like onCreateViewHolder, onBindViewHolder, and getItemCount
    // EventViewHolder will hold the views for each event item

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        // Hold views for an event item, e.g., TextViews for title, date, time, etc.
    }

}
