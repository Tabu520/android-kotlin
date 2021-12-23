package com.avenue.taipt.runningappjava.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.avenue.taipt.runningappjava.R;
import com.avenue.taipt.runningappjava.databinding.FragmentRunBinding;
import com.avenue.taipt.runningappjava.databinding.FragmentSetupBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SetupFragment extends Fragment {

    private FragmentSetupBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSetupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.tvContinue.setOnClickListener(v -> {
            navController.navigate(R.id.action_setupFragment_to_runFragment);
        });
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
