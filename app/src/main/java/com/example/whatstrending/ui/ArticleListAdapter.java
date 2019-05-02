package com.example.whatstrending.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatstrending.R;
import com.example.whatstrending.data.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private static final String TAG = ArticleListAdapter.class.getSimpleName();

    private List<Article> mArticleList;
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
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.article_list_item, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = mArticleList.get(position);

        holder.mTitleTV.setText(article.getTitle());

        String articleImage = article.getUrlToImage();
        if (articleImage != null && !articleImage.equals("")) {
            Picasso.get()
                    .load(articleImage)
                    .resize(350, 350)
                    .centerCrop(Gravity.TOP)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.mArticleImage);
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList != null ? mArticleList.size() : 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        TextView mTitleTV;

        @BindView(R.id.article_image)
        ImageView mArticleImage;

        @BindView(R.id.list_item)
        ConstraintLayout mListItem;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int elementId = mArticleList.get(getAdapterPosition()).getId();
            mArticleClickListener.onArticleClick(elementId);
        }
    }
}
