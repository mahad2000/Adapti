package com.cosc4319.adapti_project.fragments.add_event;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.navigation.Navigation;

import com.cosc4319.adapti_project.fragments.DatePickerFragment;
import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.databinding.FragmentAddEventBinding;
import com.cosc4319.adapti_project.utililities.EventHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, DatePickerFragment.DatePickerListener {

    private FragmentAddEventBinding binding;
    private TextView newEventDate;
    private Switch allDaySwitch;
    private String currentDateString;
    private Button saveEventButton;
    private TextView newEventTime;
    private final Calendar selectedTime = Calendar.getInstance();

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
        setHasOptionsMenu(true);

        // Check if there are arguments containing selectedDate
        if (getArguments() != null && getArguments().containsKey("selectedDate")) {
            Calendar selectedDate = (Calendar) getArguments().getSerializable("selectedDate");

            // Convert the selected date to a formatted string
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            currentDateString = dateFormat.format(selectedDate.getTime());

            // Log the selected date for debugging
            Log.d("AddEventFragment", "Selected Date: " + currentDateString);

            // Set the selected date to the newEventDate TextView
            binding.newEventDate.setText(currentDateString);
        }



        saveEventButton.setOnClickListener(new View.OnClickListener() {
            //Log.d("EventFragment", "Saving event");
            @Override
            public void onClick(View view) {
                saveEvent(); // Save the event information
            }
        });

        TextView discardEventText = root.findViewById(R.id.discardEventText);
        discardEventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the home page (pop the current fragment from the back stack)
                requireActivity().getSupportFragmentManager().popBackStack();
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
        String eventTime = newEventTime.getText().toString();
        boolean isAllDay = allDaySwitch.isChecked();

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

        // Navigate back to the home page
        Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the Home Button press
            // Navigate back to the home page
            Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}