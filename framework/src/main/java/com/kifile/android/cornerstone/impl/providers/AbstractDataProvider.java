package com.kifile.android.cornerstone.impl.providers;

import android.os.Handler;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.core.DataProvider;
import com.kifile.android.cornerstone.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The implement of {@link DataProvider}.
 *
 * @author kifile
 */
public abstract class AbstractDataProvider<DATA> implements DataProvider<DATA> {

    private final List<DataObserver<DATA>> mObservers = new ArrayList<>();

    private DATA mData;

    protected DataFetcher<DATA> mFetcher;

    // 由于数据加载需要通过异步线程调用，因此在此处创建一个静态的Handler，专门负责主线程通信。
    protected static Handler sHandler = new Handler();

    /**
     * When you extends AbstractDataProvider, don't make the constructor having any argument.
     * <p/>
     * Unless you want to control the provider by yourself like {@link CombinedDataProvider}
     * <p/>
     * This constructor will be called by {@link com.kifile.android.cornerstone.core.AbstractDataProviderManager}.
     */
    public AbstractDataProvider() {

    }

    @Override
    public void setFetcher(DataFetcher<DATA> fetcher) {
        mFetcher = fetcher;
    }

    @Override
    public synchronized void registerDataObserver(final DataObserver<DATA> observer) {
        checkMainThread();
        mObservers.add(observer);
        if (isDataNeedUpdate()) {
            refresh();
        } else {
            observer.onDataChanged(mData);
        }
    }

    @Override
    public void refresh() {
        if (mFetcher != null) {
            setData(mFetcher.fetch());
        }
    }

    @Override
    public synchronized void unregisterDataObserver(DataObserver observer) {
        checkMainThread();
        mObservers.remove(observer);
    }

    protected void setData(DATA data) {
        if (mData == null || !mData.equals(data) || isDataNeedUpdate()) {
            mData = data;
            notifyDataChanged();
        }

    }

    @Override
    public DATA getData() {
        return mData;
    }

    @Override
    public synchronized void notifyDataChanged() {
        checkMainThread();
        for (DataObserver<DATA> observer : mObservers) {
            observer.onDataChanged(mData);
        }
    }

    @Override
    public boolean isDataNeedUpdate() {
        return mData == null;
    }

    @Override
    public void release() {
        mData = null;
    }

    /**
     * Check if it is running on main thread, keep thread-safe.
     */
    protected final void checkMainThread() {
        if (!ThreadUtils.isMain()) {
            throw new RuntimeException("Should run on main thread.");
        }
    }
}
