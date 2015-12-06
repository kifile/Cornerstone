package com.kifile.android.cornerstone.core;


import android.support.annotation.NonNull;

/**
 * AbstractFetcherConverter is used to transform a data to another type.
 *
 * @author kifile
 */
public abstract class AbstractFetcherConverter<FROM, TO> implements DataFetcher<TO> {

    private DataFetcher<FROM> mProxy;

    /**
     * Wrap the DataFetcher will be transformed.
     *
     * @param proxy
     */
    public AbstractFetcherConverter(DataFetcher<FROM> proxy) {
        mProxy = proxy;
    }

    @Override
    public TO fetch() throws FetchException, ConvertException {
        FROM data = mProxy.fetch();
        if (data == null) {
            throw new FetchException("Proxy return null.");
        }
        return convert(data);
    }

    protected abstract TO convert(@NonNull FROM from) throws ConvertException;

}
