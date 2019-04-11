package com.example.whatstrending.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity implements ArticleListAdapter.ArticleClickListener {

    private static final String TAG = NewsFeedActivity.class.getSimpleName();

    private NewsFeedViewModel mViewModel;

    private List<Article> mArticleList;
    private ArticleListAdapter mArticleListAdapter;

    @BindView(R.id.news_feed_rv)
    RecyclerView mNewsFeedRV;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.parent_layout)
    CoordinatorLayout mParentLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ButterKnife.bind(this);

        initViewModel();
        initViews();
    }

    @Override
    public void onArticleClick(int articleId) {
        Toast.makeText(this, "Clicked article id " + articleId, Toast.LENGTH_SHORT).show();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);

        mViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                mArticleList = articles;
                Log.i(TAG, "Observed changed to article list");
                //TODO: Snackbar to allow user to refresh article list when new data fetched
                if (articles != null && articles.size() > 0) {
                    mArticleListAdapter.setArticleList(mArticleList);
                    mArticleListAdapter.notifyDataSetChanged();
                } else {
                    //showEmptyView();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initViews() {
        mToolbar.setTitle(getString(R.string.app_name));

        mArticleListAdapter = new ArticleListAdapter(null, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mNewsFeedRV.setLayoutManager(layoutManager);
        mNewsFeedRV.setAdapter(mArticleListAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mNewsFeedRV.addItemDecoration(itemDecoration);

        mArticleListAdapter.setArticleList(mArticleList);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }

    private void showSnackBar() {
        Snackbar.make(mParentLayout, R.string.new_headlines_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.refresh_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }

    private void refreshList() {
        if (mViewModel == null) {
            initViewModel();
        }
        mViewModel.refreshArticles();
    }
}
