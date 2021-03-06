package com.kifile.android.cornerstone.impl.fetchers;

import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.FetchException;

/**
 * DataConverter is used to invoke the exist data in DataConverter.
 *
 * @author kifile
 */
public class DataConverter<DATA> implements DataFetcher<DATA> {

    private final DATA mData;

    public DataConverter(DATA data) {
        mData = data;
    }

    @Override
    public DATA fetch() throws FetchException, ConvertException {
        return mData;
    }
}
