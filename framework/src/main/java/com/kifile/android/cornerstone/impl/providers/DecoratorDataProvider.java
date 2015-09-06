package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.core.DataProvider;

/**
 * Created by kifile on 15/9/6.
 */
public class DecoratorDataProvider<DATA> extends AbstractDataProvider<DATA> {

    private DataProvider<DATA> mDecor;

    public DecoratorDataProvider(DataProvider<DATA> decor) {
        mDecor = decor;
    }

    @Override
    public void setFetcher(DataFetcher<DATA> fetcher) {
        mDecor.setFetcher(fetcher);
    }

    @Override
    public void refresh() {
        mDecor.refresh();
    }

    @Override
    public synchronized void registerDataObserver(DataObserver<DATA> observer) {
        mDecor.registerDataObserver(observer);
    }

    @Override
    public synchronized void unregisterDataObserver(DataObserver observer) {
        mDecor.unregisterDataObserver(observer);
    }

    @Override
    public DATA getData() {
        return mDecor.getData();
    }

    @Override
    public void notifyDataChanged() {
        mDecor.notifyDataChanged();
    }

    @Override
    public boolean isDataNeedUpdate() {
        return mDecor.isDataNeedUpdate();
    }

    @Override
    public void release() {
        super.release();
    }

}
