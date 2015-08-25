package com.kifile.android.cornerstone.impl;

import com.kifile.android.cornerstone.core.AbstractDataProviderManager;

/**
 * Cornerstone is a simple implement of {@link AbstractDataProviderManager}.
 *
 * @author kifile
 */
public class Cornerstone extends AbstractDataProviderManager {

    private static Cornerstone sInstance;

    private Cornerstone() {
        // Make Cornerstone singleton.
    }

    /**
     * Use singleton here.
     *
     * @return
     */
    public static Cornerstone getInstance() {
        if (sInstance == null) {
            synchronized (Cornerstone.class) {
                if (sInstance == null) {
                    sInstance = new Cornerstone();
                }
            }
        }
        return sInstance;
    }
}
