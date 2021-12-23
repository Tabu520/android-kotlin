package com.taipt.newsapplicationjava.api;

import com.taipt.newsapplicationjava.models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApi {

    @GET
    Call<NewsResponse> getAllNews(@Url String url);

    @GET
    Call<NewsResponse> getNewsByCategory(@Url String url);
}
