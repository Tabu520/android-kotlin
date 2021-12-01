package com.avenue.taipt.newsappjava.db;

import androidx.room.TypeConverter;

import com.avenue.taipt.newsappjava.models.Source;

public class Converters {

    @TypeConverter
    public String fromSource(Source source) {
        return source.getName();
    }

    @TypeConverter
    public Source toSource(String name) {
        return new Source(name, name);
    }
}
