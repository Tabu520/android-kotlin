package com.avenue.taipt.newsappjava.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.avenue.taipt.newsappjava.api.RetrofitInstance;
import com.avenue.taipt.newsappjava.db.ArticleDatabase;
import com.avenue.taipt.newsappjava.models.Article;
import com.avenue.taipt.newsappjava.models.NewsResponse;

import java.util.List;

import retrofit2.Response;

public class NewsRepository {

    private final ArticleDatabase db;

    public NewsRepository(ArticleDatabase db) {
        this.db = db;
    }

    public Response<NewsResponse> getBreakingNews(String countryCode, int pageNumber, String apiKey) {
        return RetrofitInstance.getNewsApi().getBreakingNews(countryCode, pageNumber, apiKey);
    }

    public Response<NewsResponse> searchNews(String countryCode, int pageNumber, String apiKey) {
        return RetrofitInstance.getNewsApi().searchForNews(countryCode, pageNumber, apiKey);
    }

    public long upsert(Article article) {
        return db.articleDao().upsert(article);
    }

    public LiveData<List<Article>> getSavedNews() {
        return db.articleDao().getAllArticles();
    }

    public void deleteArticle(Article article) {
        db.articleDao().deleteArticle(article);
    }
}
