package com.app.checking.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.checking.databinding.FragmentHomeStudentBinding;

public class HomeStudentFragment extends Fragment {

private FragmentHomeStudentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeStudentViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeStudentViewModel.class);

    binding = FragmentHomeStudentBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}