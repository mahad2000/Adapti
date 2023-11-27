package com.cosc4319.adapti_project.utililities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.cosc4319.adapti_project.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;
    private Context context;  // Add a reference to the context
    private final EventHelper eventHelper;

    public EventAdapter(List<Event> eventList, Context context) {
        // Sort the eventList based on dates
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                return event1.getEventDate().compareTo(event2.getEventDate());
            }
        });

        this.eventList = eventList;
        this.context = context;
        this.eventHelper = new EventHelper();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Binding data to the ViewHolder
        Event event = eventList.get(position);


        // Setting the event title
        holder.textEventTitle.setText(event.getEventTitle());

        // Formatting and setting the event date and time
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

        // Set click listeners for the images
        holder.editImage.setOnClickListener(v -> {
            // Handle click on edit image
            Toast.makeText(context, "Edit clicked for event at position " + position, Toast.LENGTH_SHORT).show();

            // Retrieve the clicked event
            Event clickedEvent = eventList.get(position);

            // Pass the event information to the AddEventFragment
            Bundle bundle = new Bundle();
            bundle.putString("eventName", clickedEvent.getEventTitle());
            bundle.putString("eventDate", new SimpleDateFormat("MM/dd/yyyy").format(clickedEvent.getEventDate()));
            bundle.putString("eventTime", clickedEvent.getEventTime());
            bundle.putBoolean("isAllDay", clickedEvent.isAllDay());
            bundle.putString("eventID", clickedEvent.getEventID()); // Add event ID if needed

            // Navigate to the AddEventFragment with the event information
            Navigation.findNavController(v).navigate(R.id.navigation_add, bundle);
        });

        holder.deleteImage.setOnClickListener(v -> {
            // Handle click on delete image
            showDeleteConfirmationDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        // Returning the total number of items in the data set
        return eventList.size();
    }

    // ViewHolder class for holding references to the views in each item
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView textEventTitle;
        public TextView textEventDateTime;
        public ImageView editImage;
        public ImageView deleteImage;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initializing TextViews and ImageViews from the layout
            textEventTitle = itemView.findViewById(R.id.textEventTitle);
            textEventDateTime = itemView.findViewById(R.id.textEventDateTime);
            editImage = itemView.findViewById(R.id.editImage);
            deleteImage = itemView.findViewById(R.id.deleteImage);
        }
    }

    private void showDeleteConfirmationDialog(int position) {
        Event event = eventList.get(position);
        String eventID = event.getEventID(); // Use the method to get the event ID

        Log.d("EventAdapter", "Attempting to delete event with ID: " + eventID); // Add this line

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this event?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle delete confirmation
                String del = "Attempt Delete: " + eventID;
                Log.d("EventAdapter", del);
                Toast.makeText(context, "Delete confirmed for event with ID " + eventID, Toast.LENGTH_SHORT).show();

                // Check if eventID is null before attempting to delete
                if (eventID != null) {
                    // Use the existing EventHelper instance instead of creating a new one
                    eventHelper.deleteEvent(eventID);

                    // Remove the event from the RecyclerView
                    eventList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Log.e("EventAdapter", "EventID is null. Cannot delete the event.");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
