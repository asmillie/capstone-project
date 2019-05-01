package com.example.whatstrending.ui;

import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;
import com.example.whatstrending.data.NewsApiResponse;
import com.example.whatstrending.data.SearchArticlesIntentService;
import com.example.whatstrending.network.NewsApiClient;
import com.example.whatstrending.network.NewsApiService;
import com.example.whatstrending.utils.AnalyticsUtils;
import com.example.whatstrending.utils.NetworkUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements ArticleListAdapter.ArticleClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final String QUERY_EXTRA = "query";

    private FirebaseAnalytics mFirebaseAnalytics;

    private SearchViewModel mViewModel;
    private List<Article> mArticleList;
    private ArticleListAdapter mArticleListAdapter;
    private String mQuery;
    private Dialog mAlertDialog;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.article_search_results)
    RecyclerView mArticleSearchResults;

    @BindView(R.id.loading_bar)
    ProgressBar mLoadingBar;

    @BindView(R.id.search_articles)
    SearchView mSearchView;

    @BindView(R.id.empty_view)
    TextView mEmptyView;

    @BindBool(R.bool.is_large_screen_device)
    boolean mIsLargeScreenDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initViewModel();
        initViews();
        initSearch();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                mQuery = intent.getStringExtra(SearchManager.QUERY);
                SearchArticlesIntentService.startActionSearchArticles(this, mQuery);
                showLoading();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isConnected(this)) {
            //Show offline mode
            noInternetDialog();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY_EXTRA, mQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState.containsKey(QUERY_EXTRA)) {
            mQuery = savedInstanceState.getString(QUERY_EXTRA);
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

    @Override
    public void onArticleClick(int articleId) {
        AnalyticsUtils.logArticleSelect(mFirebaseAnalytics, articleId);

        Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_CATEGORY, Constants.ARTICLE_CATEGORY_SEARCH_RESULT);
        startActivity(intent);
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.search_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mArticleListAdapter = new ArticleListAdapter(null, this);

        RecyclerView.LayoutManager layoutManager;
        if (!mIsLargeScreenDevice) {
            layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        }

        mArticleSearchResults.setLayoutManager(layoutManager);
        mArticleSearchResults.setAdapter(mArticleListAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mArticleSearchResults.addItemDecoration(itemDecoration);

        mArticleListAdapter.setArticleList(mArticleList);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        mViewModel.getArticleSearchResults().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null && articles.size() != 0) {
                    mArticleList = articles;
                    updateList();
                } else {
                    showEmptyResults();
                }
            }
        });
    }

    private void initSearch() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());

        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);
    }

    private void updateList() {
        mArticleListAdapter.setArticleList(mArticleList);
        mArticleListAdapter.notifyDataSetChanged();
        showSearchResults();
    }

    private void showLoading() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mArticleSearchResults.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);

    }

    private void showSearchResults() {
        mLoadingBar.setVisibility(View.GONE);
        mArticleSearchResults.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    private void showEmptyResults() {
        mLoadingBar.setVisibility(View.GONE);
        mArticleSearchResults.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void noInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.alert_dialog_no_internet, null))
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Reload activity solution found @ https://stackoverflow.com/questions/3053761/reload-activity-in-android
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.cancel();
                    }
                });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }
}
