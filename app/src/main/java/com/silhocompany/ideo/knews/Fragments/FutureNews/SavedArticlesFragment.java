package com.silhocompany.ideo.knews.Fragments.FutureNews;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Adapter.ArticleCursorAdapter;
import com.silhocompany.ideo.knews.Data.ArticleContract;

public class SavedArticlesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String KEY_STRING_URL = "string_url";
    private static final int ARTICLE_LOADER = 0;
    private ArticleCursorAdapter mArticleCursorAdapter;
    private ArticleCursorAdapter.OnSavedArticleClicked mListener;
    private TextView mTextView;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_future_news, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerviewFuture);
        mTextView  = view.findViewById(R.id.emptyArticleSavedTextView);

        mListener = (ArticleCursorAdapter.OnSavedArticleClicked) getActivity();
        mArticleCursorAdapter = new ArticleCursorAdapter(getActivity(), null, mListener);

        mRecyclerView.setAdapter(mArticleCursorAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().getSupportLoaderManager().initLoader(ARTICLE_LOADER, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT) {

                    long id = (long) viewHolder.itemView.getTag();
                    Uri uri1 = ContentUris.withAppendedId(ArticleContract.ArticleEntry.CONTENT_URI, id);
                    int ro = getContext().getContentResolver().delete(uri1, null, null);
                    if (ro != 0) {
                        Toast.makeText(getContext(), R.string.item_deleted, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getContext(), R.string.no_item_deleted, Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(mRecyclerView);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ArticleContract.ArticleEntry._ID,
                ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE,
                ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE,
                ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE,
                ArticleContract.ArticleEntry.COLUMN_URL_IMAGE
        };

        return new CursorLoader(getActivity(),
                ArticleContract.ArticleEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mArticleCursorAdapter.swapCursor(data);
        if(mArticleCursorAdapter.getItemCount() == 0){
            mRecyclerView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            String string = getString(R.string.no_article_saved);
            mTextView.setText(string);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mArticleCursorAdapter.swapCursor(null);
    }

}
