package com.kifile.android.cornerstone.core;

/**
 * AbstractFetcherConverter is used to transform a data to another type.
 *
 * @author kifile
 */
public abstract class AbstractFetcherConverter<FROM, TO> implements DataFetcher<TO>, ConvertErrorCallback {

    private DataFetcher<FROM> mProxy;

    private ConvertErrorCallback mErrorCallback;

    /**
     * Wrap the DataFetcher will be transformed.
     *
     * @param proxy
     */
    public AbstractFetcherConverter(DataFetcher<FROM> proxy) {
        mProxy = proxy;
        if (proxy instanceof AbstractFetcherConverter) {
            ((AbstractFetcherConverter) proxy).setErrorCallback(this);
        }
    }

    public AbstractFetcherConverter(DataFetcher<FROM> proxy, ConvertErrorCallback errorCallback) {
        this(proxy);
        setErrorCallback(errorCallback);
    }

    public void setErrorCallback(ConvertErrorCallback callback) {
        mErrorCallback = callback;
    }

    /**
     * 处理错误信息.
     * <p/>
     * 需要遵循以下原则:
     * <ul>
     * <li>1.handleError后返回null</li>
     * <li>2.仅对非null数据,转换出错时调用handleError,防止错误信息被反复调用.</li>
     * </ul>
     *
     * @param error 错误信息,传递给最上层处理者,判断错误类型.
     */
    public void handleError(Object error) {
        onConvertError(error);
    }

    @Override
    public TO fetch() {
        FROM data = mProxy.fetch();
        return convert(data);
    }

    protected abstract TO convert(FROM from);

    @Override
    public void onConvertError(Object error) {
        if (mErrorCallback != null) {
            mErrorCallback.onConvertError(error);
        }
    }
}
