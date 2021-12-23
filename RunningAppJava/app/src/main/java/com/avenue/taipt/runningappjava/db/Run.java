package com.avenue.taipt.runningappjava.db;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "running_table")
public class Run {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Bitmap img = null;
    private long timestamp = 0L;
    private float avgSpeedInKmh = 0f;
    private int distanceInMeters = 0;
    private long timeInMillis =  0L;
    private int caloriesBurned = 0;

    public Run(Bitmap img, long timestamp, float avgSpeedInKmh, int distanceInMeters, long timeInMillis, int caloriesBurned) {
        this.img = img;
        this.timestamp = timestamp;
        this.avgSpeedInKmh = avgSpeedInKmh;
        this.distanceInMeters = distanceInMeters;
        this.timeInMillis = timeInMillis;
        this.caloriesBurned = caloriesBurned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getAvgSpeedInKmh() {
        return avgSpeedInKmh;
    }

    public void setAvgSpeedInKmh(float avgSpeedInKmh) {
        this.avgSpeedInKmh = avgSpeedInKmh;
    }

    public int getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(int distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
}
