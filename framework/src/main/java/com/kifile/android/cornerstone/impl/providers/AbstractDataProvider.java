package com.kifile.android.cornerstone.impl.providers;

import android.annotation.NonNull;
import android.os.Handler;
import android.os.Looper;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.core.DataProvider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The implement of {@link DataProvider}.
 *
 * @author kifile
 */
public abstract class AbstractDataProvider<DATA> implements DataProvider<DATA> {

    private static Executor EXECUTOR;

    private final List<DataObserver<DATA>> mObservers = new CopyOnWriteArrayList<>();

    private DATA mData;

    protected DataFetcher<DATA> mFetcher;

    protected boolean mIsAsync;

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

    public static void setDefaultExecutor(@NonNull Executor executor) {
        EXECUTOR = executor;
    }

    /**
     * Set if the fetch task run on a non-main thread.
     *
     * @param async
     */
    public void setAsync(boolean async) {
        mIsAsync = async;
    }

    @Override
    public void setFetcher(DataFetcher<DATA> fetcher) {
        mFetcher = fetcher;
    }

    @Override
    public void registerDataObserver(final DataObserver<DATA> observer) {
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
        if (mIsAsync) {
            executeFetchTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        setData(mFetcher.fetch());
                    } catch (Exception e) {
                        handleException(e);
                    }
                }
            });
        } else {
            try {
                setData(mFetcher.fetch());
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    protected final void executeFetchTask(Runnable runnable) {
        if (EXECUTOR == null) {
            synchronized (AbstractDataProvider.class) {
                if (EXECUTOR == null) {
                    EXECUTOR = Executors.newCachedThreadPool();
                }
            }
        }
        EXECUTOR.execute(runnable);
    }

    @Override
    public void unregisterDataObserver(DataObserver observer) {
        checkMainThread();
        mObservers.remove(observer);
    }

    protected void setData(final DATA data) {
        // Always use handler to post the notify task.
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mData == null || !mData.equals(data) || isDataNeedUpdate()) {
                    mData = data;
                    notifyDataChanged();
                }
            }
        });
    }

    @Override
    public DATA getData() {
        return mData;
    }

    @Override
    public void notifyDataChanged() {
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

    protected void handleException(Exception e) {
        e.printStackTrace();
    }

    /**
     * Check if it is running on main thread, keep thread-safe.
     */
    protected final void checkMainThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new RuntimeException("Should run on main thread.");
        }
    }
}
