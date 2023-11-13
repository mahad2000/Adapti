package com.cosc4319.adapti_project.utililities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cosc4319.adapti_project.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventTitle, eventDay, eventMonth, eventTime;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDay = itemView.findViewById(R.id.eventDay);
            eventMonth = itemView.findViewById(R.id.eventMonth);
            eventTime = itemView.findViewById(R.id.eventTime);
        }

        public void bind(Event event) {
            // Bind your data to the views
            eventTitle.setText(event.getEventTitle());
            eventDay.setText(event.getDay());
            eventMonth.setText(event.getMonth());
            eventTime.setText(event.getEventTime());
        }
    }
}
