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

    public CursorFetcher(ContentResolver resolver, Uri uri, String[] projections) {
        mResolver = resolver;
        mUri = uri;
        mProjections = projections;
    }

    @Override
    public Cursor fetch() {
        return mResolver.query(mUri, mProjections, null, null, null);
    }
}
