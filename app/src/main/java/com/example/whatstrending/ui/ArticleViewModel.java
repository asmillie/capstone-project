package com.example.whatstrending.ui;

import android.arch.lifecycle.ViewModel;

import com.example.whatstrending.data.AppRepository;

public class ArticleViewModel extends ViewModel {

    private final AppRepository mRepository;
    private final int mArticleId;

    ArticleViewModel(AppRepository appRepository, int articleId) {
        this.mRepository = appRepository;
        this.mArticleId = articleId;
    }
}
