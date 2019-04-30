package com.example.whatstrending.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.whatstrending.utils.WorkUtils;

import java.util.List;

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

    public LiveData<List<Article>> getAllArticlesByCategory(String category) {
        return mDatabase.articleDao().getAllArticlesByCategory(category);
    }

    public void refreshHeadlines() {
        WorkUtils.oneTimeGetAllTopHeadlines();
    }

    public void saveArticles(final List<Article> articles) {
        if (articles != null && !articles.isEmpty()) {
            mDatabase.articleDao().saveArticles(articles);
        }
    }

    public void deleteAllArticlesByCategory(String category) {
        mDatabase.articleDao().deleteAllArticlesByCategory(category);
    }

    public LiveData<Article> getArticleById(int id) {
        return mDatabase.articleDao().getArticleById(id);
    }

    public LiveData<List<Article>> getAllArticleIdsByCategory(String category) {
        return mDatabase.articleDao().getAllArticleIdsByCategory(category);
    }

    /*
    Used by App Widget to get the top headlines
     */
    public List<Article> getTopHeadlines(int limit) {
        return mDatabase.articleDao().getTopHeadlines(limit);
    }
}
