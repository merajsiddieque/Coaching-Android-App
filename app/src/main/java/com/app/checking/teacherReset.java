package com.app.checking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.checking.ui.main.TeacherReset.TeacherResetFragment;

public class teacherReset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_reset);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TeacherResetFragment.newInstance())
                    .commitNow();
        }
    }
}