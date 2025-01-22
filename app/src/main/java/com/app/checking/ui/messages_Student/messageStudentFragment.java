package com.app.checking.ui.messages_Student;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.checking.R;
import com.app.checking.databinding.FragmentMessagesStudentBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class messageStudentFragment extends Fragment {

    private FragmentMessagesStudentBinding binding;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messages;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewModel
        messageStudentViewModel galleryViewModel =
                new ViewModelProvider(this).get(messageStudentViewModel.class);

        binding = FragmentMessagesStudentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase with custom credentials
        initializeFirebaseApp();

        // Initialize RecyclerView and message list
        recyclerView = root.findViewById(R.id.messagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration());

        // Fetch messages from Firebase
        fetchMessagesFromFirebase();

        return root;
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
                    .setDatabaseUrl("https://winner-classes-5e21d.firebaseio.com") // Correct Database URL (it should be Realtime Database URL, not Firestore)
                    .build();

            FirebaseApp adminApp = FirebaseApp.initializeApp(requireContext(), adminOption, "adminApp");
            firebaseAuth = FirebaseAuth.getInstance(adminApp);
        }
    }

    private void fetchMessagesFromFirebase() {
        // Get reference to the "Student Messages" node in Firebase Realtime Database
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("Student Messages");

        // Listen for changes in the "Student Messages" node
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the previous messages
                messages.clear();

                // Loop through all messages in Firebase and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String message = snapshot.child("message").getValue(String.class);
                    String dateTime = snapshot.child("dateTime").getValue(String.class);  // Get the dateTime from Firebase
                    if (!TextUtils.isEmpty(message)) {
                        // Add new message at the top of the list (index 0)
                        messages.add(0, new Message(message, dateTime));
                    }
                }

                // Notify adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error (optional)
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
