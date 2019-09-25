package com.silhocompany.ideo.knews.Fragments.PresentNews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Adapter.ArticleAdapter;
import com.silhocompany.ideo.knews.Fragments.PopUp.AlertDialogFragment;
import com.silhocompany.ideo.knews.Models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public abstract class NewsFragment extends Fragment {

    private static final String TAAG = NewsFragment.class.getSimpleName();
    protected ArticleAdapter mArticleAdapter;
    protected RecyclerView mRecyclerView;
    protected NewsFragment.OnNewSelectedInterface mListener;
    protected ArticleAdapter.OnNewsInsertedInterface mListener2;
    protected RecyclerView.LayoutManager mManager;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    public static final String KEY_LIST = "key_list";

    public interface OnNewSelectedInterface {
        void onNewsSelected(int index, ArrayList<Article> articles);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.list_present_news, container, false);

        mListener = (NewsFragment.OnNewSelectedInterface) getActivity();
        mListener2 = (ArticleAdapter.OnNewsInsertedInterface) getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mManager = new LinearLayoutManager(getActivity());
        mArticleAdapter = new ArticleAdapter(getActivity(), new ArrayList<>(), mListener, mListener2);

        if (!isNetworkAvailable()) alertUserAboutError();

        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.setLayoutManager(mManager);

        mSwipeRefreshLayout.setRefreshing(true);
        new Downloader().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, getUrl());

        refreshData();

        setDividerRecyclerView();

        return view;
    }

    private void setDividerRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView
                .getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void alertUserAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getActivity().getFragmentManager(), "error_dialog");
    }

    protected abstract String[] getUrl();

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void refreshData() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mArticleAdapter.clear();
            new Downloader().execute(getUrl());
        });

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private class Downloader extends AsyncTask<String, Void, ArrayList<Article>> {

        ArrayList<Article> mArticleArrayList = new ArrayList<>();
        OkHttpClient client = getUnsafeOkHttpClient();

        private OkHttpClient getUnsafeOkHttpClient() {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                for(X509Certificate certificate: chain){
                                    certificate.checkValidity();
                                    Log.d(TAAG, certificate.toString());
                                }
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                for(X509Certificate certificate: chain){
                                    certificate.checkValidity();
                                }
                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
                builder.hostnameVerifier((hostname, session) -> {
                    if("newsapi.org".equalsIgnoreCase(hostname)){
                    return true;}
                    else return false;
                });

                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected ArrayList<Article> doInBackground(String... strings) {

            for (String aMUrl : getUrl()) {
                Request mRequest = new Request.Builder().url(aMUrl).build();
                try {
                    Response response = client.newCall(mRequest).execute();
                    try {
                        if (response.isSuccessful()) {
                            String json = response.body().string();
                            mArticleArrayList = getMultipleUrls(json);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return mArticleArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            mArticleAdapter.addAll(articles);
            mSwipeRefreshLayout.setRefreshing(false);
            Log.v(TAAG, String.valueOf(mArticleAdapter.getItemCount()));
        }

        private ArrayList<Article> getMultipleUrls(String jsonData) throws JSONException {

            if (mArticleArrayList == null || mArticleArrayList.size() == 0) {
                mArticleArrayList = getArticleForecast(jsonData);
            } else {
                mArticleArrayList.addAll(getArticleForecast(jsonData));
            }

            return mArticleArrayList;
        }

        private ArrayList<Article> getArticleForecast(String jsonData) throws JSONException {
            JSONObject forecast = new JSONObject(jsonData);
            JSONArray articles = forecast.getJSONArray("articles");

            ArrayList<Article> listArticles = new ArrayList<>(articles.length());

            for (int i = 0; i < articles.length(); i++) {
                JSONObject jsonArticle = articles.getJSONObject(i);
                Article article = new Article();

                String urlImage = jsonArticle.getString("urlToImage");

                article.setTitle(jsonArticle.getString("title"));
                article.setDescription(jsonArticle.getString("description"));
                article.setImageView(urlImage);
                article.setArticleUrl(jsonArticle.getString("url"));
                article.setUrlToImage(jsonArticle.getString("urlToImage"));

                listArticles.add(i, article);
            }

            return listArticles;
        }
    }
}