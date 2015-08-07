package com.kifile.android.cornerstone.impl.fetchers;

import java.io.IOException;
import java.io.InputStream;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Convert a InputStream to Bitmap.
 *
 * @author kifile
 */
public class InputStream2BitmapConverter extends AbstractFetcherConverter<InputStream, Bitmap> {

    public InputStream2BitmapConverter(DataFetcher<InputStream> proxy) {
        super(proxy);
    }

    @Override
    protected Bitmap convert(InputStream inputStream) {
        if (inputStream != null) {
            try {
                return BitmapFactory.decodeStream(inputStream);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
