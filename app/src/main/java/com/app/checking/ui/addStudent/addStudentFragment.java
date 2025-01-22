package com.app.checking.ui.addStudent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.checking.databinding.FragmentAddStudentBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public class addStudentFragment extends Fragment {

    private FragmentAddStudentBinding binding;
    private FirebaseAuth studentAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false);

        // Initialize or reuse FirebaseApp
        initializeFirebaseApp();

        // Set button listeners
        binding.addStdAdd.setOnClickListener(v -> {
            String email = binding.addStdEmail.getText().toString().trim();
            String password = binding.addStdPass.getText().toString().trim();
            if (validateInput(email, password)) {
                registerStudent(email, password); // Correct method name
            }
        });

        binding.addStdReset.setOnClickListener(v -> resetFields());

        return binding.getRoot();
    }

    private void initializeFirebaseApp() {
        try {
            // Check if the "studentApp" FirebaseApp already exists
            FirebaseApp studentApp = FirebaseApp.getInstance("studentApp");
            studentAuth = FirebaseAuth.getInstance(studentApp);
        } catch (IllegalStateException e) {
            // If not initialized, initialize it
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw") // API Key from google-services.json
                    .setApplicationId("1:205322996588:android:734b17b30e0a11352ac149") // Application ID from google-services.json
                    .setDatabaseUrl("https://winnerclassesstudent.firebaseio.com/") // Corrected Database URL format
                    .build();

            FirebaseApp studentApp = FirebaseApp.initializeApp(requireContext(), options, "studentApp");
            studentAuth = FirebaseAuth.getInstance(studentApp);
        }
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerStudent(String email, String password) {
        studentAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                        resetFields(); // Clear fields after successful registration
                    } else {
                        Toast.makeText(requireContext(), "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetFields() {
        binding.addStdEmail.setText("");
        binding.addStdPass.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear the binding to avoid memory leaks
    }
}
