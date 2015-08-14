package com.kifile.android.cornerstone.impl;

import com.kifile.android.cornerstone.core.DataObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kifile
 */
public abstract class CombinedDataProvider extends AbstractDataProvider {

    private final Map<String, AbstractDataProvider> mCombinedProviders = new HashMap<>();

    /**
     * It should only be called in your constructor.
     *
     * @param key
     * @param provider
     */
    protected void put(String key, AbstractDataProvider provider) {
        mCombinedProviders.put(key, provider);
    }

    protected AbstractDataProvider getProvider(String key) {
        return mCombinedProviders.get(key);
    }

    /**
     * When asked refresh, always notify {@link #mCombinedProviders} to refresh.
     * <p/>
     * Otherwise, it won't call {@link #setData(Object)}, so {@link #isDataNeedUpdate()}
     * always return true.
     */
    @Override
    public void refresh() {
        for (AbstractDataProvider provider : mCombinedProviders.values()) {
            if (provider.isDataNeedUpdate()) {
                provider.refresh();
            }
        }
    }

    /**
     * Refresh the target provider.
     *
     * @param key
     */
    public void refresh(String key) {
        AbstractDataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.refresh();
    }

    @Override
    public void registerDataObserver(DataObserver observer) {
        throw new UnsupportedOperationException("Cannot register observer in " + getClass().getName());
    }

    @Override
    public void unregisterDataObserver(DataObserver observer) {
        throw new UnsupportedOperationException("Cannot register observer in " + getClass().getName());
    }

    public void registerDataObserver(String key, DataObserver observer) {
        AbstractDataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.registerDataObserver(observer);
    }

    public void unregisterDataObserver(String key, DataObserver observer) {
        AbstractDataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.unregisterDataObserver(observer);
    }

    @Override
    public void release() {
        super.release();
        for (AbstractDataProvider provider : mCombinedProviders.values()) {
            provider.release();
        }
        mCombinedProviders.clear();
    }

}
