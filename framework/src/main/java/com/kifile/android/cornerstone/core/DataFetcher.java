package com.kifile.android.cornerstone.core;

/**
 * DataFetcher is the place we exactly get data.
 *
 * @author kifile
 */
public interface DataFetcher<DATA> {

    DATA fetch();
}
