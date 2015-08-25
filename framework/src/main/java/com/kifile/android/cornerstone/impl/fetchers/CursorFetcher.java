package com.kifile.android.cornerstone.impl.fetchers;

import com.kifile.android.cornerstone.core.DataFetcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Fetch the cursor data from content provider.
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

    @Override
    public Cursor fetch() {
        return mResolver.query(mUri, mProjections, mSelection, mSelectionArgs, mOrderBy);
    }
}
