package com.avenue.taipt.runningappjava.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.avenue.taipt.runningappjava.repositories.MainRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private MainRepository mainRepository;

    @Inject
    public MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
        Timber.d("mainRepository --- %s", mainRepository.hashCode());
    }

}
