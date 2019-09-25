package com.silhocompany.ideo.knews.Fragments.PastNews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Fragments.PopUp.AlertDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OldFactsFragment extends Fragment {

    private TextView mTextView;
    private TextView mTitleTextView;
    private ImageView mImageView;
    private ImageView mDateLine;

    private String mFact;
    private String mJsonFirst;
    private String mJsonSecond;
    private String mDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.old_day_fact_layout, container, false);

        setHasOptionsMenu(true);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        mTextView = (TextView) view.findViewById(R.id.factPastTextView);
        mTitleTextView = (TextView) view.findViewById(R.id.textView2);
        mDateLine = (ImageView) view.findViewById(R.id.dateLine);

        getFact();

        mImageView.setOnClickListener(view1 -> getFact());

        return view;
    }

    public void getFact() {
        DownloadFact downloadFact = new DownloadFact();
        downloadFact.execute(getURL());
    }

    public String getFactShare(){
        return mFact;
    }

    public String getDateShare() {
        return mDate;
    }


    @NonNull
    private String getURL() {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        return "https://numbersapi.p.mashape.com/" + month
                + "/" + day +"/date?fragment=true&json=true";
    }


    private class DownloadFact extends AsyncTask<String, Integer, String> {

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

        private void alertUserAboutError() {
            AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
            alertDialogFragment.show(getActivity().getFragmentManager(), "error_dialog");
        }

        @Override
        protected void onPreExecute() {
            if(!isNetworkAvailable()){
                alertUserAboutError();}
            if (mJsonFirst != null) {
                mJsonSecond = mJsonFirst;
            }
            animate(Integer.MAX_VALUE, 800.00f, -800.00f);
        }

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(strings[0]).
                    addHeader("X-Mashape-Key", "i8AehBhoADmshaWycNwg4WlRc2UEp16CGZjjsnCupE0nbl6KLV")
                    .build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {

                        return response.body().string();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return getString(R.string.failed_download);
            }

        @Override
        protected void onPostExecute(String s) {
            try {
                animate(0,0,0);
                populate(s);

                mJsonFirst = mFact;

                if(mJsonSecond != null && mJsonFirst.equals(mJsonSecond)) getFact();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void animate(int duration, float x, float y){
            TranslateAnimation translateAnimation = new TranslateAnimation(x, y, 0.0f, 0.0f);
            translateAnimation.setDuration(1500);
            translateAnimation.setRepeatCount(duration);
            translateAnimation.setInterpolator(new CycleInterpolator(1));
            mDateLine.startAnimation(translateAnimation);

            mImageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_click));
            postponeEnterTransition();
        }

        private void populate(String jsonData) throws JSONException {
            JSONObject mJSONObject = new JSONObject(jsonData);
            mFact = mJSONObject.getString("text");
            mDate = mJSONObject.getString("year");
            String upperFact = mFact.substring(0, 1).toUpperCase() + mFact.substring(1);
            mTextView.setText(upperFact);
            mTitleTextView.setText(String.format("%s%s", getString(R.string.in), mDate));
            startPostponedEnterTransition();
        }
    }
}

