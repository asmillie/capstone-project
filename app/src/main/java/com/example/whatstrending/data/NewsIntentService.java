package com.example.whatstrending.data;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.whatstrending.network.NewsApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public NewsIntentService() {
        super("NewsIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetTopHeadlines(Context context, String countryCode) {
        Intent intent = new Intent(context, NewsIntentService.class);
        intent.setAction(ACTION_GET_TOP_HEADLINES);
        intent.putExtra(EXTRA_COUNTRY_CODE, countryCode);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TOP_HEADLINES.equals(action)) {
                final String countryCode = intent.getStringExtra(EXTRA_COUNTRY_CODE);
                handleActionGetTopHeadlines(countryCode);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetTopHeadlines(String countryCode) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NEWS_API_BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build();

        NewsApiService service = retrofit.create(NewsApiService.class);
        //TODO: Move country code to Constants & add Utils method to verify codes
        service.getTopHeadlines(countryCode).enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                Log.i(TAG, "Success contacting API");
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                Log.e(TAG, "Error contacting API: " + t.toString());
            }
        });
    }
}
