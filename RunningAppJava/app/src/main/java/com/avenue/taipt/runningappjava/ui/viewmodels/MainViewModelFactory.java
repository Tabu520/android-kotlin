package com.avenue.taipt.runningappjava.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.avenue.taipt.runningappjava.repositories.MainRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final MainRepository mainRepository;

    public MainViewModelFactory(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass == MainViewModel.class) {
            return (T) new MainViewModel(mainRepository);
        }
        if (aClass == StatisticsViewModel.class) {
            return (T) new StatisticsViewModel(mainRepository);
        }
        return null;
    }
}
