package com.example.whatstrending.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatstrending.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {

    public static final String EXTRA_ARTICLE_ID = "article-id";

    private int mArticleId;
    private Unbinder mUnbinder;

    public ArticleFragment() {
        // Required empty public constructor
    }

    public static ArticleFragment newInstance(int articleId) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticleId = getArguments().getInt(EXTRA_ARTICLE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mArticleId = savedInstanceState.getInt(EXTRA_ARTICLE_ID);
        }

        View view = inflater.inflate(R.layout.fragment_article, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void initViewModel() {
        ArticleViewModelFactory factory = new ArticleViewModelFactory(getActivity().getApplication(), mArticleId);

        ArticleViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(ArticleViewModel.class);
    }
}
