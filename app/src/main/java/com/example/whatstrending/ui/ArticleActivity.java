package com.example.whatstrending.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;

import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int articleId = intent.getIntExtra(Constants.EXTRA_ARTICLE_ID, Constants.EXTRA_ARTICLE_ID_DEFAULT);

        if (articleId == Constants.EXTRA_ARTICLE_ID_DEFAULT) {
            finish();
        }

        initViewModel(articleId);
    }

    private void initFragment(int articleId) {
        ArticleFragment fragment = ArticleFragment.newInstance(articleId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.article_fragment_container, fragment).commit();
    }

    private void initViewModel(int articleId) {
        ArticleViewModelFactory factory = new ArticleViewModelFactory(getApplication(), articleId);

        ArticleViewModel viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
    }
}
