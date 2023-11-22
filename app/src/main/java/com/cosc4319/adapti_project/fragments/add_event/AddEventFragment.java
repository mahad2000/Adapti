package com.cosc4319.adapti_project.fragments.add_event;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Locale;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, DatePickerFragment.DatePickerListener {

    private FragmentAddEventBinding binding;
    private EditText newEventName;
    private TextView newEventDate;
    private Switch allDaySwitch;
    private Button saveEventButton;
    private TextView newEventTime;
    private String currentDateString;
    private final Calendar selectedTime = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        binding = FragmentAddEventBinding.bind(root);
        currentDateString = null;

        saveEventButton = root.findViewById(R.id.save_event_button);
        initializeUIElements();
        setupEventListeners();
        if (getArguments() != null) {
            setEventDataFromVoiceCommand(getArguments());
        }

        if (getArguments() != null && getArguments().containsKey("selectedDate")) {
            handleSelectedDate(getArguments());
        }

        saveEventButton.setOnClickListener(view -> saveEvent());
        TextView discardEventText = root.findViewById(R.id.discardEventText);
        discardEventText.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());

        return root;
    }

    private void initializeUIElements() {
        newEventName = binding.newEventName;
        newEventDate = binding.newEventDate;
        newEventTime = binding.newEventTime;
        allDaySwitch = binding.allDaySwitch;
    }

    private void setEventDataFromVoiceCommand(Bundle arguments) {
        String eventName = arguments.getString("eventName", "");
        String eventDate = arguments.getString("eventDate", "");
        String eventTime = arguments.getString("eventTime", "");
        boolean isAllDay = arguments.getBoolean("isAllDay", false);

        newEventName.setText(eventName);
        currentDateString = eventDate;

        if (isAllDay) {
            newEventDate.setText(eventDate);
            newEventTime.setVisibility(View.GONE);
            allDaySwitch.setChecked(true);
        } else {
            newEventDate.setText(eventDate);
            newEventTime.setText(eventTime);
            newEventTime.setVisibility(View.VISIBLE);
            allDaySwitch.setChecked(false);
        }
    }

    private void handleSelectedDate(Bundle arguments) {
        Calendar selectedDate = (Calendar) arguments.getSerializable("selectedDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        currentDateString = dateFormat.format(selectedDate.getTime());
        Log.d("AddEventFragment", "Selected Date: " + currentDateString);
        binding.newEventDate.setText(currentDateString);
    }

    private void saveEvent() {
        String eventTitle = binding.newEventName.getText().toString().trim();  // Trim to handle spaces
        String eventDate = currentDateString;
        String eventTime = newEventTime.getText().toString().trim();  // Trim to handle spaces
        boolean isAllDay = allDaySwitch.isChecked();

        // Check if both eventTitle and currentDateString are not empty
        if (!eventTitle.isEmpty() && currentDateString != null) {
            EventHelper eventHelper = new EventHelper();

            // Check if the time is empty
            if (eventTime.isEmpty()) {
                // Show a dialog for saving without time or canceling
                showSaveWithoutTimeDialog(eventTitle, eventDate, isAllDay);
            } else {
                // Save the event with the provided time
                eventHelper.addEvent(eventTitle, eventDate, eventTime, isAllDay);

                binding.newEventName.setText("");
                allDaySwitch.setChecked(false);
                currentDateString = null;
                newEventTime.setText("");

                Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
            }
        } else {
            // Show a dialog for incomplete event information
            showIncompleteEventDialog();
        }
    }

    private void showSaveWithoutTimeDialog(final String eventTitle, final String eventDate, final boolean isAllDay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Save Without Time");
        builder.setMessage("Event time is not specified. Do you want to save it without time?");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the event without time
                EventHelper eventHelper = new EventHelper();
                eventHelper.addEvent(eventTitle, eventDate, "", isAllDay);

                binding.newEventName.setText("");
                allDaySwitch.setChecked(false);
                currentDateString = null;
                newEventTime.setText("");

                Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the save operation
                dialog.dismiss();
            }
        });

        builder.show();
    }
    private void showIncompleteEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Incomplete Event");

        // Check which condition is not met and set the appropriate message
        if (binding.newEventName.getText().toString().trim().isEmpty()) {
            builder.setMessage("Event title is empty. Please enter a title.");
        } else if (currentDateString == null) {
            builder.setMessage("Event date is not selected. Please select a date.");
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setupEventListeners() {
        AddEventViewModel addEventViewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        addEventViewModel.getText().observe(getViewLifecycleOwner(), newEventName::setText);

        binding.newEventDate.setOnClickListener(view -> openDatePicker());
        binding.newEventTime.setOnClickListener(view -> openTimePicker());
        allDaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            handleAllDaySwitch(isChecked);
            newEventTime.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });
    }

    private void openDatePicker() {
        DialogFragment datePicker = new DatePickerFragment();
        ((DatePickerFragment) datePicker).setDatePickerListener(this);
        datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
    }

    private void openTimePicker() {
        if (allDaySwitch.isChecked()) {
            return;
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> handleSelectedTime(hourOfDay, minute),
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }

    private void handleSelectedTime(int hourOfDay, int minute) {
        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedTime.set(Calendar.MINUTE, minute);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        newEventTime.setText(timeFormat.format(selectedTime.getTime()));
    }

    private void handleAllDaySwitch(boolean isChecked) {
        newEventTime.setText("All Day");
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
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
