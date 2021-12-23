package com.avenue.taipt.runningappjava.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.avenue.taipt.runningappjava.databinding.FragmentRunBinding;
import com.avenue.taipt.runningappjava.databinding.FragmentTrackingBinding;
import com.avenue.taipt.runningappjava.other.Constants;
import com.avenue.taipt.runningappjava.other.Polyline;
import com.avenue.taipt.runningappjava.services.TrackingService;
import com.avenue.taipt.runningappjava.ui.viewmodels.MainViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrackingFragment extends Fragment {

    private FragmentTrackingBinding binding;
    private MainViewModel mainViewModel;

    private GoogleMap map;
    private boolean isTracking;
    private ArrayList<Polyline> pathPoints = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrackingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(googleMap -> {
            this.map = googleMap;
        });
        binding.btnToggleRun.setOnClickListener(v -> {
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    private void addLatestPolyline() {

    }

    private void sendCommandToService(String action) {
        Intent intent = new Intent(requireContext(), TrackingService.class);
        intent.setAction(action);
        requireContext().startService(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
