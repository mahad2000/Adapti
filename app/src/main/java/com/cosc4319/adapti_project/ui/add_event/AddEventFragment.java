package com.cosc4319.adapti_project.ui.add_event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cosc4319.adapti_project.DatePickerFragment;
import com.cosc4319.adapti_project.LoginActivity;
import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.SignupActivity;
import com.cosc4319.adapti_project.databinding.FragmentAddEventBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentAddEventBinding binding;
    private Switch allDaySwitch; // Add a member variable for the switch

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AddEventViewModel addEventViewModel = new ViewModelProvider(this).get(AddEventViewModel.class);
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        allDaySwitch = root.findViewById(R.id.all_day_switch); // Initialize the switch

        final TextView textView = binding.newEventName;
        addEventViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        TextView newEventDate = root.findViewById(R.id.new_event_date);
        newEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
            }
        });

        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the switch state (isChecked) here
                if (isChecked) {
                    // The switch is checked, so it's an all-day event
                    // You can set a boolean flag or update your data accordingly
                    // For example, set an "isAllDay" variable to true
                    boolean isAllDay = true;
                } else {
                    // The switch is unchecked, so it's not an all-day event
                    // You can set the "isAllDay" variable to false
                    boolean isAllDay = false;
                }
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

        TextView newEventDate = getView().findViewById(R.id.new_event_date);
        newEventDate.setText(currentDateString);
    }


    // Inside your activity or fragment





}