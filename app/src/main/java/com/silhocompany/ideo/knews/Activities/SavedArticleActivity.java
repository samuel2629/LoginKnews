package com.silhocompany.ideo.knews.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Adapter.ArticleCursorAdapter;
import com.silhocompany.ideo.knews.Data.ArticleContract;
import com.silhocompany.ideo.knews.Fragments.Container.WebViewFragment;
import com.silhocompany.ideo.knews.Fragments.FutureNews.SavedArticlesFragment;

public class SavedArticleActivity extends MainActivity implements NavigationView.OnNavigationItemSelectedListener,
        ArticleCursorAdapter.OnSavedArticleClicked{


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.hideOverflowMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getSavedArticlesFragment(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_articles_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.remove_all){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.remove_items_question)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> getContentResolver().delete(ArticleContract.ArticleEntry.CONTENT_URI, null, null))
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel());
            alertDialog.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getOldFactActivity(this);
        } else if (id == R.id.nav_gallery) {
            getActualNewsActivity(this);
        } else if (id == R.id.nav_slideshow) {
            getSavedArticlesFragment(this);
        } else if (id == R.id.nav_share) {
            getLicenceActivity(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSavedArticleClickedOn(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();

        bundle.putString(SavedArticlesFragment.KEY_STRING_URL, url);
        webViewFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerframe, webViewFragment, WebViewFragment.WEBVIEWFRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
