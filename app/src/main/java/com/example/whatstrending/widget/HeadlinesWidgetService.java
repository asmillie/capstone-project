package com.example.whatstrending.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.whatstrending.Constants;
import com.example.whatstrending.data.AppRepository;
import com.example.whatstrending.data.Article;

import java.util.List;

public class HeadlinesWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
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
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
