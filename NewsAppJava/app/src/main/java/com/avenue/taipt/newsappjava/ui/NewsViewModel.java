package com.avenue.taipt.newsappjava.ui;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avenue.taipt.newsappjava.NewsApplication;
import com.avenue.taipt.newsappjava.models.Article;
import com.avenue.taipt.newsappjava.models.NewsResponse;
import com.avenue.taipt.newsappjava.repository.NewsRepository;
import com.avenue.taipt.newsappjava.utils.AppExecutors;
import com.avenue.taipt.newsappjava.utils.Constants;
import com.avenue.taipt.newsappjava.utils.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {

    private final NewsRepository newsRepository;
    public MutableLiveData<Resource<NewsResponse>> breakingNews = new MutableLiveData<>();
    private int breakingNewsPage = 1;
    private NewsResponse breakingNewsResponse;

    public MutableLiveData<Resource<NewsResponse>> searchNews = new MutableLiveData<>();
    private int searchNewsPage = 1;
    private NewsResponse searchNewsResponse;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        this.newsRepository = ((NewsApplication) application).getNewsRepositoryInstance();
    }

    public void loadBreakingNews(String countryCode) {
        Log.d("TaiPT", "NewsViewModel::loadBreakingNews()");
        breakingNews.postValue(new Resource.Loading<>());
        try {
            if (hasInternetConnection()) {
                Call<NewsResponse> response = newsRepository.getBreakingNews(countryCode, breakingNewsPage, Constants.API_KEY);
                response.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.body() != null) {
                            breakingNewsPage++;
                            if (breakingNewsResponse == null) {
                                breakingNewsResponse = response.body();
                            } else {
                                ArrayList<Article> oldArticles = breakingNewsResponse.getArticles();
                                ArrayList<Article> newArticles = response.body().getArticles();
                                if (oldArticles != null) {
                                    oldArticles.addAll(newArticles);
                                }
                            }
                            if (breakingNewsResponse == null) {
                                breakingNews.postValue(new Resource.Success<>(response.body()));
                            } else {
                                breakingNews.postValue(new Resource.Success<>(breakingNewsResponse));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        breakingNews.postValue(new Resource.Error<>(null, t.getMessage()));
                    }
                });
            } else {
                searchNews.postValue(new Resource.Error<>(null, "No internet connection!"));
            }
        } catch (Exception e) {
            searchNews.postValue(new Resource.Error<>(null, "Network Failure"));
        }
    }

    public void searchNews(String searchQuery) {
        Log.d("TaiPT", "NewsViewModel::searchNews()");
        searchNews.postValue(new Resource.Loading<>());
        try {
            if (hasInternetConnection()) {
                Call<NewsResponse> responseCall = newsRepository.searchNews(searchQuery, searchNewsPage, Constants.API_KEY);
                responseCall.enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.body() != null) {
                            searchNewsPage++;
                            searchNews.postValue(new Resource.Success<>(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        searchNews.postValue(new Resource.Error<>(null, t.getMessage()));
                    }
                });
            } else {
                searchNews.postValue(new Resource.Error<>(null, "No internet connection!"));
            }
        } catch (Exception e) {
            searchNews.postValue(new Resource.Error<>(null, "Network Failure"));
        }
    }

    public LiveData<List<Article>> getSavedNews() {
        return newsRepository.getSavedNews();
    }

    public void saveArticle(Article article) {
        newsRepository.upsert(article);
    }

    public void deleteArticle(Article article) {
        newsRepository.deleteArticle(article);
    }

    public int getBreakingNewsPage() {
        return breakingNewsPage;
    }

    public int getSearchNewsPage() {
        return searchNewsPage;
    }

    private boolean hasInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ((NewsApplication) getApplication())
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) return false;
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        if (capabilities == null) return false;
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }
}
