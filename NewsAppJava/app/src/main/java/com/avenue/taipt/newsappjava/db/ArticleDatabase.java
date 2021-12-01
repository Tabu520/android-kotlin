package com.avenue.taipt.newsappjava.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.avenue.taipt.newsappjava.models.Article;

@Database(
        entities = {Article.class},
        version = 1
)
@TypeConverters(Converters.class)
public abstract class ArticleDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();

    private static ArticleDatabase sInstance;

    public static ArticleDatabase createDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (ArticleDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleDatabase.class,
                            "article_db.db")
                            .build();
                }
            }
        }
        return sInstance;
    }
}
