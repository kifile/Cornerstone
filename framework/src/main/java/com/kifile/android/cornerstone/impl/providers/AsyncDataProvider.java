package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.core.DataProvider;
import com.kifile.android.cornerstone.utils.WorkerThreadPool;

/**
 * Fetch Data on a no-ui thread.
 *
 * @author kifile
 */
public class AsyncDataProvider<DATA> extends DecoratorDataProvider<DATA> {

    private AsyncDataProvider(DataProvider<DATA> proxy) {
        super(proxy);
    }

    private WorkerThreadPool.WorkerRunnable mWorker;

    @Override
    public void refresh() {
        if (mWorker != null) {
            mWorker.cancel();
        }
        mWorker = WorkerThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                AsyncDataProvider.super.refresh();
            }
        });
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
