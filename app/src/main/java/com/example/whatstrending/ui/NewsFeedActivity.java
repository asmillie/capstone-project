package com.example.whatstrending.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

    @BindView(R.id.empty_view)
    TextView mEmptyView;
    //TODO: Empty view when no content, loading wheel when fetching new content
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
                    showList();
                } else {
                    hideList();
                }
            }
        });
    }

    private void initViews() {
        mArticleListAdapter = new ArticleListAdapter(null, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mNewsFeedRV.setLayoutManager(layoutManager);
        mNewsFeedRV.setAdapter(mArticleListAdapter);
        mArticleListAdapter.setArticleList(mArticleList);
    }

    private void hideList() {
        mNewsFeedRV.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showList() {
        mNewsFeedRV.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }
}
