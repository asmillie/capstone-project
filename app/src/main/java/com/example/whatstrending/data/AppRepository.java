package com.example.whatstrending.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AppRepository {

    private final static String TAG = AppRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static AppRepository sInstance;

    private final AppDatabase mDatabase;

    private AppRepository(Context context) {
        mDatabase = AppDatabase.getInstance(context);
    }

    //Singleton instantiation of Repository
    public static AppRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppRepository(context);
            }
        }
        return sInstance;
    }

    public LiveData<List<Article>> getAllArticles() {
        LiveData<List<Article>> articles = mDatabase.articleDao().getAllArticles();
        if (isArticleListEmpty(articles)) {
             refreshArticles();
        }
        return articles;
    }
    //TODO: Also setup on a schedule
    public void refreshArticles() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NewsApiWorker.class).build();
        WorkManager.getInstance().enqueue(workRequest);
    }

    public void saveArticles(final List<Article> articles) {
        if (articles != null && !articles.isEmpty()) {
            mDatabase.articleDao().saveArticles(articles);
        }
    }

    public void deleteAllArticles() {
        mDatabase.articleDao().deleteAllArticles();
    }

    ////// Private Methods //////

    private boolean isArticleListEmpty(LiveData<List<Article>> articles) {
        List<Article> articleList = articles.getValue();
        return articleList == null || articleList.size() == 0;
    }
}
