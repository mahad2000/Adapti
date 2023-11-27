package com.cosc4319.adapti_project.activities;
import android.os.Bundle;
import android.app.Dialog;
import com.cosc4319.adapti_project.fragments.add_event.AddEventFragment;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.Manifest;
import android.content.pm.PackageManager;
import com.cosc4319.adapti_project.utililities.EventHelper;


import android.speech.tts.TextToSpeech;
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

import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.databinding.ActivityMainBinding;
import com.cosc4319.adapti_project.utililities.Event;
import com.cosc4319.adapti_project.utililities.EventHelper;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView dialogText;
    private TextToSpeech textToSpeech;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;
    private EventHelper eventHelper;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int selectedTheme = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
                .getInt("selectedTheme", R.style.Base_Theme_MyApplication);
        applyTheme(selectedTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        eventHelper = new EventHelper();

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });
        ImageView readEventsIcon = findViewById(R.id.readEventsIcon);
        readEventsIcon.setOnClickListener(v -> readEvents());




        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestRecordAudioPermission();
        }
        ImageView accountIcon = findViewById(R.id.accountIcon);
        accountIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserProfile();
            }
        });

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
                    interpretCommand(spokenText);
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
        // Check if the command starts with "create event"
        if (command.toLowerCase().startsWith("create event")) {
            // Check if the command includes "all day" to set the isAllDay flag
            boolean isAllDay = command.toLowerCase().contains("all day");

            // Call the createEvent method with the command and the isAllDay flag
            createEvent(command, isAllDay);
        }//else if (command.startsWith("edit event")) {
            // editEvent(command);
        //} else if (command.startsWith("discard event")) {
        //    discardEvent(command);
       // }
    }
    private void showUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName(); // Get the user's name
            String email = user.getEmail(); // Get the user's email

            // Declare the AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Profile Information");
            builder.setMessage("Name: " + name + "\nEmail: " + email);

            // Adding a "Logout" button
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logoutUser(); // Call the logoutUser method when the Logout button is clicked
                }
            });

            // Adding an "OK" button to simply close the dialog
            builder.setNegativeButton("OK", null);

            // Add the theme selection option to the dialog
            builder.setNeutralButton("Change Theme", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showThemeSelectionDialog();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Handle the case where no user is logged in
        }
    }


    private void showThemeSelectionDialog() {
        final String[] themes = {"Base Theme", "Deuteranopia Theme", "Protanopia Theme", "Tritanopia Theme"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Theme")
                .setItems(themes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int themeId;
                        switch (which) {
                            case 0:
                                themeId = R.style.Base_Theme_MyApplication;
                                break;
                            case 1:
                                themeId = R.style.Base_Theme_MyApplication_Deuteranopia;
                                break;
                            case 2:
                                themeId = R.style.Base_Theme_MyApplication_Protanopia;
                                break;
                            case 3:
                                themeId = R.style.Base_Theme_MyApplication_Tritanopia;
                                break;
                            default:
                                themeId = R.style.Base_Theme_MyApplication;
                                break;  // Add a break statement for the default case
                        }

                        // Save the selected theme preference
                        getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
                                .edit()
                                .putInt("selectedTheme", themeId)
                                .apply();

                        // Apply the selected theme
                        applyTheme(themeId);
                        recreate(); // Recreate the activity to apply the theme changes
                    }
                });

        builder.create().show();
    }

    private void applyTheme(int themeId) {
        setTheme(themeId);
    }
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        // Redirect to login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void displayUserInfo(String name, String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile Information");
        builder.setMessage("Name: " + name + "\nEmail: " + email);

        // Adding a "Logout" button
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser(); // Call the logoutUser method when the Logout button is clicked
            }
        });

        // Adding an "OK" button to simply close the dialog
        builder.setNegativeButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
    private String parseDate(String dateString) {
        try {
            // Adjust the input format to match the expected format of dateString
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
            Date eventDate = inputFormat.parse(dateString);

            Calendar eventCalendar = Calendar.getInstance();
            eventCalendar.setTime(eventDate);

            // Determine the correct year
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            eventCalendar.set(Calendar.YEAR, currentYear); // Set the year to the current year

            // If the event date has already passed in the current year, add one year
            if (eventCalendar.before(Calendar.getInstance())) {
                eventCalendar.add(Calendar.YEAR, 1);
            }

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            return outputFormat.format(eventCalendar.getTime());
        } catch (Exception e) {
            Log.e("DateParse", "Error parsing date: " + e.getMessage());
            // If parsing fails, return the original string or handle the error appropriately
            return dateString;
        }
    }



    private void createEvent(String command, boolean isAllDay) {
        // Example command: "create event Doctor Appointment on April 5 at 10 AM"

        // Split the command into parts
        String[] parts = command.split(" ");

        // Basic validation to check if the command has the minimum required parts
        if (parts.length < 8) {
            Log.e("VoiceCommand", "Command format not recognized");
            return;
        }

        // Extract event name
        String eventName = parts[2] + " " + parts[3];

        // Extract the date part of the command
        String dateString = parts[5] + " " + parts[6];
        String eventDate = parseDate(dateString); // Use parseDate to get the date in correct format
        String eventTime = isAllDay ? "" : parts[8] + " " + parts[9];

        // Pass the data to AddEventFragment including the isAllDay flag
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
                .replace(R.id.nav_host_fragment_activity_main, fragment)
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
    private void readEvents() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            eventHelper.getEvents(userId, new EventHelper.EventDataListener() {
                @Override
                public void onDataLoaded(List<Event> eventList) {
                    StringBuilder eventDetails = new StringBuilder();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

                    if (!eventList.isEmpty()) {
                        eventDetails.append("Your Upcoming Events:\n");
                    }

                    boolean isFirstEvent = true;
                    for (Event event : eventList) {
                        if (!isFirstEvent) {
                            eventDetails.append("Your Next Event:\n");
                        }
                        isFirstEvent = false;

                        String formattedDate = dateFormat.format(event.getEventDate());
                        eventDetails.append(event.getEventTitle())
                                .append(", Date: ").append(formattedDate);

                        if (event.isAllDay()) {
                            eventDetails.append(", Time: All Day");
                        } else {
                            eventDetails.append(", Time: ").append(event.getEventTime());
                        }
                        eventDetails.append(".\n");
                    }

                    if (!eventDetails.toString().isEmpty()) {
                        textToSpeech.speak(eventDetails.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeech.speak("No upcoming events", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            });
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
