package com.avenue.taipt.newsappjava.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.avenue.taipt.newsappjava.repository.NewsRepository;

public class NewsViewModelProviderFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;

    public NewsViewModelProviderFactory(@NonNull Application application) {
        this.application = application;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass == NewsViewModel.class) {
            return (T) new NewsViewModel(application);
        }
        return null;
    }
}
