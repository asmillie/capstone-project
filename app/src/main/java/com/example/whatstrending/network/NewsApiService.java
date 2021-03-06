package com.example.whatstrending.network;

import com.example.whatstrending.BuildConfig;
import com.example.whatstrending.data.NewsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsApiService {
    @Headers("X-Api-Key: " + BuildConfig.NEWSAPI_API_KEY)
    @GET("top-headlines")
    Call<NewsApiResponse> getTopHeadlines(
            @Query("country") String countryCode,
            @Query("category") String category,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );

    @Headers("X-Api-Key: " + BuildConfig.NEWSAPI_API_KEY)
    @GET("everything")
    Call<NewsApiResponse> searchArticles(
            @Query("q") String query,
            @Query("language") String languageCode,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );
}
