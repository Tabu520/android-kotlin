package com.avenue.taipt.runningappjava.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.avenue.taipt.runningappjava.R;
import com.avenue.taipt.runningappjava.other.Constants;
import com.avenue.taipt.runningappjava.other.Polyline;
import com.avenue.taipt.runningappjava.other.Polylines;
import com.avenue.taipt.runningappjava.other.TrackingUtility;
import com.avenue.taipt.runningappjava.ui.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TrackingService extends LifecycleService {

    private boolean isFirstRun = true;

    public MutableLiveData<Boolean> isTracking = new MutableLiveData<>();
    public MutableLiveData<Polylines> pathPoints = new MutableLiveData<>();

    private FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("VisibleForTests")
    @Override
    public void onCreate() {
        super.onCreate();
        postInitialValue();
        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        isTracking.observe(this, this::updateLocationTracking);
    }

    private void postInitialValue() {
        isTracking.postValue(false);
        pathPoints.postValue(new Polylines());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            switch (intent.getAction()) {
                case Constants.ACTION_START_OR_RESUME_SERVICE:
                    Timber.d("Started or resumed service!");
                    if (isFirstRun) {
                        startForegroundService();
                        isFirstRun = false;
                    } else {
                        Timber.d("Resuming Service...");
                    }
                    break;
                case Constants.ACTION_PAUSE_SERVICE:
                    Timber.d("Paused service!");
                    break;
                case Constants.ACTION_STOP_SERVICE:
                    Timber.d("Stopped service!");
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService() {

        addEmptyPolylines();
        isTracking.postValue(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                .setContentTitle("Running App")
                .setContentText("00:00:00")
                .setContentIntent(getMainActivityPendingIntent());

        startForeground(Constants.NOTIFICATION_ID, builder.build());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (isTracking.getValue() != null && isTracking.getValue()) {
                List<Location> locations = locationResult.getLocations();
                for (Location location : locations) {
                    addPathPoint(location);
                    Timber.d("NEW LOCATION: " + location.getLatitude() + " -- " + location.getLongitude());
                }
            }
        }
    };


    @SuppressLint("MissingPermission")
    private void updateLocationTracking(boolean isTracking) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermission(this)) {
                LocationRequest request = LocationRequest.create();
                request.setInterval(Constants.LOCATION_UPDATE_INTERVAL);
                request.setFastestInterval(Constants.FASTEST_LOCATION_INTERVAL);
                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                fusedLocationProviderClient.requestLocationUpdates(
                        request,
                        locationCallback,
                        Looper.getMainLooper()
                );
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private void addPathPoint(Location location) {
        if (location != null) {
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            Polylines polylines = pathPoints.getValue();
            if (polylines != null) {
                if (polylines.size() == 0) {
                    polylines.add(new Polyline());
                    polylines.get(0).add(pos);
                }  else {
                    polylines.get(polylines.size() - 1).add(pos);
                }
                pathPoints.postValue(polylines);
            }
        }
    }

    private void addEmptyPolylines() {
        Polylines polylines = pathPoints.getValue();
        if (polylines != null) {
            polylines.add(new Polyline());
            pathPoints.postValue(polylines);
        } else {
            pathPoints.postValue(new Polylines());
        }
    }

    private PendingIntent getMainActivityPendingIntent() {
        Intent intent = new Intent(Constants.ACTION_SHOW_TRACKING_FRAGMENT, null, this, MainActivity.class);
        return PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        );
        notificationManager.createNotificationChannel(channel);
    }
}
