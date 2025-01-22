package com.app.checking.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.checking.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the binding and initialize the ViewModel
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Set up the TextView to observe LiveData
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize and set up the button for video upload
        Button btnUploadVideo = binding.btnUploadVideo;
        btnUploadVideo.setOnClickListener(v -> uploadVideo());

        // Check and request permissions
        checkPermissions();

        return root;
    }

    private void checkPermissions() {
        // Check and request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE_PERMISSIONS);
            }
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(getContext(), "Permissions granted.", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Permissions denied. App may not work correctly.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadVideo() {
        // Placeholder method for video upload functionality
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(getContext(), "Please grant storage permission to upload a video.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Please grant storage permission to upload a video.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Placeholder action (replace with actual video upload logic)
        Toast.makeText(getContext(), "Video upload functionality to YouTube will be implemented here.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Set binding to null to avoid memory leaks
        binding = null;
    }
}
