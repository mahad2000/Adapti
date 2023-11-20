package com.cosc4319.adapti_project.activities;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class BaseActivity extends AppCompatActivity {
    protected void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected <T> void handleFirebaseTaskResult(Task<T> task, String successMessage) {
        task.addOnCompleteListener(new OnCompleteListener<T>() {
            @Override
            public void onComplete(@NonNull Task<T> task) {
                if (task.isSuccessful()) {
                    showShortToast(successMessage);
                } else {
                    showLongToast("Error: " + task.getException().getMessage());
                }
            }
        });
    }
}