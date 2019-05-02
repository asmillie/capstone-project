package com.example.whatstrending.ui;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = ArticleActivity.class.getSimpleName();

    private List<Article> mArticleIds;
    private int mSelectedArticleId = Constants.EXTRA_ARTICLE_ID_DEFAULT;
    private String mArticleCategory;

    private ArticlePagerAdapter mPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.article_fragment_pager)
    ViewPager mPager;

    @BindView(R.id.search_articles)
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            final int articleId = intent.getIntExtra(Constants.EXTRA_ARTICLE_ID, Constants.EXTRA_ARTICLE_ID_DEFAULT);
            mArticleCategory = intent.getStringExtra(Constants.EXTRA_CATEGORY);

            if (articleId == Constants.EXTRA_ARTICLE_ID_DEFAULT) {
                finish();
            } else {
                mSelectedArticleId = articleId;
            }
        }

        initPager();
        initViewModel();
        initSearch();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.EXTRA_ARTICLE_ID, mSelectedArticleId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState.containsKey(Constants.EXTRA_ARTICLE_ID)) {
            mSelectedArticleId = savedInstanceState.getInt(Constants.EXTRA_ARTICLE_ID);
            updatePager();
        }
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePager() {
        boolean pagerUpdated = false;
        if (mArticleIds != null && mSelectedArticleId != Constants.EXTRA_ARTICLE_ID_DEFAULT) {
            for (int index=0; index<mArticleIds.size(); index++) {
                int articleId = mArticleIds.get(index).getId();
                if (articleId == mSelectedArticleId) {
                    mPager.setCurrentItem(index);
                    pagerUpdated = true;
                    break;
                }
            }
        }

        if (!pagerUpdated) {
            mPager.setCurrentItem(0);
        }
    }

    private void initPager() {
        mPagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    private void initSearch() {
        ComponentName cn = new ComponentName(this, SearchActivity.class);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(cn);

        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);
    }

    private void initViewModel() {
        ArticleViewModel viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        viewModel.setArticleCategory(mArticleCategory);
        viewModel.getArticleIds().observe(this, articles -> {
            if (articles != null) {
                mArticleIds = articles;
                mPagerAdapter.notifyDataSetChanged();
                updatePager();
                Log.i(TAG, "Observed change to article ids list, contains " + mArticleIds.size() + " items");
            }
        });
    }

    private class ArticlePagerAdapter extends FragmentStatePagerAdapter {

        ArticlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mSelectedArticleId = mArticleIds.get(position).getId();
            Log.i(TAG, "Pager Adapter getting item for position " + position + " for article id " + mSelectedArticleId);
            return ArticleFragment.newInstance(mSelectedArticleId);
        }

        @Override
        public int getCount() {
            return (mArticleIds != null) ? mArticleIds.size() : 0;
        }
    }
}
