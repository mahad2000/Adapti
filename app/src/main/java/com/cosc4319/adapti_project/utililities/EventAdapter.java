package com.cosc4319.adapti_project.utililities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cosc4319.adapti_project.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.textEventTitle.setText(event.getEventTitle());

        String dateTimeText;
        if (event.getEventDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = dateFormat.format(event.getEventDate());

            dateTimeText = event.isAllDay()
                    ? formattedDate
                    : formattedDate + " " + event.getEventTime();
        } else {
            // If the date is null, display "All Day" or any other message you prefer
            dateTimeText = "All Day";
        }

        holder.textEventDateTime.setText(dateTimeText);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView textEventTitle;
        public TextView textEventDateTime;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textEventTitle = itemView.findViewById(R.id.textEventTitle);
            textEventDateTime = itemView.findViewById(R.id.textEventDateTime);
        }
    }
}
