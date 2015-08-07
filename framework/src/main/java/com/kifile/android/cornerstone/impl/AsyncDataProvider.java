package com.kifile.android.cornerstone.impl;

/**
 * Fetch Data on a no-ui thread.
 *
 * @author kifile
 */
public class AsyncDataProvider<DATA> extends AbstractDataProvider<DATA> {

    @Override
    public void refresh() {
        // Todo: When utils is built, use Thread Pool to execute it.
        new Thread() {

            @Override
            public void run() {
                super.run();
                setData(mFetcher.fetch());
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

    @Override
    public void recycle() {
        super.recycle();
    }
}
