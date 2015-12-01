package com.kifile.android.cornerstone.impl.providers;

import com.kifile.android.cornerstone.core.DataFetcher;

/**
 * Paged data provider.
 * <p/>
 * Created by kifile on 15/8/25.
 */
public abstract class PageDataProvider<DATA> extends AbstractDataProvider<PageDataProvider.PageData<DATA>> {

    private DataFetcher<DATA> mFetcher;

    @Override
    public void setFetcher(DataFetcher<PageData<DATA>> fetcher) {
        throw new UnsupportedOperationException("Should call #setPageFetcher to setFetcher");
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
            executeFetchTask(new Runnable() {
                @Override
                public void run() {
                    if (mFetcher != null) {
                        setPageData(page, mFetcher.fetch());
                    }
                }
            });
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
        throw new UnsupportedOperationException("Should call #setPageData to setData");
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
