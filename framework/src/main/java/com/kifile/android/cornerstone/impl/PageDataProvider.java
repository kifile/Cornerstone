package com.kifile.android.cornerstone.impl;

/**
 * Created by kifile on 15/8/25.
 */
public abstract class PageDataProvider<DATA> extends AbstractDataProvider<PageDataProvider.PageData<DATA>> {

    @Override
    public void refresh() {
        loadPage(0);
    }

    public abstract void loadPage(int page);

    @Override
    protected boolean isDataNeedUpdate() {
        PageData<DATA> page = getData();
        return page != null && page.page == 0;
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
