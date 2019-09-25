package com.silhocompany.ideo.knews.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Data.ArticleContract;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class ArticleCursorAdapter extends MyRecyclerAdapter<ArticleCursorAdapter.ViewHolder>{

    public interface OnSavedArticleClicked{
        void onSavedArticleClickedOn(String url);
    }

    private OnSavedArticleClicked mListener;

    public ArticleCursorAdapter(Context context, Cursor cursor, OnSavedArticleClicked listener) {
        super(context, cursor);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        int titleColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE);
        int descriptionColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE);
        int urlToImageColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_URL_IMAGE);
        int urlWeb = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE);

        String articleTitle = cursor.getString(titleColumnIndex);
        String articleDescription = cursor.getString(descriptionColumnIndex);
        String urlToImage = cursor.getString(urlToImageColumnIndex);
        String url = cursor.getString(urlWeb);

        Long id = cursor.getLong(cursor.getColumnIndex(ArticleContract.ArticleEntry._ID));
        viewHolder.itemView.setTag(id);

        viewHolder.mTitle.setText(articleTitle);
        viewHolder.mDescription.setText(articleDescription);
        Glide.with(viewHolder.mContext).load(urlToImage).into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(view -> mListener.onSavedArticleClickedOn(url));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView mDescription;
        private final TextView mTitle;
        private final Context mContext;

        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title_saved_article);
            mDescription = (TextView) view.findViewById(R.id.description_saved_article);
            imageView = (ImageView) view.findViewById(R.id.photoImageViewSavedArticle);
            mContext = view.getContext();
        }

    }

}