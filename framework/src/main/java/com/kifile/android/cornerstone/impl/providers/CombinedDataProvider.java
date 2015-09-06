package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.core.DataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kifile
 */
public abstract class CombinedDataProvider extends AbstractDataProvider {

    private final Map<String, DataProvider> mCombinedProviders = new HashMap<>();

    /**
     * It should only be called in your constructor.
     *
     * @param key
     * @param provider
     */
    protected void put(String key, DataProvider provider) {
        if (mCombinedProviders.containsKey(key)) {
            throw new IllegalArgumentException("The key[" + key + "] has been registered.");
        }
        mCombinedProviders.put(key, provider);
    }

    public DataProvider getProvider(String key) {
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
        for (DataProvider provider : mCombinedProviders.values()) {
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
        DataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.refresh();
    }

    @Override
    public void notifyDataChanged() {
        for (DataProvider provider : mCombinedProviders.values()) {
            if (provider.isDataNeedUpdate()) {
                provider.notifyDataChanged();
            }
        }
    }

    public void notifyDataChanged(String key) {
        DataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.notifyDataChanged();
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
        DataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.registerDataObserver(observer);
    }

    public void unregisterDataObserver(String key, DataObserver observer) {
        DataProvider provider = getProvider(key);
        if (provider == null) {
            throw new IllegalArgumentException();
        }
        provider.unregisterDataObserver(observer);
    }

    @Override
    public void release() {
        super.release();
        for (DataProvider provider : mCombinedProviders.values()) {
            provider.release();
        }
        mCombinedProviders.clear();
    }

}
