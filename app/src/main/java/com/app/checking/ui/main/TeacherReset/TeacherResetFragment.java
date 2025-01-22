package com.app.checking.ui.main.TeacherReset;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.checking.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherResetFragment extends Fragment {

    private EditText studentResetEmail;
    private Button studentResetButton;
    private CheckBox teacherSelfReset; // The checkbox for teacher self-reset

    private FirebaseAuth studentAuth;
    private FirebaseAuth teacherAuth;

    // Static method to create a new instance of the fragment
    public static TeacherResetFragment newInstance() {
        return new TeacherResetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_reset, container, false);

        // Initialize FirebaseApp for student if it's not initialized already
        initializeFirebaseApp("studentApp", "AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw",
                "1:205322996588:android:734b17b30e0a11352ac149", "winnerclassesstudent.firebasestorage.app");

        // Initialize FirebaseApp for teacher if it's not initialized already
        initializeFirebaseApp("teacherApp", "AIzaSyCuedvOWPTept9pSq0lVPWZVR8dzQu8qSs",
                "1:998395215065:android:cc2c885caef11017da92c8", "https://winnerclassesteacher.firebaseio.com");

        // Initialize Firebase Authentication instances
        studentAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("studentApp"));
        teacherAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("teacherApp"));

        // Bind views
        studentResetEmail = view.findViewById(R.id.teacherResetEmail); // EditText for email
        studentResetButton = view.findViewById(R.id.teacherResetButton); // Button to trigger password reset
        teacherSelfReset = view.findViewById(R.id.teacherSelfReset); // Checkbox for teacher self-reset

        // Set click listener for reset password button
        studentResetButton.setOnClickListener(v -> {
            // If the checkbox is checked, reset the current teacher's password immediately
            if (teacherSelfReset.isChecked()) {
                resetCurrentUserPassword();  // Reset the current user's password (Teacher)
            } else {
                // If the checkbox is not checked, proceed with the student's email reset logic
                String email = studentResetEmail.getText().toString().trim();

                // Check if the email field is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
                } else {
                    // Send password reset email for the student
                    sendPasswordResetEmail(email);
                }
            }
        });

        return view;
    }

    private void initializeFirebaseApp(String appName, String apiKey, String applicationId, String databaseUrl) {
        FirebaseApp app;
        try {
            // Check if FirebaseApp is already initialized
            app = FirebaseApp.getInstance(appName);
        } catch (IllegalStateException e) {
            // If not initialized, initialize FirebaseApp
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey(apiKey)
                    .setApplicationId(applicationId)
                    .setDatabaseUrl(databaseUrl)
                    .build();

            app = FirebaseApp.initializeApp(requireContext(), options, appName);
        }
    }

    private void sendPasswordResetEmail(String email) {
        // Try to send password reset email using Student FirebaseAuth
        studentAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        Toast.makeText(getContext(), "Password reset link sent to email (Student)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetCurrentUserPassword() {
        // Get the current logged-in user's email
        String currentUserEmail = teacherAuth.getCurrentUser().getEmail();

        if (currentUserEmail != null) {
            teacherAuth.sendPasswordResetEmail(currentUserEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password reset link sent to email (Self)", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No logged-in teacher found", Toast.LENGTH_SHORT).show();
        }
    }
}
