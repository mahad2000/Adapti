package com.cosc4319.adapti_project.ui.add_event;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.cosc4319.adapti_project.DatePickerFragment;
import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.databinding.FragmentAddEventBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, DatePickerFragment.DatePickerListener {

    private FragmentAddEventBinding binding;
    private TextView newEventDate;
    private Switch allDaySwitch;
    private String currentDateString;
    private Button saveEventButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        binding = FragmentAddEventBinding.bind(root);
        saveEventButton = root.findViewById(R.id.save_event_button);
        initializeUIElements();
        setupEventListeners();

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent(); // Save the event information
            }
        });

        return root;
    }
    private void saveEvent() {
        // Get the event information from your UI elements
        String eventTitle = binding.newEventName.getText().toString();
        String eventDate = currentDateString;
        boolean isAllDay = allDaySwitch.isChecked();

        // Save the event to Firebase using the EventHelper
        EventHelper eventHelper = new EventHelper();
        eventHelper.addEvent(eventTitle, eventDate, isAllDay);

        // Show a toast message indicating that the event is saved
        Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show();

        // Clear the UI elements or perform any other necessary actions to prepare for the next event
        binding.newEventName.setText(""); // Clear the event name
        allDaySwitch.setChecked(false); // Reset the all-day switch
        currentDateString = null; // Clear the selected date

        // Restart the fragment by replacing it with a new instance
        AddEventFragment newFragment = new AddEventFragment();
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(this.getId(), newFragment);
        fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
        fragmentTransaction.commit();
    }

    private void initializeUIElements() {
        newEventDate = binding.newEventDate;
        allDaySwitch = binding.allDaySwitch;

        // Initialize other UI elements if needed
    }

    private void setupEventListeners() {
        AddEventViewModel addEventViewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        final TextView newEventName = binding.newEventName;

        addEventViewModel.getText().observe(getViewLifecycleOwner(), newEventName::setText);

        newEventDate.setOnClickListener(view -> openDatePicker());

        allDaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> handleAllDaySwitch(isChecked));
    }

    private void openDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        ((DatePickerFragment) datePicker).setDatePickerListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
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
        updateDateInView(selectedDate);
    }

    private void updateDateInView(String selectedDate) {
        newEventDate.setText(selectedDate);
    }
}
