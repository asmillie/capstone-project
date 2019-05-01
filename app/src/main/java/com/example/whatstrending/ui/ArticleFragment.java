package com.example.whatstrending.ui;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;
import com.example.whatstrending.utils.DateUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {

    private static final String TAG = ArticleFragment.class.getSimpleName();

    private int mArticleId;
    private Unbinder mUnbinder;

    private ArticleFragmentViewModel mViewModel;
    private Article mArticle;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.date_published)
    TextView mDatePublished;

    @BindView(R.id.author)
    TextView mAuthor;

    @BindView(R.id.content)
    TextView mContent;

    @BindView(R.id.article_image)
    ImageView mArticleImage;

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance(int articleId) {
        Log.i(TAG, "Instantiating fragment for article id " + articleId);
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticleId = getArguments().getInt(Constants.EXTRA_ARTICLE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mArticleId = savedInstanceState.getInt(Constants.EXTRA_ARTICLE_ID);
        }

        View view = inflater.inflate(R.layout.fragment_article, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initViewModel();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onResume() {
        if (mArticleId != Constants.EXTRA_ARTICLE_ID_DEFAULT && mViewModel == null) {
            initViewModel();
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.EXTRA_ARTICLE_ID, mArticleId);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.view_original_btn)
    public void viewOriginalArticle() {
        if (mArticle != null && mArticle.getUrl() != null && !mArticle.getUrl().equals("")) {
            final Uri articleURL = Uri.parse(mArticle.getUrl());
            final Intent intent = new Intent(Intent.ACTION_VIEW, articleURL);
            try {
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "Error getting package manager: " + e.toString());
                Toast.makeText(getActivity(), "Error while attempting to view original article", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initViewModel() {
        ArticleFragmentViewModelFactory factory = new ArticleFragmentViewModelFactory(getActivity().getApplication(), mArticleId);

        mViewModel = ViewModelProviders.of(this, factory).get(ArticleFragmentViewModel.class);
        mViewModel.getArticle().observe(this, new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                if (article != null) {
                    Log.i(TAG, "Observed change to article for article id " + mArticleId);
                    mArticle = article;
                    populateUI();
                }
            }
        });
    }

    private void populateUI() {
        if (mArticle != null) {
            Log.i(TAG, "Populating views for article " + mArticleId);
            String articleImage = mArticle.getUrlToImage();
            if (articleImage != null && !articleImage.equals("")) {
                Picasso.get()
                        .load(articleImage)
                        .error(R.drawable.ic_broken_image)
                        .into(mArticleImage);
            }

            mTitle.setText(mArticle.getTitle());

            String datePublished = DateUtils.formatUTCDateString(mArticle.getPublishedAt());
            mDatePublished.setText(datePublished);

            mAuthor.setText(mArticle.getAuthor());

            String content = mArticle.getContent();
            if (content == null || content.equals("")) {
                content = getResources().getString(R.string.no_content);
            }
            mContent.setText(content);
        }
    }
}
