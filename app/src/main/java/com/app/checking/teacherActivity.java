package com.app.checking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.checking.databinding.ActivityTeacherBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class teacherActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTeacherBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ViewBinding
        binding = ActivityTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar
        setSupportActionBar(binding.appBarTeacher.toolbar);

        // Initialize the navigation drawer and view
        DrawerLayout drawer = binding.teacherDrawerLayout;
        NavigationView navigationView = binding.navView;

        // Configure AppBar for top-level destinations
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.teacherhome, R.id.addStudentTch, R.id.teachermessages, R.id.resetpassteacher, R.id.teacherlogout)
                .setOpenableLayout(drawer)
                .build();

        // Set up navigation controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_teacher);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.teacherlogout) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(teacherActivity.this, MainActivity.class);
                    startActivity(intent);
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
            }
        });

        // Get the email passed from MainActivity and set it to the TextView
        String userEmail = getIntent().getStringExtra("userEmail");
        if (userEmail != null) {
            setCurrentUserEmail(userEmail);
        }
    }

    private void setCurrentUserEmail(String email) {
        // Find the TextView in the NavigationView header
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0); // Get the header view
        TextView teacherEmailTextView = headerView.findViewById(R.id.teacherCurrentEmail);

        // Set the passed email to the TextView
        teacherEmailTextView.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu
        getMenuInflater().inflate(R.menu.teacher, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Navigate up in the app using the navigation controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_teacher);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
