package com.avenue.taipt.runningappjava.other;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

public class TrackingUtility {

    public static boolean hasLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        } else {
            return EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );
        }
    }

    public static float calculatePolylineLength(Polyline polyline) {
        float distance = 0f;
        for (int i = 0; i < polyline.size() - 2; i++) {
            LatLng pos1 = polyline.get(i);
            LatLng pos2 = polyline.get(i + 1);
            float[] result = new float[1];
            Location.distanceBetween(
                    pos1.latitude,
                    pos1.longitude,
                    pos2.latitude,
                    pos2.longitude,
                    result
            );
            distance += result[0];
        }
        return distance;
    }

    public static String getFormattedStopWatchTime(long ms, boolean includeMillis) {
        long milliseconds = ms;
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        milliseconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        if (!includeMillis) {
            return ((hours < 10) ? "0" : "") + hours + ":" +
                    ((minutes < 10) ? "0" : "") + minutes + ":" +
                    ((seconds < 10) ? "0" : "") + seconds + "";
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds);
        milliseconds /= 10;
        return ((hours < 10) ? "0" : "") + hours + ":" +
                ((minutes < 10) ? "0" : "") + minutes + ":" +
                ((seconds < 10) ? "0" : "") + seconds + ":" +
                ((milliseconds < 10) ? "0" : "") + milliseconds;
    }
}
