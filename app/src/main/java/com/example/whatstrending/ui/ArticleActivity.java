package com.example.whatstrending.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = ArticleActivity.class.getSimpleName();

    private List<Article> mArticleIds;
    private int mSelectedArticleId;

    @BindView(R.id.article_fragment_pager)
    ViewPager mArticleFragmentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        final int articleId = intent.getIntExtra(Constants.EXTRA_ARTICLE_ID, Constants.EXTRA_ARTICLE_ID_DEFAULT);

        if (articleId == Constants.EXTRA_ARTICLE_ID_DEFAULT) {
            finish();
        } else {
            mSelectedArticleId = articleId;
        }

        initViewModel();
    }

    private void initFragment(final int articleId) {
        ArticleFragment fragment = ArticleFragment.newInstance(articleId);
        Log.i(TAG, "Created fragment for " + articleId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.article_fragment_container, fragment).commit();
    }

    private void initViewModel() {
        ArticleViewModel viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        viewModel.getArticleIds().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null) {
                    mArticleIds = articles;
                }
            }
        });
    }

    private class ArticlePagerAdapter extends FragmentStatePagerAdapter {

        public ArticlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //TODO: Get next article id and create/return fragment
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
