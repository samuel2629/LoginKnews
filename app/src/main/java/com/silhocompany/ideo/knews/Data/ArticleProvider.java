package com.silhocompany.ideo.knews.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Samuel on 28/03/2017.
 */

public class ArticleProvider extends ContentProvider {

    private ArticleHelper mArticleHelper;
    public static final String LOG_TAG = ArticleProvider.class.getSimpleName();
    private static final int ARTICLES = 100;
    private static final int ARTICLES_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    public boolean onCreate() {
        mArticleHelper = new ArticleHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mArticleHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLES:
                cursor = db.query(ArticleContract.ArticleEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case ARTICLES_ID:
                selection = ArticleContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ArticleContract.ArticleEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLES :
                return insertArticle(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported" + uri);
        }
    }

    private Uri insertArticle(Uri uri, ContentValues contentValues) {
        String title = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE);
        if(title == null) throw new IllegalArgumentException("Title required");
        String description = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE);
        if(description == null) throw new IllegalArgumentException("Description required");
        String url = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE);
        if(url == null) throw new IllegalArgumentException("url required");
        String urlToImage = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_URL_IMAGE);
        if(urlToImage == null) throw new IllegalArgumentException("Image Required");

        SQLiteDatabase db = mArticleHelper.getWritableDatabase();

        long id = db.insert(ArticleContract.ArticleEntry.TABLE_NAME, null, contentValues);
        if(id == -1){
            return null;}

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mArticleHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match){
            case ARTICLES:
                getContext().getContentResolver().notifyChange(uri, null);
                return db.delete(ArticleContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
            case ARTICLES_ID:
                selection = ArticleContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ArticleContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
                if(rowsDeleted !=0) getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for" + uri);

        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                return updateArticle(uri, contentValues, selection, selectionArgs);
            case ARTICLES_ID:
                selection = ArticleContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateArticle(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateArticle(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if(contentValues.containsKey(ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE)){
            String title = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE);
            if(title == null) throw new IllegalArgumentException("Title required");
        }
        if (contentValues.containsKey(ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE)) {
            String description = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE);
            if(description == null) throw new IllegalArgumentException("Description required");
        }
        if(contentValues.containsKey(ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE)){
            String url = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE);
            if(url == null) throw new IllegalArgumentException("url required");
        }
        if(contentValues.containsKey(ArticleContract.ArticleEntry.COLUMN_URL_IMAGE)) {
            String url = contentValues.getAsString(ArticleContract.ArticleEntry.COLUMN_URL_IMAGE);
            if (url == null) throw new IllegalArgumentException("image required");
        }
        if(contentValues.size() == 0)return 0;

        SQLiteDatabase db = mArticleHelper.getWritableDatabase();

        int rowsUpdated = db.update(ArticleContract.ArticleEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if(rowsUpdated != 0) getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    static {

        sUriMatcher.addURI(ArticleContract.ArticleEntry.CONTENT_AUTHORITY,
                ArticleContract.ArticleEntry.PATH_ARTICLES,
                ARTICLES);

        sUriMatcher.addURI(ArticleContract.ArticleEntry.CONTENT_AUTHORITY,
                ArticleContract.ArticleEntry.PATH_ARTICLES + "/#",
                ARTICLES_ID);
    }
}
