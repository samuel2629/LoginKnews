package com.silhocompany.ideo.knews.Data;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Samuel on 27/03/2017.
 */

public class ArticleContract {

    private ArticleContract() {
    }

    public static final class ArticleEntry implements BaseColumns {

        public static final String CONTENT_AUTHORITY = "com.silhocompany.ideo.knews";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_ARTICLES = "articles";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARTICLES);

        public static final String TABLE_NAME = "articles";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE_ARTICLE = "title";
        public static final String COLUMN_DESCRIPTION_ARTICLE = "description";
        public static final String COLUMN_URL_ARTICLE = "url";
        public static final String COLUMN_URL_IMAGE = "urlToImage";
    }

}
