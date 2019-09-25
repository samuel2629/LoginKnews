package com.silhocompany.ideo.knews.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Fragments.PopUp.AlertDialogFragment;

public class LauncherActivity extends MainActivity {

    protected RelativeLayout mRelativeLayout;
    protected ImageView mImageView;
    private static final String DIALOG_FRAGMENT_NO_NETWORK = "DIALOG_FRAGMENT_NO_NETWORK";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mRelativeLayout = findViewById(R.id.relativeLayoutMain);
        mImageView = findViewById(R.id.logoImageView);

        animate();
        if (!isNetworkAvailable()) {alertUserAboutError();}
        else {mRelativeLayout.postDelayed(() -> {
            mRelativeLayout.setVisibility(View.GONE);
            getActualNewsActivity(this);
        },2200);}
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    protected void alertUserAboutError() {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_NO_NETWORK);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    protected void animate(){
        ObjectAnimator imageAnim = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f);
        imageAnim.setInterpolator(new AnticipateOvershootInterpolator());
        imageAnim.setStartDelay(700);
        AnimatorSet set = new AnimatorSet().setDuration(1500);
        set.play(imageAnim);
        set.start();
    }

}
