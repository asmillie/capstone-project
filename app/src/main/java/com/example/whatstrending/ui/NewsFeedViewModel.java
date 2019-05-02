package com.example.whatstrending.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.whatstrending.Constants;
import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

import java.util.List;

class NewsFeedViewModel extends AndroidViewModel {

    private final AppRepository mRepository;
    private LiveData<List<Article>> mArticles;

    public NewsFeedViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Article>> getAllArticles() {
        if (mArticles == null) {
            mArticles = mRepository.getAllArticlesByCategory(Constants.ARTICLE_CATEGORY_HEADLINE);
        }
        return mArticles;
    }

    public void refreshArticles() {
        mRepository.refreshHeadlines();
    }
}
