package com.kifile.android.cornerstone.core;

/**
 * DataObserver is called at {@link DataProvider#registerDataObserver
 * (DataObserver)}.
 *
 * @author kifile
 */
public interface DataObserver<DATA> {

    /**
     * Watch if the data of {@link DataProvider} is changed.
     *
     * @param data
     */
    void onDataChanged(DATA data);
}
