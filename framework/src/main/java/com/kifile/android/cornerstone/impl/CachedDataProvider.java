package com.kifile.android.cornerstone.impl;

import android.util.Log;

import com.kifile.android.cornerstone.BuildConfig;

/**
 * @author kifile
 */
public class CachedDataProvider<DATA> extends AbstractDataProvider<DATA> {

    private static final String TAG = CachedDataProvider.class.getSimpleName();

    private static final long DEFAULT_CACHE_TIME = 5L * 60 * 1000;

    private long mCacheTime = DEFAULT_CACHE_TIME;

    private boolean mCached;

    public void setCacheTime(long time) {
        mCacheTime = DEFAULT_CACHE_TIME;
    }

    public void tryCache(final String key) {
        if (!mCached) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "cache data");
            }
            mCached = true;
            Cornerstone.obtainProvider(key);
            sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Cornerstone.releaseProvider(key);
                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "uncache data");
                    }
                    mCached = false;
                }
            }, mCacheTime);
        } else {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "been cached");
            }
        }
    }
}
