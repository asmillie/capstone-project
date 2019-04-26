package com.example.whatstrending.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.whatstrending.Constants;
import com.example.whatstrending.network.NewsApiClient;
import com.example.whatstrending.network.NewsApiService;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;

public class NewsApiWorker extends Worker {

    private static final String TAG = NewsApiWorker.class.getSimpleName();

    private final Context mContext;
    private int totalResults;

    public NewsApiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        this.totalResults = Constants.TOTAL_RESULTS_DEFAULT;
    }

    @NonNull
    @Override
    public Result doWork() {

        int nextPage = Constants.PAGE_DEFAULT; //First page already fetched, start on 2nd
        if (totalResults == Constants.TOTAL_RESULTS_DEFAULT) {
            //Initial Article Refresh
            getTopHeadlinesPage(Constants.PAGE_DEFAULT);
            nextPage++;
        }

        if (totalResults > Constants.PAGE_SIZE_DEFAULT) {
            //More Headlines available, request pages until all results retrieved (Capped for testing to MAX_PAGES)
            //Pagination calculation solution from https://stackoverflow.com/questions/17944/how-to-round-up-the-result-of-integer-division
            int totalPages = (totalResults + Constants.PAGE_SIZE_DEFAULT - 1) / Constants.PAGE_SIZE_DEFAULT;
            if (totalPages > Constants.MAX_PAGES) {
                totalPages = Constants.MAX_PAGES;
            }

            while (nextPage <= totalPages) {
                Log.i(TAG, "Requesting page " + nextPage);
                getTopHeadlinesPage(nextPage);
                nextPage++;
            }
        }

        return Result.success();
    }

    private void getTopHeadlinesPage(int page) {

        String countryCode = "us"; //TODO: If time, provide preference to set this

        NewsApiService newsApiService = NewsApiClient.getInstance().getNewsApi();
        Call<NewsApiResponse> call = newsApiService.getTopHeadlines(countryCode, Constants.DEFAULT_CATEGORY, Constants.PAGE_SIZE_DEFAULT, page);
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

            saveArticles(response.getArticles());
        }
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
            AppRepository.getInstance(mContext).saveArticles(articles);
        }
    }
}
