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
                        try {
                            final DATA data = mFetcher.fetch();
                            // Always use handler to post the notify task.
                            sHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setPageData(page, data);
                                }
                            });
                        } catch (Exception e) {
                            handleException(e);
                        }
                    }
                }
            });
        } else {
            if (mFetcher != null) {
                try {
                    setPageData(page, mFetcher.fetch());
                } catch (Exception e) {
                    handleException(e);
                }
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

    @Override
    protected void handleException(Exception e) {
        super.handleException(e);
        if (mIsAsync) {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    setPageData(-1, null);
                }
            });
        } else {
            setPageData(-1, null);
        }
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
