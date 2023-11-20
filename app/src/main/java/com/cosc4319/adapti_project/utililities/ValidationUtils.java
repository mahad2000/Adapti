package com.cosc4319.adapti_project.utililities;

import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9!@#\\$%^&*]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    public static boolean isEmailValid(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.(com|org|edu|net|gov|mil|co\\.uk|io|info|etc)$";
        return Pattern.matches(emailPattern, email);
    }
}