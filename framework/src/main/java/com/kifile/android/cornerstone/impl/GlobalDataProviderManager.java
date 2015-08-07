package com.kifile.android.cornerstone.impl;

import com.kifile.android.cornerstone.core.AbstractDataProviderManager;

/**
 * GlobalDataProviderManager is a simple implement of {@link AbstractDataProviderManager}.
 *
 * @author kifile
 */
public class GlobalDataProviderManager extends AbstractDataProviderManager {

    private static GlobalDataProviderManager sInstance;

    private GlobalDataProviderManager() {
        // Make GlobalDataProviderManager singleton.
    }

    /**
     * Use singleton here.
     *
     * @return
     */
    public static GlobalDataProviderManager getInstance() {
        if (sInstance == null) {
            synchronized (GlobalDataProviderManager.class) {
                if (sInstance == null) {
                    sInstance = new GlobalDataProviderManager();
                }
            }
        }
        return sInstance;
    }
}
