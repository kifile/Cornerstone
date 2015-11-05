package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.utils.WorkerThreadPool;

import java.util.concurrent.Executor;

/**
 * Fetch Data on a no-ui thread.
 *
 * @author kifile
 */
public class AsyncDataProvider<DATA> extends AbstractDataProvider<DATA> {

    private Executor mExecutor;

    public AsyncDataProvider() {
    }

    public AsyncDataProvider(Executor executor) {
        mExecutor = executor;
    }

    private WorkerThreadPool.WorkerRunnable mWorker;

    private final Runnable mSuperRefresh = new Runnable() {
        @Override
        public void run() {
            AsyncDataProvider.super.refresh();
        }
    };

    @Override
    public void refresh() {
        if (mExecutor != null) {
            mExecutor.execute(mSuperRefresh);
        } else {
            if (mWorker != null) {
                mWorker.cancel();
            }
            mWorker = WorkerThreadPool.getInstance().execute(mSuperRefresh, true);
        }
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
