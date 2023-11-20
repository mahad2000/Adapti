package com.cosc4319.adapti_project.fragments.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.databinding.FragmentHomeBinding;

import java.util.Calendar;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button addEventButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        addEventButton = root.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected date from the CalendarView
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTimeInMillis(binding.calendarView.getDate());

                // Log the selected date for debugging
                Log.d("HomeFragment", "Selected Date: " + selectedDate.getTime());

                // Create a bundle to pass data to the AddEventFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedDate", selectedDate);

                // Navigate to the AddEventFragment with the selected date bundle
                Navigation.findNavController(view).navigate(R.id.navigation_add, bundle);
            }
        });


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

