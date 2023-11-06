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
import android.widget.ImageView;
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

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
// Add the imports for OnSuccessListener and OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentAddEventBinding binding;
    private TextView newEventDate;
    private Switch allDaySwitch;
    private String currentDateString;
    private Button saveEventButton;
    private ImageView imageView;
    private TextView textViewResult;
    private Button buttonRecognize;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UI elements
        newEventDate = binding.newEventDate;
        allDaySwitch = binding.allDaySwitch;
        saveEventButton = binding.saveEventButton;
        imageView = binding.imageView; // Make sure you have an ImageView with id 'image_view' in your layout
        textViewResult = binding.textViewResult; // Make sure you have a TextView with id 'text_view_result' in your layout
        buttonRecognize = binding.buttonTextRecognition; // Make sure you have a Button with id 'button_text_recognition' in your layout

        initializeUIElements();
        setupEventListeners();

        buttonRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recognizeText();
            }
        });

        return root;
    }
    private void recognizeText() {
        // Use BitmapFactory to get the Bitmap from the drawable resource
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.your_sample_image);
        imageView.setImageBitmap(bitmap);

        // Create an InputImage object from the Bitmap
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Get an instance of TextRecognizer
        TextRecognizer recognizer = TextRecognition.getClient();

        // Process the image
        recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text texts) {
                        // Task completed successfully
                        for (Text.TextBlock block : texts.getTextBlocks()) {
                            String blockText = block.getText();
                            textViewResult.append(blockText + "\n");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        textViewResult.setText("Recognition failed: " + e.getMessage());
                    }
                });
    }
}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
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
        Button captureImageButton = binding.captureImageButton;

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here you would start an activity to capture an image or pick from gallery
                // This could be done using an Intent to open the camera or gallery
                // Once the image is captured or selected, process it with OCR
                startImageCapture();
            }
        });
    }
    private void startImageCapture() {
        // Intent to capture an image or open gallery
        // Handle the result in onActivityResult
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
