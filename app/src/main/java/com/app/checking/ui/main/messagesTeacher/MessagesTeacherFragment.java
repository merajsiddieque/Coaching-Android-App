package com.app.checking.ui.main.messagesTeacher;

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
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MessagesTeacherFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private EditText message;
    private Button sendMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages_teacher, container, false);

        // Initialize Firebase for teacher messages
        initializeFirebaseApp();

        // Initialize views
        message = rootView.findViewById(R.id.teachermessagetxtBox);
        sendMessage = rootView.findViewById(R.id.teachermessageSubmit);

        // Set click listener for the send message button
        sendMessage.setOnClickListener(v -> {
            String messageText = message.getText().toString().trim();

            if (TextUtils.isEmpty(messageText)) {
                Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            } else {
                // Get the current date and time
                String currentDateAndTime = getCurrentDateAndTime();
                sendMessageToFirebase("Student Messages", messageText, currentDateAndTime);
                Toast.makeText(getContext(), "Message sent to Student", Toast.LENGTH_SHORT).show();
                message.setText("");
            }
        });

        return rootView;
    }

    private void initializeFirebaseApp() {
        try {
            // Check if FirebaseApp for "adminApp" already exists
            FirebaseApp adminApp = FirebaseApp.getInstance("adminApp");
            firebaseAuth = FirebaseAuth.getInstance(adminApp);
        } catch (IllegalStateException e) {
            // Initialize FirebaseApp if not already initialized
            FirebaseOptions adminOption = new FirebaseOptions.Builder()
                    .setApiKey("AIzaSyCNNDwijxyDV-H2BJ42qMRh7p6NwkCHOow") // API Key from google-services.json
                    .setApplicationId("1:1020091215156:android:e5d5a670c9514565068961") // Application ID from google-services.json
                    .setDatabaseUrl("winner-classes-5e21d.firebasestorage.app") // Example Database URL for Realtime Database
                    .build();

            FirebaseApp adminApp = FirebaseApp.initializeApp(requireContext(), adminOption, "adminApp");
            firebaseAuth = FirebaseAuth.getInstance(adminApp);
        }
    }

    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date()); // Return current date and time in the format "yyyy-MM-dd HH:mm:ss"
    }

    private void sendMessageToFirebase(String recipient, String messageText, String dateTime) {
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", messageText);
        messageMap.put("dateTime", dateTime); // Include date and time along with the message

        FirebaseDatabase.getInstance()
                .getReference()
                .child(recipient)
                .push()
                .setValue(messageMap)
                .addOnSuccessListener(unused -> {
                    // Optional: Add success handling if needed
                })
                .addOnFailureListener(e -> {
                    // Optional: Handle failure
                    Toast.makeText(getContext(), "Failed to send message to " + recipient, Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // No need to clear binding reference since we're using findViewById
    }
}
