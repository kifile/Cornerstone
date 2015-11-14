package com.kifile.android.sample.cornerstone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.impl.Cornerstone;
import com.kifile.android.sample.cornerstone.data.HttpSampleProvider;

public class HttpActivity extends AppCompatActivity {

    private HttpSampleProvider mProvider;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProvider = (HttpSampleProvider) Cornerstone.obtainProvider(HttpSampleProvider.KEY);
        setContentView(R.layout.activity_http);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private final DataObserver<String> mObserver = new DataObserver<String>() {
        @Override
        public void onDataChanged(String s) {
            Snackbar.make(fab, s, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mProvider.registerDataObserver(mObserver);
    }

    @Override
    protected void onPause() {
        mProvider.unregisterDataObserver(mObserver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Cornerstone.releaseProvider(HttpSampleProvider.KEY);
        super.onDestroy();
    }
}
