package com.example.whatstrending;

public class Constants {

    private Constants() {}

    /** App Path **/
    private static final String APP_BASE_URI = "com.example.whatstrending";

    public static final String EXTRA_ARTICLE_ID = "article-id";

    public static final int EXTRA_ARTICLE_ID_DEFAULT = -1;

    /** Widget **/
    public static final int WIDGET_HEADLINE_LIMIT = 25;

    public static final String WIDGET_ACTION_PREFIX = APP_BASE_URI + ".WIDGET_ACTION.";

    public static final String WIDGET_VIEW_ARTICLE_ACTION = WIDGET_ACTION_PREFIX + "VIEW_ARTICLE";

    /** News API **/
    public static final int TOTAL_RESULTS_DEFAULT = 0;
    public static final int PAGE_DEFAULT = 1;
    public static final int PAGE_SIZE_DEFAULT = 50;
    public static final int MAX_PAGES = 3; //For testing purposes limit number of pages retrieved
    public static final String DEFAULT_CATEGORY = "technology"; //Retrieve only the tech headlines
    public static final String LANGUAGE_CODE = "en";
    public static final String COUNTRY_CODE = "us";

    /** Article Categories **/
    public static final String ARTICLE_CATEGORY_HEADLINE = "headline";
    public static final String ARTICLE_CATEGORY_SEARCH_RESULT = "search_result";
}
