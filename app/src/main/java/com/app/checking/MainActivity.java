package com.app.checking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button reset;
    private FirebaseAuth adminAuth;
    private FirebaseAuth teacherAuth;
    private FirebaseAuth studentAuth;
    private Intent userIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseApp instances for admin, teacher, and student
        initializeFirebaseApps();

        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth instances
        adminAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("adminApp"));
        teacherAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("teacherApp"));
        studentAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("studentApp"));

        // Initialize views
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPass);
        login = findViewById(R.id.loginLogin);
        reset = findViewById(R.id.loginReset);

        // Login button click listener
        login.setOnClickListener(view -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                Toast.makeText(MainActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
            } else {
                signInUser(emailText, passwordText);
            }
        });

        // Reset button click listener
        reset.setOnClickListener(view -> {
            email.setText("");
            password.setText("");
        });
    }

    private void initializeFirebaseApps() {
        FirebaseOptions adminOptions = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyCNNDwijxyDV-H2BJ42qMRh7p6NwkCHOow")
                .setApplicationId("1:1020091215156:android:e5d5a670c9514565068961")
                .setDatabaseUrl("https://winner-classes-5e21d.firebasestorage.app")
                .build();
        FirebaseApp.initializeApp(this, adminOptions, "adminApp");

        FirebaseOptions teacherOptions = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyCuedvOWPTept9pSq0lVPWZVR8dzQu8qSs")
                .setApplicationId("1:998395215065:android:cc2c885caef11017da92c8")
                .setDatabaseUrl("https://winnerclassesteacher.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(this, teacherOptions, "teacherApp");

        FirebaseOptions studentOptions = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw")
                .setApplicationId("1:205322996588:android:734b17b30e0a11352ac149")
                .setDatabaseUrl("https://winnerclassesstudent.firebasestorage.app")
                .build();
        FirebaseApp.initializeApp(this, studentOptions, "studentApp");
    }

    private void signInUser(String email, String password) {
        // Try Admin authentication
        adminAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        handleSignInResult(task, adminActivity.class);
                    } else {
                        // Try Teacher authentication if Admin fails
                        teacherAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, teacherTask -> {
                                    if (teacherTask.isSuccessful()) {
                                        handleSignInResult(teacherTask, teacherActivity.class);
                                    } else {
                                        // Try Student authentication if Teacher fails
                                        studentAuth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(this, studentTask -> {
                                                    if (studentTask.isSuccessful()) {
                                                        handleSignInResult(studentTask, StdActivity.class);
                                                    } else {
                                                        // Show error message if all attempts fail
                                                        Toast.makeText(MainActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    private void handleSignInResult(Task<AuthResult> task, Class<?> activityClass) {
        if (task.isSuccessful() && task.getResult() != null) {
            // Successfully authenticated
            userIntent = new Intent(MainActivity.this, activityClass);
            String userEmail = task.getResult().getUser().getEmail();
            if (userEmail != null) {
                userIntent.putExtra("userEmail", userEmail); // Pass the user email to the next activity
            }
            startActivity(userIntent);
            finish();
        } else {
            // Authentication failed
            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred";
            Toast.makeText(MainActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
