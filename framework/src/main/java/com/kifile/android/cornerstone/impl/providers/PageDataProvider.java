package com.kifile.android.cornerstone.impl.providers;

/**
 * Paged data provider.
 * <p/>
 * Created by kifile on 15/8/25.
 */
public abstract class PageDataProvider<DATA> extends AbstractDataProvider<PageDataProvider.PageData<DATA>> {

    @Override
    public void refresh() {
        loadPage(0);
    }

    public abstract void loadPage(int page);

    @Override
    public boolean isDataNeedUpdate() {
        PageData<DATA> page = getData();
        return page != null && page.page == 0;
    }

    @Override
    protected synchronized void setData(PageData<DATA> dataPageData) {
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
