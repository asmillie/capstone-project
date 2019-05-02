package com.example.whatstrending.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

class ArticleFragmentViewModel extends ViewModel {

    private final AppRepository mRepository;
    private final int mArticleId;

    private LiveData<Article> mArticle;

    ArticleFragmentViewModel(AppRepository appRepository, int articleId) {
        this.mRepository = appRepository;
        this.mArticleId = articleId;
    }

    LiveData<Article> getArticle() {
        if (mArticle == null) {
            refreshArticle();
        }
        return mArticle;
    }

    private void refreshArticle() {
        mArticle = mRepository.getArticleById(mArticleId);
    }
}
