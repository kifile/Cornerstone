package com.kifile.android.cornerstone.impl;

import java.util.ArrayList;
import java.util.List;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.core.DataProvider;

import android.os.Handler;

/**
 * The implement of {@link DataProvider}.
 *
 * @author kifile
 */
public abstract class AbstractDataProvider<DATA> implements DataProvider<DATA> {

    private final List<DataObserver> mObservers = new ArrayList<>();

    private DATA mData;

    protected DataFetcher<DATA> mFetcher;

    // 由于数据加载需要通过异步线程调用，因此在此处创建一个静态的Handler，专门负责主线程通信。
    protected static Handler sHandler = new Handler();

    public void setFetcher(DataFetcher<DATA> fetcher) {
        mFetcher = fetcher;
    }

    @Override
    public synchronized void registerDataObserver(final DataObserver<DATA> observer) {
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
        mObservers.remove(observer);
    }

    protected synchronized void setData(DATA data) {
        if (mData == null || !mData.equals(data)) {
            mData = data;
            notifyDataChanged();
        }

    }

    private void notifyDataChanged() {
        for (DataObserver observer : mObservers) {
            observer.onDataChanged(mData);
        }
    }

    protected boolean isDataNeedUpdate() {
        return mData == null;
    }

    @Override
    public void recycle() {

    }
}
