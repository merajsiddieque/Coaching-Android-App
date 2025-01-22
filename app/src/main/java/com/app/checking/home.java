package com.app.checking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.checking.ui.home.HomeTAFragment;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HomeTAFragment.newInstance())
                    .commitNow();
        }
    }
}