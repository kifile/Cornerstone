package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.utils.WorkerThreadPool;

/**
 * Paged data provider.
 * <p/>
 * Created by kifile on 15/8/25.
 */
public abstract class PageDataProvider<DATA> extends AbstractDataProvider<PageDataProvider.PageData<DATA>> {

    private DataFetcher<DATA> mFetcher;

    @Override
    public void setFetcher(DataFetcher<PageData<DATA>> fetcher) {
        throw new RuntimeException("Should call #setPageFetcher to setFetcher");
    }

    public void setPageFetcher(DataFetcher<DATA> fetcher) {
        mFetcher = fetcher;
    }

    /**
     * When refresh, load the page at 0.
     */
    @Override
    public final void refresh() {
        loadPage(0);
    }

    public void loadPage(final int page) {
        if (mIsAsync) {
            cancelAsyncTask();
            mWorker = WorkerThreadPool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    if (mFetcher != null) {
                        setPageData(page, mFetcher.fetch());
                    }
                }
            }, true);
        } else {
            if (mFetcher != null) {
                setPageData(page, mFetcher.fetch());
            }
        }
    }

    @Override
    public boolean isDataNeedUpdate() {
        PageData<DATA> page = getData();
        return page == null || page.page != 0;
    }

    @Override
    protected void setData(PageData<DATA> dataPageData) {
        throw new RuntimeException("Should call #setPageData to setData");
    }

    public void setPageData(int page, DATA data) {
        super.setData(new PageData<>(page, data));
    }

    public static class PageData<DATA> {

        public int page;

        public DATA data;

        public PageData(int page, DATA data) {
            this.page = page;
            this.data = data;
        }
    }
}
