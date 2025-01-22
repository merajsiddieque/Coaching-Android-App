package com.app.checking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.checking.databinding.ActivityStdBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StdActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityStdBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ViewBinding
        binding = ActivityStdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Set up the toolbar
        setSupportActionBar(binding.appBarStd.toolbar);

        // Initialize the navigation drawer and view
        DrawerLayout drawer = binding.stdDrawerLayout;
        NavigationView navigationView = binding.navView;

        // Configure AppBar for top-level destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.Stdhome, R.id.StdMessages, R.id.StdResetPass, R.id.StdLogout)
                .setOpenableLayout(drawer)
                .build();

        // Set up navigation controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_std);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.StdLogout) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                firebaseAuth.signOut();
                Intent intent = new Intent(StdActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the activity to prevent going back to it
                return true; // Prevent further processing for this item
            } else if (id == R.id.StdResetPass) {
                initializeFirebaseApp("studentApp", "AIzaSyDsbg4PwwatBlMLWU86OKO9GrM6oxK6fPw",
                        "1:205322996588:android:734b17b30e0a11352ac149",
                        "https://winnerclassesstudent.firebaseio.com/");
                sendPasswordResetEmail();
                drawer.closeDrawer(GravityCompat.START);
                return true; // Prevent further processing for this item
            } else {
                // Navigate to the selected fragment
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

                // Close the drawer only if navigation was successful
                if (handled) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                return handled;
            }
        });

        // Set user email in the navigation header
        String userEmail = getIntent().getStringExtra("userEmail");
        if (userEmail != null) {
            setCurrentUserEmail(userEmail);
        }
    }

    private void initializeFirebaseApp(String appName, String apiKey, String applicationId, String databaseUrl) {
        try {
            // Check if the FirebaseApp instance with the given name already exists
            FirebaseApp firebaseApp = FirebaseApp.getInstance(appName);
            firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        } catch (IllegalStateException e) {
            // If not initialized, initialize it
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApiKey(apiKey)
                    .setApplicationId(applicationId)
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(this, options, appName);
            firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        }
    }

    private void sendPasswordResetEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(StdActivity.this, "Password reset link sent to " + email, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(StdActivity.this, "Failed to send password reset email.", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(StdActivity.this, "User email not available.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(StdActivity.this, "No authenticated user found.", Toast.LENGTH_LONG).show();
        }
    }

    private void setCurrentUserEmail(String email) {
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0); // Get the header view
        TextView studentEmailTextView = headerView.findViewById(R.id.StdCurrentEmail);
        studentEmailTextView.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.std, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_std);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
