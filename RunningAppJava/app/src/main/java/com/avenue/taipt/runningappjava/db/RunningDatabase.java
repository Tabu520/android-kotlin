package com.avenue.taipt.runningappjava.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {Run.class},
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class RunningDatabase extends RoomDatabase {

    public abstract RunDao runDao();
}
