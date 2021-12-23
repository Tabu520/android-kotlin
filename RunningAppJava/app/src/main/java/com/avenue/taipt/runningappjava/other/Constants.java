package com.avenue.taipt.runningappjava.other;

import android.graphics.Color;

public class Constants {

    public static final String RUNNING_DATABASE_NAME = "running_db";
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 0;
    public static final String ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE";
    public static final String ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT";

    public static final String SHARED_PREFERENCES_NAME = "sharedPref";
    public static final String KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_WEIGHT = "KEY_WEIGHT";

    public static final long TIMER_UPDATE_INTERVAL = 50L;

    public static final long LOCATION_UPDATE_INTERVAL = 5000L;
    public static final long FASTEST_LOCATION_INTERVAL = 2000L;

    public static final int POLYLINE_COLOR = Color.RED;
    public static final float POLYLINE_WIDTH = 8f;
    public static final float MAP_ZOOM = 15f;

    public static final String NOTIFICATION_CHANNEL_ID = "tracking_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Tracking";
    public static final int NOTIFICATION_ID = 1;
}
