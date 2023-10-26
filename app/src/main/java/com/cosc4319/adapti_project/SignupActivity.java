package com.cosc4319.adapti_project;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText, passwordCriteriaText, emailCriteriaText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        passwordCriteriaText = findViewById(R.id.passwordCriteria);
        emailCriteriaText = findViewById(R.id.emailCriteria);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                if (isPasswordValid(password) && isEmailValid(email)) {
                    // Use Firebase Authentication to create a new user
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    // Update user profile if needed
                                    if (user != null) {
                                        user.updateProfile(new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build());
                                    }

                                    Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignupActivity.this, "Failed to create a user.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button to redirect an existing user
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Get the current password
                String password = charSequence.toString();

                // Check the password criteria and update the criteria message
                if (isPasswordValid(password)) {
                    passwordCriteriaText.setText("");
                } else {
                    passwordCriteriaText.setText(
                            "Password must contain at least one uppercase letter, " +
                            "one lowercase letter, and one digit or special character.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signupEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Get the current email
                String email = charSequence.toString();

                // Check the email criteria and update the criteria message
                if (isEmailValid(email)) {
                    emailCriteriaText.setText("");
                } else { emailCriteriaText.setText("Email is not valid."); }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9!@#\\$%^&*]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean isEmailValid(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.(com|org|edu|net|gov|mil|co\\.uk|io|info|etc)$";
        return Pattern.matches(emailPattern, email);
    }
}
