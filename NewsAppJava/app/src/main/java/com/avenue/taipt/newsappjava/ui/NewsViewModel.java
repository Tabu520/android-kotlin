package com.avenue.taipt.newsappjava.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.avenue.taipt.newsappjava.repository.NewsRepository;

public class NewsViewModel extends AndroidViewModel {

    private NewsRepository newsRepository;

    public NewsViewModel(@NonNull Application application, NewsRepository newsRepository) {
        super(application);
        this.newsRepository = newsRepository;
    }


}
