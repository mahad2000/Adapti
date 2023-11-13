package com.cosc4319.adapti_project.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.databinding.FragmentHomeBinding;
import com.cosc4319.adapti_project.utililities.Event;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Create an instance of the Event class and set the date
        Event event = new Event("Meeting", "12/25/2023", "10:00 AM", true);

        // Get the day and month
        String day = Objects.requireNonNull(event.getDay()); // Null check here
        String month = Objects.requireNonNull(event.getMonth()); // Null check here

        // Set the day and month to the appropriate TextView
        TextView dayTextView = root.findViewById(R.id.eventDay);
        TextView monthTextView = root.findViewById(R.id.eventMonth);

        dayTextView.setText(day);
        monthTextView.setText(month);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
