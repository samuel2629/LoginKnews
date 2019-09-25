package com.silhocompany.ideo.knews.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.silhocompany.ideo.knews.Fragments.PresentNews.NewsFragment;
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

/**
 * Created by Samuel on 01/06/2018.
 */

public class RemoteFetchService extends Service {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public static ArrayList<Article> mArticleArrayList = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        fetchDataFromWeb();
        return super.onStartCommand(intent, flags, startId);
    }

    private void fetchDataFromWeb() {
        new Downloader().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, getUrl());

    }

    private void populateWidget() {

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(NewAppWidget.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        sendBroadcast(widgetUpdateIntent);

        this.stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected String[] getUrl() {
        return new String[]{
                "https://newsapi.org/v1/articles?source=bbc-news&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=time&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-guardian-uk&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=the-guardian-au&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=metro&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=independent&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",
                "https://newsapi.org/v1/articles?source=associated-press&sortBy=top&apiKey=04e9f0afe05f427e9b2c03399acfacd5",};
    }

    private class Downloader extends AsyncTask<String, Void, ArrayList<Article>> {

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
            populateWidget();
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
