package com.example.whatstrending.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.whatstrending.Constants;
import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final AppRepository mAppRepository;
    private LiveData<List<Article>> mArticleSearchResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Article>> getArticleSearchResults() {
        if (mArticleSearchResults == null) {
            refreshSearchResults();
        }
        return mArticleSearchResults;
    }

    private void refreshSearchResults() {
        mArticleSearchResults = mAppRepository.getAllArticlesByCategory(Constants.ARTICLE_CATEGORY_SEARCH_RESULT);
    }
}
