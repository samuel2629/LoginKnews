package com.silhocompany.ideo.knews.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Adapter.ArticleAdapter;
import com.silhocompany.ideo.knews.Data.ArticleContract;
import com.silhocompany.ideo.knews.Fragments.Container.ViewPagerFragment;
import com.silhocompany.ideo.knews.Fragments.Container.WebViewFragment;
import com.silhocompany.ideo.knews.Fragments.FutureNews.SavedArticlesFragment;
import com.silhocompany.ideo.knews.Fragments.PastNews.OldFactsFragment;
import com.silhocompany.ideo.knews.Fragments.PresentNews.NewsFragment;
import com.silhocompany.ideo.knews.Models.Article;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsFragment.OnNewSelectedInterface,
        ArticleAdapter.OnNewsInsertedInterface {

    protected static final String VIEWPAGER_FRAGMENT = "view_pager_fragment";
    protected static final String OLD_FACT_FRAGMENT = "old_fact_frag";
    protected static final String SAVED_ARTICLES_FRAGMENT = "saved_articles_fragment";
    protected Toast mToastSaved;
    protected Toast mToastAlreadySaved;

    public OldFactsFragment getOldFactFragment() {
        return mOldFactFragment;
    }

    private OldFactsFragment mOldFactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getSavedArticleActivity(Activity activity){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        Intent intent = new Intent(activity, SavedArticleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getOldFactActivity(Activity activity){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        Intent intent = new Intent(activity, OldFactActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getActualNewsActivity(Activity activity){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        Intent intent = new Intent(activity, ActualNewsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getLicenceActivity(Activity activity){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        Intent intent = new Intent(activity, LicenceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, options.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getSavedArticlesFragment(Activity activity){
        SavedArticlesFragment savedFragment = (SavedArticlesFragment) getSupportFragmentManager().findFragmentByTag(SAVED_ARTICLES_FRAGMENT);
        if(savedFragment == null){
            SavedArticlesFragment savedArticlesFragment = new SavedArticlesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.containerframe, savedArticlesFragment, SAVED_ARTICLES_FRAGMENT);
            //transaction.addToBackStack(null);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            Bundle bundle = new Bundle();
            bundle.putAll(options.toBundle());
            transaction.commit();
            setTitle(getString(R.string.saved_articles));
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerframe, savedFragment).commit();
            setTitle(getString(R.string.saved_articles));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getOldFactFragment(Activity activity) {

        OldFactsFragment savedFragment = (OldFactsFragment) getSupportFragmentManager().findFragmentByTag(OLD_FACT_FRAGMENT);
        if(savedFragment == null){
            mOldFactFragment = new OldFactsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.containerframe, mOldFactFragment, OLD_FACT_FRAGMENT);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            Bundle bundle = new Bundle();
            bundle.putAll(options.toBundle());
            transaction.commit();
            setTitle(getString(R.string.prev_today));
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerframe, savedFragment).commit();
            setTitle(getString(R.string.prev_today));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void getActualNewsFragment(Activity activity) {
        ViewPagerFragment savedFragment = (ViewPagerFragment)
                getSupportFragmentManager().findFragmentByTag(VIEWPAGER_FRAGMENT);

        if (savedFragment == null){
            ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.containerframe, viewPagerFragment, VIEWPAGER_FRAGMENT);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            Bundle bundle = new Bundle();
            bundle.putAll(options.toBundle());
            transaction.commit();
            setTitle(getString(R.string.around_world));
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerframe, savedFragment).commit();
            setTitle(getString(R.string.around_world));
        }
    }

    @Override
    public void onNewsSelected(int index, ArrayList<Article> articles) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ViewPagerFragment.KEY_NEW_INDEX, index);
        bundle.putParcelableArrayList(NewsFragment.KEY_LIST, articles);
        webViewFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.containerframe, webViewFragment, WebViewFragment.WEBVIEWFRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onNewsSaved(Article article, FloatingActionButton saveButton) {

        if(mToastSaved != null || mToastAlreadySaved != null){
            mToastSaved.cancel();
            mToastAlreadySaved.cancel();}

        String title = article.getTitle();
        String description = article.getDescription();
        String url = article.getArticleUrl();
        String urlToImage = article.getUrlToImage();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleContract.ArticleEntry.COLUMN_TITLE_ARTICLE, title);
        contentValues.put(ArticleContract.ArticleEntry.COLUMN_DESCRIPTION_ARTICLE, description);
        contentValues.put(ArticleContract.ArticleEntry.COLUMN_URL_ARTICLE, url);
        contentValues.put(ArticleContract.ArticleEntry.COLUMN_URL_IMAGE, urlToImage);

        Uri uri = getContentResolver().insert(ArticleContract.ArticleEntry.CONTENT_URI, contentValues);

        mToastSaved = Toast.makeText(this, R.string.article_saved_toast, Toast.LENGTH_SHORT);
        mToastAlreadySaved = Toast.makeText(this, R.string.article_already_saved_toast, Toast.LENGTH_SHORT);

        if(uri == null) {
            mToastAlreadySaved.show();
        } else mToastSaved.show();
    }
}
