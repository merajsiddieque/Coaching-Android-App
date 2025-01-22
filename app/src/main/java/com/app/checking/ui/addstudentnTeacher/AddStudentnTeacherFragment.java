package com.app.checking.ui.addstudentnTeacher;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.checking.databinding.FragmentAddStudentTeacherBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public class AddStudentnTeacherFragment extends Fragment {

    private FragmentAddStudentTeacherBinding binding;
    private FirebaseAuth studentAuth, teacherAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddStudentTeacherBinding.inflate(inflater, container, false);

        // Initialize Firebase for studentApp
        FirebaseApp studentApp = initializeFirebaseApp("studentApp",
                "AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw",
                "1:205322996588:android:734b17b30e0a11352ac149",
                "winnerclassesstudent.firebasestorage.app");
        studentAuth = FirebaseAuth.getInstance(studentApp);

        // Initialize Firebase for teacherApp
        FirebaseApp teacherApp = initializeFirebaseApp("teacherApp",
                "AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw",
                "1:1020091215156:android:5eabcde670c9514565068961",
                "winnerclassesteacher.firebasestorage.app");
        teacherAuth = FirebaseAuth.getInstance(teacherApp);

        // Ensure only one radio button can be selected at a time
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.radioButtonStudent.getId()) {
                binding.radioButtonTeacher.setChecked(false);
            } else if (checkedId == binding.radioButtonTeacher.getId()) {
                binding.radioButtonStudent.setChecked(false);
            }
        });

        // Set "Add" button listener
        binding.addStdAdd.setOnClickListener(v -> {
            String email = binding.addStdEmail.getText().toString();
            String password = binding.addStdPass.getText().toString();
            if (validateInput(email, password)) {
                if (binding.radioButtonStudent.isChecked()) {
                    registerStudent(email, password);
                } else if (binding.radioButtonTeacher.isChecked()) {
                    registerTeacher(email, password);
                } else {
                    Toast.makeText(getActivity(), "Please select a role (Student or Teacher)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set "Reset" button listener
        binding.addStdReset.setOnClickListener(v -> {
            binding.addStdEmail.setText("");
            binding.addStdPass.setText("");
            binding.radioGroup.clearCheck(); // Clear radio button selection
        });

        return binding.getRoot();
    }

    private FirebaseApp initializeFirebaseApp(String appName, String apiKey, String appId, String dbUrl) {
        try {
            return FirebaseApp.getInstance(appName);
        } catch (IllegalStateException e) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey(apiKey)
                    .setApplicationId(appId)
                    .setDatabaseUrl(dbUrl)
                    .build();
            return FirebaseApp.initializeApp(requireContext(), options, appName);
        }
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerStudent(String email, String password) {
        studentAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Student Registered Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Student Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerTeacher(String email, String password) {
        teacherAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Teacher Registered Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Teacher Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
