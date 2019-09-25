package com.silhocompany.ideo.knews.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.silhocompany.ideo.knews.R;


public class LicenceActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);

        mTextView = (TextView) findViewById(R.id.licenceTextView);
        mTextView.setText(R.string.licence_message);
    }
}
