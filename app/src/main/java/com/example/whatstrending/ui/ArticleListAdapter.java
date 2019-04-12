package com.example.whatstrending.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private static final String TAG = ArticleListAdapter.class.getSimpleName();

    private List<Article> mArticleList;
    private Context mContext;
    private final ArticleClickListener mArticleClickListener;

    public interface ArticleClickListener {
        void onArticleClick(int articleId);
    }

    ArticleListAdapter(List<Article> articleList, ArticleClickListener clickListener) {
        this.mArticleList = articleList;
        this.mArticleClickListener = clickListener;
    }

    void setArticleList(List<Article> articleList) {
        this.mArticleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.article_list_item, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = mArticleList.get(position);

        holder.mTitleTV.setText(article.getTitle());
        holder.mDescription.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return mArticleList != null ? mArticleList.size() : 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        TextView mTitleTV;

        @BindView(R.id.description)
        TextView mDescription;

        @BindView(R.id.view_article_btn)
        Button mViewArticleBtn;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mViewArticleBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mArticleList.get(getAdapterPosition()).getId();
            mArticleClickListener.onArticleClick(elementId);
        }
    }
}
