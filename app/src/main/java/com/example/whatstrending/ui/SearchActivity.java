package com.example.whatstrending.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;
import com.example.whatstrending.data.NewsApiResponse;
import com.example.whatstrending.network.NewsApiClient;
import com.example.whatstrending.network.NewsApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private List<Article> mArticleList;
    private int totalResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        totalResults = Constants.TOTAL_RESULTS_DEFAULT;

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO: Loading spinner
            //TODO: Run Search in AsyncTask
            //TODO: Hide Spinner, Display Results in RecyclerView
        }
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(String... query) {
            String urlEncodedQuery = TextUtils.htmlEncode(query[0]); //NewsAPI Requires search query to be Url-encoded

            NewsApiService newsApiService = NewsApiClient.getInstance().getNewsApi();
            Call<NewsApiResponse> call = newsApiService.searchArticles(urlEncodedQuery,
                    Constants.LANGUAGE_CODE,
                    Constants.PAGE_SIZE_DEFAULT,
                    Constants.PAGE_DEFAULT);

            NewsApiResponse response = null;
            try {
                response = call.execute().body();
            } catch (IOException e) {
                Log.e(TAG, "Error contacting News Api: " + e.toString());
            }

            if (response != null && response.getArticles() != null) {
                if (totalResults == Constants.TOTAL_RESULTS_DEFAULT) {
                    totalResults = response.getTotalResults();
                }

                return response.getArticles();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            mArticleList = articles;

        }
    }
}
