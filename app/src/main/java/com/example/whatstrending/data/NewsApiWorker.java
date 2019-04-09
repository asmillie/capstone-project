package com.example.whatstrending.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.whatstrending.network.NewsApiClient;
import com.example.whatstrending.network.NewsApiService;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;

public class NewsApiWorker extends Worker {

    private static final String TAG = NewsApiWorker.class.getSimpleName();

    private static final int TOTAL_RESULTS_DEFAULT = 0;
    private static final int PAGE_DEFAULT = 1;
    private static final int PAGE_SIZE_DEFAULT = 50;

    private final Context mContext;
    private int totalResults;

    public NewsApiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        this.totalResults = TOTAL_RESULTS_DEFAULT;
    }

    @NonNull
    @Override
    public Result doWork() {

        if (totalResults == TOTAL_RESULTS_DEFAULT) {
            getTopHeadlinesPage(PAGE_DEFAULT);
        }

        if (totalResults > PAGE_SIZE_DEFAULT) {
            //More Headlines available, request pages until all results retrieved
            //Pagination calculation solution from https://stackoverflow.com/questions/17944/how-to-round-up-the-result-of-integer-division
            int totalPages = (totalResults + PAGE_SIZE_DEFAULT - 1) / PAGE_SIZE_DEFAULT;
            int currentPage = 2; //First page already fetched, start on 2nd
            while (currentPage != totalPages) {
                Log.i(TAG, "Requesting page " + currentPage);
                getTopHeadlinesPage(currentPage);
                currentPage++;
            }
        }

        return Result.success();
    }

    private void getTopHeadlinesPage(int page) {

        String countryCode = "us"; //TODO: If time, provide preference to set this

        NewsApiService newsApiService = NewsApiClient.getInstance().getNewsApi();
        Call<NewsApiResponse> call = newsApiService.getTopHeadlines(countryCode, PAGE_SIZE_DEFAULT, page);
        NewsApiResponse response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            Log.e(TAG, "Error contacting News Api: " + e.toString());
        }

        if (response != null && response.getArticles() != null) {
            if (totalResults == TOTAL_RESULTS_DEFAULT) {
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
