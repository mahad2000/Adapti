package com.cosc4319.adapti_project;
import android.os.Bundle;
import android.app.Dialog;
import com.cosc4319.adapti_project.ui.add_event.AddEventFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cosc4319.adapti_project.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView dialogText;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestRecordAudioPermission();
        }

        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.microphoneIcon); // Ensure this ID matches in your layout
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null && !data.isEmpty()) {
                    String spokenText = data.get(0);
                    Log.d("VoiceCommand", "Recognized Text: " + spokenText);
                    showPopupWithText(spokenText); // Show the popup with the recognized text
                }
            }



            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                }
                return true;  // Change this to true
            }
        });



        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }
    private void interpretCommand(String command) {
        if (command.toLowerCase().startsWith("create event")) {
            createEvent(command);
        }
        // ... other commands
    }
    private void showPopupWithText(String text) {
        // Inflate the popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Create a PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Set the text to the TextView
        TextView textView = popupView.findViewById(R.id.popup_text_view);
        textView.setText(text);

        // Show the popup window
        popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
    }

    private void createEvent(String command) {
        // Example command: "create event Doctor Appointment on April 5 at 10 AM"

        // Split the command into parts
        String[] parts = command.split(" ");

        // Basic validation to check if the command has the minimum required parts
        if (parts.length < 8) {
            // Handle error - command format is not as expected
            Log.e("VoiceCommand", "Command format not recognized");
            return;
        }

        // Extract event name - this is a simplistic approach, consider refining it
        // Assuming the event name is always one word for now
        String eventName = parts[2];

        // Extract date and time assuming they are always at fixed positions
        // This should be improved for flexibility and error checking
        String eventDate = parts[4] + " " + parts[5];
        String eventTime = parts[7] + " " + parts[8];
        boolean isAllDay = false; // Additional logic needed for determining this

        // After parsing, pass the data to AddEventFragment
        passDataToAddEventFragment(eventName, eventDate, eventTime, isAllDay);
    }

    private void passDataToAddEventFragment(String eventName, String eventDate, String eventTime, boolean isAllDay) {
        AddEventFragment fragment = new AddEventFragment();

        Bundle bundle = new Bundle();
        bundle.putString("eventName", eventName);
        bundle.putString("eventDate", eventDate);
        bundle.putString("eventTime", eventTime);
        bundle.putBoolean("isAllDay", isAllDay);

        fragment.setArguments(bundle);

        // Perform the fragment transaction to display AddEventFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment) // Replace 'fragment_container' with your container ID
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
