package com.avenue.taipt.runningappjava.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.avenue.taipt.runningappjava.repositories.MainRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StatisticsViewModel extends ViewModel {

    private MainRepository mainRepository;

    @Inject
    public StatisticsViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

}
