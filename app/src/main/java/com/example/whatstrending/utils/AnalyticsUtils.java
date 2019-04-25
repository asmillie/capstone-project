package com.example.whatstrending.utils;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsUtils {

    private AnalyticsUtils() {}

    public static void logArticleSelect(FirebaseAnalytics analytics, int articleId) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, articleId + "");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "article");
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
