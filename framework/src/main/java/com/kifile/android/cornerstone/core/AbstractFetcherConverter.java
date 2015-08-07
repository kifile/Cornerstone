package com.kifile.android.cornerstone.core;

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
    public TO fetch() {
        FROM data = mProxy.fetch();
        return convert(data);
    }

    protected abstract TO convert(FROM from);
}
