package com.example.whatstrending;

public class Constants {

    private Constants() {}

    /** App Path **/
    private static final String APP_BASE_URI = "com.example.whatstrending";

    public static final String EXTRA_ARTICLE_ID = "article-id";

    public static final int EXTRA_ARTICLE_ID_DEFAULT = -1;

    /** Widget Constants **/
    public static final int WIDGET_HEADLINE_LIMIT = 25;

    public static final String WIDGET_ACTION_PREFIX = APP_BASE_URI + ".WIDGET_ACTION.";

    public static final String WIDGET_VIEW_ARTICLE_ACTION = WIDGET_ACTION_PREFIX + "VIEW_ARTICLE";
}
