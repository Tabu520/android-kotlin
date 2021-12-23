package com.avenue.taipt.newsappjava.repository;

import androidx.lifecycle.LiveData;

import com.avenue.taipt.newsappjava.api.RetrofitInstance;
import com.avenue.taipt.newsappjava.db.ArticleDatabase;
import com.avenue.taipt.newsappjava.models.Article;
import com.avenue.taipt.newsappjava.models.NewsResponse;
import com.avenue.taipt.newsappjava.utils.AppExecutors;

import java.util.List;

import retrofit2.Call;

public class NewsRepository {

    private final ArticleDatabase db;
    private AppExecutors mAppExecutors;

    public NewsRepository(ArticleDatabase db, AppExecutors appExecutors) {
        this.db = db;
        this.mAppExecutors = appExecutors;
    }

    public Call<NewsResponse> getBreakingNews(String countryCode, int pageNumber, String apiKey) {
        return RetrofitInstance.getNewsApi().getBreakingNews(countryCode, pageNumber, apiKey);
    }

    public Call<NewsResponse> searchNews(String searchQuery, int pageNumber, String apiKey) {
        return RetrofitInstance.getNewsApi().searchForNews(searchQuery, pageNumber, apiKey);
    }

    public void upsert(Article article) {
        Runnable upsertRunnable = () -> db.articleDao().upsert(article);
        mAppExecutors.diskIO().execute(upsertRunnable);
    }

    public LiveData<List<Article>> getSavedNews() {
        return db.articleDao().getAllArticles();
    }

    public void deleteArticle(Article article) {
        Runnable deleteArticleRunnable = () -> db.articleDao().deleteArticle(article);
        mAppExecutors.diskIO().execute(deleteArticleRunnable);
    }
}
