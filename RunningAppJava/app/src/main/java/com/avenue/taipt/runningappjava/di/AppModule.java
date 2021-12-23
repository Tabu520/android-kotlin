package com.avenue.taipt.runningappjava.di;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.avenue.taipt.runningappjava.db.RunDao;
import com.avenue.taipt.runningappjava.db.RunningDatabase;
import com.avenue.taipt.runningappjava.executors.AppExecutors;
import com.avenue.taipt.runningappjava.other.Constants;
import com.avenue.taipt.runningappjava.repositories.MainRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public RunningDatabase provideRunningDatabase(@ApplicationContext Context app) {
        return Room.databaseBuilder(app, RunningDatabase.class, Constants.RUNNING_DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    public RunDao provideRunDao(RunningDatabase db) {
        return db.runDao();
    }

    @Provides
    @Singleton
    public AppExecutors provideAppExecutors() {
        return AppExecutors.getInstance();
    }
}
