package com.app.checking.ui.ResetPassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.checking.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ResetpassFragment extends Fragment {

    private EditText resetAccountEmail;
    private Button resetAccountButton;

    private FirebaseAuth adminAuth;
    private FirebaseAuth teacherAuth;
    private FirebaseAuth studentAuth;

    // Static method to create a new instance of the fragment
    public static ResetpassFragment newInstance() {
        return new ResetpassFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_pass, container, false);

        // Initialize FirebaseApp for admin, teacher, and student if they are not initialized already
        initializeFirebaseApp("adminApp", "AIzaSyCNNDwijxyDV-H2BJ42qMRh7p6NwkCHOow",
                "1:1020091215156:android:e5d5a670c9514565068961", "winner-classes-5e21d.firebasestorage.app");
        initializeFirebaseApp("teacherApp", "AIzaSyCuedvOWPTept9pSq0lVPWZVR8dzQu8qSs",
                "1:998395215065:android:cc2c885caef11017da92c8", "https://winnerclassesteacher.firebaseio.com");
        initializeFirebaseApp("studentApp", "AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw",
                "1:205322996588:android:734b17b30e0a11352ac149", "winnerclassesstudent.firebasestorage.app");

        // Initialize Firebase Authentication instances
        adminAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("adminApp"));
        teacherAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("teacherApp"));
        studentAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("studentApp"));

        // Bind views
        resetAccountEmail = view.findViewById(R.id.resetAccountEmail); // EditText for email
        resetAccountButton = view.findViewById(R.id.resetAccount); // Button to trigger password reset

        // Set click listener for reset password button
        resetAccountButton.setOnClickListener(v -> {
            String email = resetAccountEmail.getText().toString().trim();

            // Check if the email field is empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
            } else {
                // Send password reset email
                sendPasswordResetEmail(email);
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
        // Try to send password reset email using Admin FirebaseAuth

        adminAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
//                        Toast.makeText(getContext(), "Password reset email sent (Student)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
        teacherAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
//                        Toast.makeText(getContext(), "Password reset email sent (Student)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
        studentAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
