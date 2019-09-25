package com.silhocompany.ideo.knews.Fragments.Container;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Fragments.FutureNews.SavedArticlesFragment;
import com.silhocompany.ideo.knews.Fragments.PresentNews.NewsFragment;
import com.silhocompany.ideo.knews.Models.Article;

import java.util.ArrayList;

/**
 * Created by Samuel on 27/02/2017.
 */

public class WebViewFragment extends Fragment {

    public static final String WEBVIEWFRAGMENT = "web_view_fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int position = getArguments().getInt(ViewPagerFragment.KEY_NEW_INDEX);
        ArrayList<Article> mArticles = getArguments().getParcelableArrayList(NewsFragment.KEY_LIST);

        String url = getArguments().getString(SavedArticlesFragment.KEY_STRING_URL);

        View view = inflater.inflate(R.layout.webview, container, false);
        WebView webView = view.findViewById(R.id.webView);

        if(mArticles == null) webView.loadUrl(url);
        else webView.loadUrl(mArticles.get(position).getArticleUrl());

        return view;
    }


}
