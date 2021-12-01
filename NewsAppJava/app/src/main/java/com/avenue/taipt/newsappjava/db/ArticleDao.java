package com.avenue.taipt.newsappjava.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.avenue.taipt.newsappjava.models.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(Article article);

    @Query(value = "SELECT * FROM articles")
    LiveData<List<Article>> getAllArticles();

    @Delete
    void deleteArticle(Article article);
}
