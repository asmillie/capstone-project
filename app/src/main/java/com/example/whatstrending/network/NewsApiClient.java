package com.example.whatstrending.network;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
Singleton Instantiation of Retrofit Client following example
@ https://www.simplifiedcoding.net/android-paging-library-tutorial/#Creating-API
 */
public class NewsApiClient {

    private static final String NEWS_API_BASE_URL = "https://newsapi.org/v2/";

    private static NewsApiClient mInstance;
    private final Retrofit mRetrofit;

    private NewsApiClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(NEWS_API_BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build();
    }

    public static synchronized NewsApiClient getInstance() {
        if (mInstance == null) {
            mInstance = new NewsApiClient();
        }
        return mInstance;
    }

    public NewsApiService getNewsApi() {
        return mRetrofit.create(NewsApiService.class);
    }
}
