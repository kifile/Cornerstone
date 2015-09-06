package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.core.DataProvider;

/**
 * Fetch Data on a no-ui thread.
 *
 * @author kifile
 */
public abstract class AsyncDataProvider<DATA> extends DecoratorDataProvider<DATA> {

    private AsyncDataProvider(DataProvider<DATA> proxy) {
        super(proxy);
    }

    @Override
    public void refresh() {
        // Todo: When utils is built, use Thread Pool to execute it.
        new Thread() {

            @Override
            public void run() {
                super.run();
                AsyncDataProvider.super.refresh();
            }
        }.start();
    }

    @Override
    protected synchronized void setData(final DATA data) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                setDataOnUiThread(data);
            }
        });
    }

    protected synchronized void setDataOnUiThread(DATA data) {
        super.setData(data);
    }

}
