package com.cosc4319.adapti_project.utililities;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthenticationHelper {
    private FirebaseAuth firebaseAuth;

    public FirebaseAuthenticationHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUpWithFirebase(String email, String password,
                                   OnCompleteListener<AuthResult> onCompleteListener) {
        // Use Firebase Authentication to create a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }
}