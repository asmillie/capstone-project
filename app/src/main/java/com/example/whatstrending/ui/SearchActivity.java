package com.example.whatstrending.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;
import com.example.whatstrending.data.NewsApiResponse;
import com.example.whatstrending.network.NewsApiClient;
import com.example.whatstrending.network.NewsApiService;
import com.example.whatstrending.utils.AnalyticsUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements ArticleListAdapter.ArticleClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    private List<Article> mArticleList;
    private ArticleListAdapter mArticleListAdapter;
    private int totalResults;

    @BindView(R.id.article_search_results)
    RecyclerView mArticleSearchResults;

    @BindView(R.id.loading_bar)
    ProgressBar mLoadingBar;

    @BindBool(R.bool.is_large_screen_device)
    boolean mIsLargeScreenDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        totalResults = Constants.TOTAL_RESULTS_DEFAULT;

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO: Loading spinner
            //TODO: Run Search in AsyncTask
            //TODO: Hide Spinner, Display Results in RecyclerView
        }
    }

    @Override
    public void onArticleClick(int articleId) {
        AnalyticsUtils.logArticleSelect(mFirebaseAnalytics, articleId);

        Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        startActivity(intent);
    }

    private void initViews() {
        mArticleListAdapter = new ArticleListAdapter(null, this);

        RecyclerView.LayoutManager layoutManager;
        if (!mIsLargeScreenDevice) {
            layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        }

        mArticleSearchResults.setLayoutManager(layoutManager);
        mArticleSearchResults.setAdapter(mArticleListAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mArticleSearchResults.addItemDecoration(itemDecoration);

        mArticleListAdapter.setArticleList(mArticleList);
    }

    private void showLoading() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mArticleSearchResults.setVisibility(View.GONE);
    }

    private void showSearchResults() {
        mLoadingBar.setVisibility(View.GONE);
        mArticleSearchResults.setVisibility(View.VISIBLE);
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<Article>> {

        @Override
        protected void onPreExecute() {
            showLoading();
        }

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
            showSearchResults();
            //TODO: Notify adapter
        }
    }
}
