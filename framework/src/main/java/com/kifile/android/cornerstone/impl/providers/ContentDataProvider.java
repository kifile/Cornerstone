package com.kifile.android.cornerstone.impl.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.fetchers.CursorFetcher;

/**
 * ContentProvider数据获取基类,加入对数据变化的监听处理.
 *
 * @author kifile
 */
public class ContentDataProvider<DATA> extends AbstractDataProvider<DATA> {

    private final Uri mUri;

    private CursorFetcher mCursorFetcher;

    private ContentObserver mContentObserver;

    private final ContentResolver mContentResolver;

    public ContentDataProvider(Context context, Uri uri) {
        mContentResolver = context.getApplicationContext().getContentResolver();
        mUri = uri;
    }

    public DataFetcher<Cursor> buildCursorFetcher() {
        if (mCursorFetcher == null) {
            mCursorFetcher = new CursorFetcher(mContentResolver, mUri);
        } else {
            mCursorFetcher.clear();
        }
        return mCursorFetcher;
    }

    public void setProjection(String[] projections) {
        checkCursorFetcherExist();
        mCursorFetcher.setProjection(projections);
    }

    public void setSelection(String selection, String[] selectionArgs) {
        checkCursorFetcherExist();
        mCursorFetcher.setSelection(selection, selectionArgs);
    }

    public void setOrderBy(String orderBy) {
        checkCursorFetcherExist();
        mCursorFetcher.setOrderBy(orderBy);
    }


    private void checkCursorFetcherExist() {
        if (mCursorFetcher == null) {
            throw new RuntimeException(new IllegalAccessError("Should call buildCursorFetcher before."));
        }
    }

    @Override
    public void refresh() {
        if (mContentObserver == null) {
            synchronized (ContentDataProvider.class) {
                if (mContentObserver == null) {
                    mContentObserver = new ContentObserver(sHandler) {
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            refresh();
                        }
                    };
                    mContentResolver.registerContentObserver(mUri, true, mContentObserver);
                }
            }
        }
        super.refresh();
    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }

    @Override
    public void release() {
        if (mContentObserver != null) {
            mContentResolver.unregisterContentObserver(mContentObserver);
            mContentObserver = null;
        }
        super.release();
    }
}
