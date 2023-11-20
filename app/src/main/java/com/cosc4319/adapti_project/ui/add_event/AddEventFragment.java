package com.cosc4319.adapti_project.ui.add_event;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.cosc4319.adapti_project.DatePickerFragment;
import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.databinding.FragmentAddEventBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, DatePickerFragment.DatePickerListener {

    private FragmentAddEventBinding binding;
    private TextView newEventDate;
    private Switch allDaySwitch;
    private String currentDateString;
    private Button saveEventButton;
    private TextView newEventTime;
    private Calendar selectedTime = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        binding = FragmentAddEventBinding.bind(root);
        saveEventButton = root.findViewById(R.id.save_event_button);
        initializeUIElements();
        setupEventListeners();
        if (getArguments() != null) {
            String eventName = getArguments().getString("eventName", "");
            String eventDate = getArguments().getString("eventDate", "");
            String eventTime = getArguments().getString("eventTime", "");
            boolean isAllDay = getArguments().getBoolean("isAllDay", false);

            Log.d("EventFragment", "Event Name: " + eventName);
            Log.d("EventFragment", "Event Date: " + eventDate);
            Log.d("EventFragment", "Event Time: " + eventTime);
            Log.d("EventFragment", "Event is All Day: " + isAllDay);

            setEventDataFromVoiceCommand(eventName, eventDate, eventTime, isAllDay);
        }

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            //Log.d("EventFragment", "Saving event");
            @Override
            public void onClick(View view) {
                saveEvent(); // Save the event information
            }
        });

        return root;
    }
    public void setEventDataFromVoiceCommand(String eventName, String eventDate, String eventTime, boolean isAllDay) {
        binding.newEventName.setText(eventName);
        currentDateString = eventDate;

        // Check if the event is marked as all day and set the UI elements accordingly
        if(isAllDay){
            // Instead of setting "All Day" as the date text, set the actual date received from voice command.
            // The eventDate parameter should be a date string.
            newEventDate.setText(eventDate);
            newEventTime.setVisibility(View.GONE); // Hide the time input since it's an all-day event.
            allDaySwitch.setChecked(true);
        } else {
            newEventDate.setText(eventDate); // Set the date text to the actual date.
            newEventTime.setText(eventTime); // Set the time text to the actual time.
            newEventTime.setVisibility(View.VISIBLE);
            allDaySwitch.setChecked(false);
        }

        // Regardless of whether the event is all day, set currentDateString to the actual date.
        currentDateString = eventDate;
    }
    private void saveEvent() {
        // Get the event information from your UI elements
        String eventTitle = binding.newEventName.getText().toString();
        // This will be an actual date string, suitable for saving in the database.
        String eventDate = currentDateString;
        // For all-day events, the time can be empty or a default string like "All Day".
        String eventTime = allDaySwitch.isChecked() ? "All Day" : newEventTime.getText().toString();

        // Save the event to Firebase using the EventHelper
        EventHelper eventHelper = new EventHelper();
        eventHelper.addEvent(eventTitle, eventDate, eventTime, allDaySwitch.isChecked());

        // Show a toast message indicating that the event is saved
        Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show();

        // Clear the UI elements or perform any other necessary actions to prepare for the next event
        binding.newEventName.setText(""); // Clear the event name
        allDaySwitch.setChecked(false); // Reset the all-day switch
        currentDateString = null; // Clear the selected date
        newEventTime.setText("");

        // Restart the fragment by replacing it with a new instance
        AddEventFragment newFragment = new AddEventFragment();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(this.getId(), newFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
        fragmentTransaction.commit();
    }

    private void initializeUIElements() {
        newEventDate = binding.newEventDate;
        newEventTime = binding.newEventTime;
        allDaySwitch = binding.allDaySwitch;

        // Initialize other UI elements if needed
    }

    private void setupEventListeners() {
        AddEventViewModel addEventViewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        final TextView newEventName = binding.newEventName;

        addEventViewModel.getText().observe(getViewLifecycleOwner(), newEventName::setText);

        newEventDate.setOnClickListener(view -> openDatePicker());
        newEventTime.setOnClickListener(view -> openTimePicker());
        allDaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleAllDaySwitch(isChecked);
            if (isChecked) {
                newEventTime.setVisibility(View.GONE); // Hide the time picker
            } else {
                newEventTime.setVisibility(View.VISIBLE); // Show the time picker
            }
        });
    }

    private void openDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        ((DatePickerFragment) datePicker).setDatePickerListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
    }
    private void openTimePicker() {
        if (allDaySwitch.isChecked()) {
            // If it is checked, do not open the time picker
            return;
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    // Handle the selected time
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minute);

                    // Format and display the selected time
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String formattedTime = timeFormat.format(selectedTime.getTime());
                    newEventTime.setText(formattedTime);
                },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false // set true if you want 24-hour format
        );

        timePickerDialog.show();
    }
    private void handleAllDaySwitch(boolean isChecked) {
        // Handle the switch state (isChecked) here
        boolean isAllDay = isChecked;

        // Additional logic can be added here if needed
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        updateDateInView(currentDateString);
    }

    @Override
    public void onDateSelected(String selectedDate) {
        currentDateString = selectedDate;
        updateDateInView(selectedDate);
    }

    private void updateDateInView(String selectedDate) {
        newEventDate.setText(selectedDate);
    }
}