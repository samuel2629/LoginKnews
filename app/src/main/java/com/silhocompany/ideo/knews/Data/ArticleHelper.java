package com.silhocompany.ideo.knews.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Samuel on 27/03/2017.
 */

public class ArticleHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "articles.db";


    public ArticleHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ARTICLES_TABLE = "CREATE TABLE " + ArticleContract.ArticleEntry.TABLE_NAME
                + " (" + ArticleContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE + " TEXT UNIQUE, "
                + ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE + " TEXT UNIQUE, "
                + ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE + " TEXT UNIQUE, "
                + ArticleContract.ArticleEntry.COLUMN_URL_IMAGE + " TEXT UNIQUE)";

        sqLiteDatabase.execSQL(SQL_CREATE_ARTICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
