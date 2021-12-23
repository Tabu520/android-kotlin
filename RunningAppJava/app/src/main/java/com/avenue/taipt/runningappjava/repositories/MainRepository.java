package com.avenue.taipt.runningappjava.repositories;

import com.avenue.taipt.runningappjava.db.Run;
import com.avenue.taipt.runningappjava.db.RunDao;
import com.avenue.taipt.runningappjava.executors.AppExecutors;

import javax.inject.Inject;

import timber.log.Timber;

public class MainRepository {

    private final RunDao runDao;

    @Inject
    public MainRepository(RunDao runDao) {
        this.runDao = runDao;
    }

    public void insertRun(Run run) {
        runDao.insertRun(run);
    }

    public void deleteRun(Run run) {
        runDao.deleteRun(run);
    }

    public void getAllRunsSortedByDate() {
        runDao.getAllRunsSortedByDate();
    }

    public void getAllRunsSortedByDistance() {
        runDao.getAllRunsSortedByDistance();
    }

    public void getAllRunsSortedByAvgSpeed() {
        runDao.getAllRunsSortedByAvgSpeed();
    }

    public void getAllRunsSortedByCaloriesBurned() {
        runDao.getAllRunsSortedByCaloriesBurned();
    }

    public void getAllRunsSortedByTimeInMillis() {
        runDao.getAllRunsSortedByTimeInMillis();
    }

    public void getTotalAvgSpeed() {
        runDao.getAvgSpeed();
    }

    public void getTotalCaloriesBurned() {
        runDao.getTotalCaloriesBurned();
    }

    public void getTotalDistance() {
        runDao.getTotalDistance();
    }

    public void getTotalTimeInMillis() {
        runDao.getTotalTimeInMillis();
    }

}
