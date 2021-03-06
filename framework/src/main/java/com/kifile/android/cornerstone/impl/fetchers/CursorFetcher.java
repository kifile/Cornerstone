package com.kifile.android.cornerstone.impl.fetchers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.FetchException;

/**
 * Fetch the cursor data from content provider.
 * <p/>
 * Do not forget close the cursor when not use.
 *
 * @author kifile
 */
public class CursorFetcher implements DataFetcher<Cursor> {

    private ContentResolver mResolver;

    private Uri mUri;

    private String[] mProjections;

    private String mSelection;

    private String[] mSelectionArgs;

    private String mOrderBy;

    public CursorFetcher(ContentResolver resolver, Uri uri) {
        mResolver = resolver;
        mUri = uri;
    }

    public CursorFetcher setProjection(String[] projections) {
        mProjections = projections;
        return this;
    }

    public CursorFetcher setSelection(String selection, String[] selectionArgs) {
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        return this;
    }

    public CursorFetcher setOrderBy(String orderBy) {
        mOrderBy = orderBy;
        return this;
    }

    /**
     * If you want to change the params of fetcher, call it to reset the old params.
     *
     * @return
     */
    public CursorFetcher clear() {
        mProjections = null;
        mSelection = null;
        mSelectionArgs = null;
        mOrderBy = null;
        return this;
    }

    @Override
    public Cursor fetch() throws FetchException, ConvertException {
        return mResolver.query(mUri, mProjections, mSelection, mSelectionArgs, mOrderBy);
    }
}
