package com.example.whatstrending.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.whatstrending.Constants;
import com.example.whatstrending.R;
import com.example.whatstrending.ui.ArticleActivity;

/**
 * Implementation of App Widget functionality.
 */
public class LatestHeadlinesAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = getHeadlines(context, appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getHeadlines(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.latest_headlines_app_widget);

        //Set List Adapter for Headlines
        Intent intent = new Intent(context, HeadlinesWidgetService.class);
        views.setRemoteAdapter(R.id.latest_headlines, intent);
        views.setEmptyView(R.id.latest_headlines, R.id.empty_view);
        //Set Intent with View Article action
        Intent viewArticleIntent = new Intent(context, ArticleActivity.class);
        viewArticleIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        viewArticleIntent.setAction(Constants.WIDGET_VIEW_ARTICLE_ACTION);
        //Set Pending Intent for each headline list item, assigning the view article intent to each
        PendingIntent selectArticlePendingIntent = PendingIntent.getBroadcast(context,
                0,
                viewArticleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.latest_headlines, selectArticlePendingIntent);

        return views;
    }
}

