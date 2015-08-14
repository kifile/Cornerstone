package com.kifile.android.sample.cornerstone;

import android.app.Activity;
import android.os.Bundle;

import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.impl.GlobalDataProviderManager;

import org.json.JSONObject;

/**
 * The sample of using DataProvider in Activity.
 *
 * @author kifile
 */
public class SampleActivity extends Activity {

    private SampleDataProvider mProvider;

    private DataObserver<JSONObject> mObserver = new DataObserver<JSONObject>() {
        @Override
        public void onDataChanged(JSONObject jsonObject) {
            setTitle(jsonObject.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProvider == null) {
            mProvider =
                    (SampleDataProvider) GlobalDataProviderManager.getInstance().obtainProvider(SampleDataProvider.KEY);
        }
    }

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
        if (mProvider != null) {
            GlobalDataProviderManager.getInstance().releaseProvider(SampleDataProvider.KEY);
            mProvider = null;
        }
        super.onDestroy();
    }

}
