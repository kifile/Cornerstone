package com.kifile.android.cornerstone.core;

/**
 * DataFetcher is the place we exactly get data.
 *
 * @author kifile
 */
public interface DataFetcher<DATA> {

    /**
     * Fetch data.
     *
     * @return If fetch successful, return the data.
     * @throws FetchException   Throw FetchException when you cannot fetch a data.
     * @throws ConvertException Throw ConvertException when you cannot convert the data to another type.
     */
    DATA fetch() throws FetchException, ConvertException;
}
