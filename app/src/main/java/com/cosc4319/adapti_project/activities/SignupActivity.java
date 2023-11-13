package com.cosc4319.adapti_project.activities;

import static com.cosc4319.adapti_project.utililities.ValidationUtils.isEmailValid;
import static com.cosc4319.adapti_project.utililities.ValidationUtils.isPasswordValid;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cosc4319.adapti_project.R;
import com.cosc4319.adapti_project.utililities.FirebaseAuthenticationHelper;
import com.cosc4319.adapti_project.utililities.ValidationUtils;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends BaseActivity {
    private EditText signupName, signupUsername, signupEmail, signupPassword;
    private TextView loginRedirectText, passwordCriteriaText, emailCriteriaText;
    private Button signupButton;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //find by IDs
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
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();

                if (ValidationUtils.isPasswordValid(password) && ValidationUtils.isEmailValid(email)) {
                    // Use Firebase Authentication helper to create a new user
                    FirebaseAuthenticationHelper firebaseAuthHelper = new FirebaseAuthenticationHelper();
                    firebaseAuthHelper.signUpWithFirebase(email, password, task -> {
                        handleFirebaseTaskResult(task, "You have signed up successfully!");
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (user != null) {
                                // Update user profile if needed
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build());
                            }
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            showShortToast("Failed to create a user.");
                        }
                    });
                } else {
                    showShortToast("Invalid email or password.");
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


}
