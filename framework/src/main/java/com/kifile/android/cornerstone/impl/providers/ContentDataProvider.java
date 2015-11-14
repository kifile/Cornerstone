package com.kifile.android.cornerstone.impl.providers;

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

    private final Context mAppContext;

    private CursorFetcher mCursorFetcher;

    private ContentObserver mContentObserver;

    public ContentDataProvider(Context context, Uri uri) {
        mAppContext = context.getApplicationContext();
        mUri = uri;
    }

    public DataFetcher<Cursor> buildCursorFetcher() {
        if (mCursorFetcher == null) {
            mCursorFetcher = new CursorFetcher(mAppContext.getContentResolver(), mUri);
        } else {
            mCursorFetcher.clear();
        }
        return mCursorFetcher;
    }

    public void setProjection(String[] projections) {
        mCursorFetcher.setProjection(projections);
    }

    public void setSelection(String selection, String[] selectionArgs) {
        mCursorFetcher.setSelection(selection, selectionArgs);
    }

    public void setOrderBy(String orderBy) {
        mCursorFetcher.setOrderBy(orderBy);
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
                    mAppContext.getContentResolver().registerContentObserver(mUri, true, mContentObserver);
                }
            }
        }
        super.refresh();
    }

    @Override
    public void release() {
        if (mContentObserver != null) {
            mAppContext.getContentResolver().unregisterContentObserver(mContentObserver);
            mContentObserver = null;
        }
        super.release();
    }
}
