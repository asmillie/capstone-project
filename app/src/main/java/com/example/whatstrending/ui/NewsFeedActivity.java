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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;
import com.example.whatstrending.utils.AnalyticsUtils;
import com.example.whatstrending.utils.NetworkUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity implements ArticleListAdapter.ArticleClickListener {

    private static final String TAG = NewsFeedActivity.class.getSimpleName();

    private NewsFeedViewModel mViewModel;

    private List<Article> mArticleList;
    private ArticleListAdapter mArticleListAdapter;
    private Snackbar mSnackbar;
    private Dialog mAlertDialog;
    //Track if activity just loaded
    private boolean mInitialLoad;
    //Track if user requested data update
    private boolean mUserRefresh;

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.news_feed_rv)
    RecyclerView mNewsFeedRV;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.parent_layout)
    CoordinatorLayout mParentLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.search_articles)
    SearchView mSearchView;

    @BindBool(R.bool.is_large_screen_device)
    boolean mIsLargeScreenDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        mInitialLoad = true;
        mUserRefresh = false;

        initViewModel();
        initViews();
        initSearch();
        initAd();
        initAnimations();
        runNewsFeedAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isConnected(this)) {
            //Show offline mode
            noInternetDialog();
        } else {
            runNewsFeedAnimation();
        }
    }

    @Override
    public void onArticleClick(int articleId) {
        AnalyticsUtils.logArticleSelect(mFirebaseAnalytics, articleId);

        Intent intent = new Intent(NewsFeedActivity.this, ArticleActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_CATEGORY, Constants.ARTICLE_CATEGORY_HEADLINE);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_action) {
            mSwipeRefreshLayout.setRefreshing(true);
            refreshList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);

        mViewModel.getAllArticles().observe(this, articles -> {
            mArticleList = articles;
            if (articles == null || articles.size() == 0) {
                return;
            }
            Log.i(TAG, "Observed changed to article list and list contains items");
            if (mSwipeRefreshLayout.isRefreshing()) {
                updateList(); //User requested refresh, update immediately
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                if (mInitialLoad || mUserRefresh) {
                    //Activity was just created or user requested data refresh
                    updateList();
                    mInitialLoad = false;
                } else {
                    //Data retrieved by scheduled job, present user option to refresh
                    showSnackBar();
                }
            }
        });
    }

    private void initViews() {
        mArticleListAdapter = new ArticleListAdapter(null, this);

        RecyclerView.LayoutManager layoutManager;
        if (!mIsLargeScreenDevice) {
            layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        }

        mNewsFeedRV.setLayoutManager(layoutManager);
        mNewsFeedRV.setAdapter(mArticleListAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mNewsFeedRV.addItemDecoration(itemDecoration);

        mArticleListAdapter.setArticleList(mArticleList);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (NetworkUtils.isConnected(NewsFeedActivity.this)) {
                mUserRefresh = true;
                refreshList();
            }
        });
    }

    private void initSearch() {
        ComponentName cn = new ComponentName(this, SearchActivity.class);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(cn);

        mSearchView.setSearchableInfo(searchableInfo);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setSubmitButtonEnabled(true);
    }

    private void initAd() {
        MobileAds.initialize(this, getString(R.string.admob_sample_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void showSnackBar() {
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
        mSnackbar = Snackbar.make(mParentLayout, R.string.new_headlines_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.refresh_action, v -> updateList());
        mSnackbar.show();
    }

    private void refreshList() {
        if (mViewModel == null) {
            initViewModel();
        }
        mViewModel.refreshArticles();
    }

    private void updateList() {
        if (mArticleList != null && mArticleList.size() > 0) {
            mArticleListAdapter.setArticleList(mArticleList);
            mArticleListAdapter.notifyDataSetChanged();
            mNewsFeedRV.scheduleLayoutAnimation();
        }
    }
    // Animation on rv following guide @ https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213
    private void initAnimations() {
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(mNewsFeedRV.getContext(), R.anim.layout_anim_fall_down);

        mNewsFeedRV.setLayoutAnimation(controller);
    }

    private void runNewsFeedAnimation() {
        mNewsFeedRV.scheduleLayoutAnimation();
    }

    private void noInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeedActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.alert_dialog_no_internet, null))
                .setPositiveButton(R.string.retry, (dialog, which) -> {
                    //Reload activity solution found @ https://stackoverflow.com/questions/3053761/reload-activity-in-android
                    finish();
                    startActivity(getIntent());
                })
                .setNegativeButton(R.string.close, (dialog, which) -> mAlertDialog.cancel());

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }
}
