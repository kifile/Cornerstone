package com.kifile.android.cornerstone.core;

/**
 * The interface of {@link com.kifile.android.cornerstone.impl.AbstractDataProvider}
 *
 * @author kifile
 */
public interface DataProvider<DATA> {

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
     * Release the resources when destroy. If the worker thread is working, stop it.
     */
    void release();
}
