package com.example.whatstrending.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.whatstrending.data.AppRepository;

class ArticleFragmentViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final AppRepository mRepository;
    private final int mArticleId;

    public ArticleFragmentViewModelFactory(@NonNull Application application, int articleId) {
        super(application);
        this.mRepository = AppRepository.getInstance(application.getApplicationContext());
        this.mArticleId = articleId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ArticleFragmentViewModel(mRepository, mArticleId);
    }
}
