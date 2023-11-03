package com.cosc4319.adapti_project.ui.add_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cosc4319.adapti_project.databinding.FragmentAddEventBinding;

public class AddEventFragment extends Fragment {

    private FragmentAddEventBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddEventViewModel addEventViewModel =
                new ViewModelProvider(this).get(AddEventViewModel.class);

        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.signupButton;
        addEventViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}