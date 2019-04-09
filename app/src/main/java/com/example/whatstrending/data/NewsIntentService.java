package com.example.whatstrending.data;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.whatstrending.network.NewsApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsIntentService extends IntentService {

    private static final String TAG = NewsIntentService.class.getSimpleName();
    private static final String NEWS_API_BASE_URL = "https://newsapi.org/v2/";

    private static final String ACTION_GET_TOP_HEADLINES = "com.example.whatstrending.data.action.GET_TOP_HEADLINES";

    private static final String EXTRA_COUNTRY_CODE = "com.example.whatstrending.data.extra.COUNTRY_CODE";
    private static final String EXTRA_PAGE_SIZE = "com.example.whatstrending.data.extra.PAGE_SIZE";

    public NewsIntentService() {
        super("NewsIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetTopHeadlines(Context context, String countryCode, int pageSize) {
        Intent intent = new Intent(context, NewsIntentService.class);
        intent.setAction(ACTION_GET_TOP_HEADLINES);
        intent.putExtra(EXTRA_COUNTRY_CODE, countryCode);
        intent.putExtra(EXTRA_PAGE_SIZE, pageSize);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TOP_HEADLINES.equals(action)) {
                final String countryCode = intent.getStringExtra(EXTRA_COUNTRY_CODE);
                final int pageSize = intent.getIntExtra(EXTRA_PAGE_SIZE, 20);
                handleActionGetTopHeadlines(countryCode, pageSize);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetTopHeadlines(String countryCode, int pageSize) {

       int page = 0; //API provides paging if totalResults are greater than the requested pageSize

        //Maximum page size is 100
        if (pageSize > 100) {
            pageSize = 100;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NEWS_API_BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build();

        NewsApiService service = retrofit.create(NewsApiService.class);
        //TODO: Move country code to Constants & add Utils method to verify codes
        service.getTopHeadlines(countryCode, pageSize, page).enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                Log.i(TAG, "Success contacting API");
                if (response.body() != null && response.body().getArticles() != null) {
                    saveArticles(response.body().getArticles());
                }
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                Log.e(TAG, "Error contacting API: " + t.toString());
            }
        });
    }

    private void saveArticles(List<Article> articles) {
        if (articles.size() > 0) {
            Log.i(TAG, "Article List contains items, copying sources");
            for (Article article: articles) {
                String sourceName = article.getSource().getName();
                if (sourceName != null && !sourceName.equals("")) {
                    article.setNewsSource(sourceName);
                }
            }
            Log.i(TAG, "Saving articles");
            AppRepository.getInstance(NewsIntentService.this).saveArticles(articles);
        }
    }
}
