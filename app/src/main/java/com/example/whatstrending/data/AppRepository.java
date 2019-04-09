package com.example.whatstrending.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.whatstrending.AppExecutors;

import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class AppRepository {

    private final static String TAG = AppRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static AppRepository sInstance;

    private final AppDatabase mDatabase;

    private Context mContext;

    private AppRepository(Context context) {
        mDatabase = AppDatabase.getInstance(context);
        mContext = context;
        ///TODO: Remove test call
        getAllArticles();
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
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NewsApiWorker.class).build();
            WorkManager.getInstance().enqueue(workRequest); //TODO: Also setup on a schedule
        }
        return articles;
    }

    public void saveArticles(final List<Article> articles) {
        if (articles != null && !articles.isEmpty()) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.articleDao().saveArticles(articles);
                }
            });
        }
    }

    ////// Private Methods //////

    private boolean isArticleListEmpty(LiveData<List<Article>> articles) {
        List<Article> articleList = articles.getValue();
        return articleList == null || articleList.isEmpty();
    }
}
