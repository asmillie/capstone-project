package com.example.whatstrending.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    private final AppRepository mRepository;
    private LiveData<List<Article>> mArticleIds;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Article>> getArticleIds() {
        if (mArticleIds == null) {
            refreshArticleIds();
        }
        return mArticleIds;
    }

    private void refreshArticleIds() {
        mArticleIds = mRepository.getAllArticleIds();
    }
}
