package com.example.whatstrending.data;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.whatstrending.Constants;
import com.example.whatstrending.network.NewsApiClient;
import com.example.whatstrending.network.NewsApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class SearchArticlesIntentService extends IntentService {

    private static final String TAG = SearchArticlesIntentService.class.getSimpleName();

    private static final String ACTION_SEARCH_ARTICLES = "com.example.whatstrending.data.action.SEARCH_ARTICLES";

    private static final String EXTRA_SEARCH_QUERY = "com.example.whatstrending.data.extra.SEARCH_QUERY";

    public SearchArticlesIntentService() {
        super("SearchArticlesIntentService");
    }

    public static void startActionSearchArticles(Context context, String query) {
        Intent intent = new Intent(context, SearchArticlesIntentService.class);
        intent.setAction(ACTION_SEARCH_ARTICLES);
        intent.putExtra(EXTRA_SEARCH_QUERY, query);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEARCH_ARTICLES.equals(action)) {
                final String query = intent.getStringExtra(EXTRA_SEARCH_QUERY);
                handleActionSearchArticles(query);
            }
        }
    }

    private void handleActionSearchArticles(String query) {
        //Delete previous search results
        AppRepository.getInstance(SearchArticlesIntentService.this).deleteAllArticlesByCategory(Constants.ARTICLE_CATEGORY_SEARCH_RESULT);
        //Get just first page of results
        getSearchResultPage(query, Constants.PAGE_DEFAULT);
    }

    private void getSearchResultPage(String query, int page) {
        String urlEncodedQuery = TextUtils.htmlEncode(query); //NewsAPI Requires search query to be Url-encoded

        NewsApiService newsApiService = NewsApiClient.getInstance().getNewsApi();
        Call<NewsApiResponse> call = newsApiService.searchArticles(urlEncodedQuery,
                Constants.LANGUAGE_CODE,
                Constants.PAGE_SIZE_DEFAULT,
                page);
        NewsApiResponse response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            Log.e(TAG, "Error contacting News Api: " + e.toString());
        }

        if (response != null && response.getArticles() != null) {
            saveArticles(response.getArticles());
        }
    }

    private void saveArticles(List<Article> articles) {
        if (articles.size() > 0) {
            Log.i(TAG, "Copying sources and setting category on Articles returned by user search");
            for (Article article: articles) {
                String sourceName = article.getSource().getName();
                if (sourceName != null && !sourceName.equals("")) {
                    article.setNewsSource(sourceName);
                }
                //Set Category
                article.setCategory(Constants.ARTICLE_CATEGORY_SEARCH_RESULT);
            }

            AppRepository repository = AppRepository.getInstance(SearchArticlesIntentService.this);
            //Delete previous search results
            Log.i(TAG, "Delete previous search results");
            repository.deleteAllArticlesByCategory(Constants.ARTICLE_CATEGORY_SEARCH_RESULT);

            Log.i(TAG, "Saving articles returned by user search");
            repository.saveArticles(articles);
        }
    }
}
