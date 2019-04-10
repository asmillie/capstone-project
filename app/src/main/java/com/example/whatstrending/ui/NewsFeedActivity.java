package com.example.whatstrending.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity implements ArticleListAdapter.ArticleClickListener {

    private NewsFeedViewModel mViewModel;

    private List<Article> mArticleList;
    private ArticleListAdapter mArticleListAdapter;

    @BindView(R.id.news_feed_rv)
    RecyclerView mNewsFeedRV;
    //TODO: Empty view when no content, loading wheel when fetching new content
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ButterKnife.bind(this);

        initViewModel();
        initViews();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);

        mViewModel.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                mArticleList = articles;
                //TODO: Show empty view when 0 articles returned
                mArticleListAdapter.setArticleList(mArticleList);
            }
        });
    }

    private void initViews() {
        mArticleListAdapter = new ArticleListAdapter(mArticleList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        mNewsFeedRV.setLayoutManager(layoutManager);
        mNewsFeedRV.setAdapter(mArticleListAdapter);
    }

    @Override
    public void onArticleClick(int articleId) {
        Toast.makeText(this, "Clicked article id " + articleId, Toast.LENGTH_SHORT).show();
    }
}
