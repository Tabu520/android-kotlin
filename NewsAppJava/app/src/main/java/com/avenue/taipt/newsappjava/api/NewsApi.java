package com.avenue.taipt.newsappjava.api;

import com.avenue.taipt.newsappjava.models.NewsResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    @GET(value = "v2/top-headlines")
    Response<NewsResponse> getBreakingNews(
            @Query("country") String countryCode,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );

    @GET(value = "v2/everything")
    Response<NewsResponse> searchForNews(
            @Query("q") String searchQuery,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );
}
