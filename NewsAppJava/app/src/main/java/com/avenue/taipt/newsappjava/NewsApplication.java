package com.avenue.taipt.newsappjava;

import android.app.Application;

import com.avenue.taipt.newsappjava.db.ArticleDatabase;
import com.avenue.taipt.newsappjava.repository.NewsRepository;
import com.avenue.taipt.newsappjava.utils.AppExecutors;

public class NewsApplication extends Application {

    private AppExecutors mAppExecutors;
    private static NewsRepository sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = AppExecutors.getInstance();
    }

    public AppExecutors getAppExecutors() {
        return this.mAppExecutors;
    }

    public ArticleDatabase getDatabase() {
        return ArticleDatabase.createDatabase(this);
    }

    public NewsRepository getNewsRepositoryInstance() {
        if (sInstance == null) {
            synchronized (NewsRepository.class) {
                if (sInstance == null) {
                    sInstance = new NewsRepository(getDatabase(), mAppExecutors);
                }
            }
        }
        return sInstance;
    }

    public void clearNewsRepositoryInstance() {
        sInstance = null;
    }
}
