package com.example.whatstrending.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;

import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = ArticleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();
        final int articleId = intent.getIntExtra(Constants.EXTRA_ARTICLE_ID, Constants.EXTRA_ARTICLE_ID_DEFAULT);

        if (articleId == Constants.EXTRA_ARTICLE_ID_DEFAULT) {
            finish();
        }
        Log.i(TAG, "Activity started for article id " + articleId);
        initViewModel(articleId);
        initFragment(articleId);
    }

    private void initFragment(final int articleId) {
        ArticleFragment fragment = ArticleFragment.newInstance(articleId);
        Log.i(TAG, "Created fragment for " + articleId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.article_fragment_container, fragment).commit();
    }

    private void initViewModel(final int articleId) {
        ArticleViewModelFactory factory = new ArticleViewModelFactory(getApplication(), articleId);

        ArticleViewModel viewModel = ViewModelProviders.of(this, factory).get(ArticleViewModel.class);
    }
}
