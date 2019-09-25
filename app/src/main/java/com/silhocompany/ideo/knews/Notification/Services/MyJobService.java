package com.silhocompany.ideo.knews.Notification.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.silhocompany.ideo.knews.Notification.NotifcationsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Samuel on 29/05/2017.
 */

public class MyJobService extends JobService {

    private AsyncTask mAsyncTask;

    @Override
    public boolean onStartJob(JobParameters job) {
        mAsyncTask = new AsyncTask() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected Object doInBackground(Object[] objects) {
                getFactAndNotification();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mAsyncTask != null) mAsyncTask.cancel(true);
        return true;
    }

    private void getFactAndNotification(){
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(getURL()).
                addHeader("X-Mashape-Key", "i8AehBhoADmshaWycNwg4WlRc2UEp16CGZjjsnCupE0nbl6KLV")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        JSONObject jsonData = new JSONObject(body);
                        String fact = jsonData.getString("text");
                        String date = jsonData.getString("year");
                        Context context = MyJobService.this;
                        NotifcationsUtils.reminderFact(context, fact, date);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
        });
    }

    @NonNull
    private String getURL() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        return "https://numbersapi.p.mashape.com/" + month
                + "/" + day +"/date?fragment=true&json=true";
    }
}
