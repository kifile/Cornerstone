package com.kifile.android.cornerstone.core;

import com.kifile.android.cornerstone.impl.providers.AbstractDataProvider;

/**
 * The interface of {@link AbstractDataProvider}
 *
 * @author kifile
 */
public interface DataProvider<DATA> {

    void setFetcher(DataFetcher<DATA> fetcher);

    /**
     * Add the observer into watch list, when data changed, notify it.
     *
     * @param observer
     */
    void registerDataObserver(DataObserver<DATA> observer);

    /**
     * Remove observer from watch list.
     *
     * @param observer
     */
    void unregisterDataObserver(DataObserver<DATA> observer);

    /**
     * Refresh data.
     */
    void refresh();

    /**
     * Notify observers data changed.
     */
    void notifyDataChanged();

    DATA getData();

    /**
     * @return If the data need update, return true.
     */
    boolean isDataNeedUpdate();

    /**
     * Release the resources when destroy. If the worker thread is working, stop it.
     */
    void release();
}
