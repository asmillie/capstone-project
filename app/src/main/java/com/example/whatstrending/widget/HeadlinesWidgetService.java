package com.example.whatstrending.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

import java.util.List;

public class HeadlinesWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new HeadlinesRemoteViewsFactory(this.getApplicationContext());
    }
}

class HeadlinesRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;

    private AppRepository mAppRepository;
    private List<Article> mArticleList;

    HeadlinesRemoteViewsFactory(Context context) { this.mContext = context; }

    @Override
    public void onCreate() {
        mAppRepository = AppRepository.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
        mArticleList = mAppRepository.getTopHeadlines(Constants.WIDGET_HEADLINE_LIMIT);
    }

    @Override
    public void onDestroy() {
        mAppRepository = null;
        mArticleList = null;
    }

    @Override
    public int getCount() {
        return mArticleList == null ? 0 : mArticleList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() == 0) {
            return null;
        }

        final Article article = mArticleList.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_headline_list_item);
        views.setTextViewText(R.id.title, article.getTitle());

        Bundle extras = new Bundle();
        extras.putInt(Constants.EXTRA_ARTICLE_ID, article.getId());

        Intent intent = new Intent();
        intent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.headline_list_item, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
