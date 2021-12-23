package com.avenue.taipt.runningappjava.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avenue.taipt.runningappjava.databinding.FragmentRunBinding;
import com.avenue.taipt.runningappjava.databinding.FragmentStatisticsBinding;
import com.avenue.taipt.runningappjava.ui.MainActivity;
import com.avenue.taipt.runningappjava.ui.viewmodels.StatisticsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private StatisticsViewModel statisticsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statisticsViewModel = ((MainActivity) requireActivity()).getStatisticsViewModel();
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
