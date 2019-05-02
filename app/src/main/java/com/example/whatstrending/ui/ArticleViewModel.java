package com.example.whatstrending.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.whatstrending.Constants;
import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

import java.util.List;

class ArticleViewModel extends AndroidViewModel {

    private final AppRepository mRepository;
    private LiveData<List<Article>> mArticleIds;
    private String mArticleCategory;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
        setArticleCategory(Constants.ARTICLE_CATEGORY_HEADLINE); //Default to Headlines
    }

    public LiveData<List<Article>> getArticleIds() {
        if (mArticleIds == null) {
            refreshArticleIds();
        }
        return mArticleIds;
    }

    public void setArticleCategory(String category) {
        mArticleCategory = category;
    }

    private void refreshArticleIds() {
        mArticleIds = mRepository.getAllArticleIdsByCategory(mArticleCategory);
    }
}
