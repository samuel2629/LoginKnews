package com.silhocompany.ideo.knews.widget;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.silhocompany.ideo.knews.Models.Article;
import com.silhocompany.ideo.knews.R;

import java.util.ArrayList;

/**
 * Created by Samuel on 01/06/2018.
 */

public class NewsProvider implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Article> listItemList = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;

    public NewsProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        if(RemoteFetchService.mArticleArrayList !=null )
            listItemList = (ArrayList<Article>) RemoteFetchService.mArticleArrayList.clone();
        else
            listItemList = new ArrayList<Article>();

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.list_row);
        Article listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.row_widget, listItem.getTitle());
        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }
}
