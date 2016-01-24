package com.kifile.android.cornerstone.widget;

import android.support.v7.widget.RecyclerView;

import com.kifile.android.cornerstone.impl.providers.PageDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kifile
 */
public abstract class PageAdapter<VH extends
        RecyclerView.ViewHolder, DATA> extends RecyclerView.Adapter<VH> {

    private final List<DATA> mData = new ArrayList<>();

    private int mPage;

    @Override
    public final int getItemCount() {
        return mData.size();
    }

    public DATA getItem(int position) {
        return mData.get(position);
    }

    public final void appendData(PageDataProvider.PageData<List<DATA>> pageData) {
        if (pageData != null) {
            if (pageData.page == 0) {
                mPage = 0;
                mData.clear();
                mData.addAll(pageData.data);
                notifyDataSetChanged();
            } else if (pageData.page == mPage + 1 && pageData.data != null) {
                mPage = pageData.page;
                final int oldSize = mData.size();
                mData.addAll(pageData.data);
                notifyItemRangeInserted(oldSize, pageData.data.size());
            }
        }
    }

    public int getCurrentPage() {
        return mPage;
    }
}
