package com.example.whatstrending.network;

import com.example.whatstrending.BuildConfig;
import com.example.whatstrending.data.NewsApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApiService {
    //https://newsapi.org/v2/top-headlines?country=us&apiKey=aeea4d6f7df04fc5aef5026a9ad2ff92
    @Headers("X-Api-Key: " + BuildConfig.NEWSAPI_API_KEY)
    @GET("top-headlines" )
    Call<NewsApiResponse> getTopHeadlines(@Query("country") String countryCode);
}
