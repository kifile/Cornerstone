package com.kifile.android.cornerstone.impl.fetchers;

import android.support.annotation.NonNull;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Convert String to JSONArray.
 *
 * @author kifile
 */
public class String2JSONArrayConverter extends AbstractFetcherConverter<String, JSONArray> {

    public String2JSONArrayConverter(DataFetcher<String> proxy) {
        super(proxy);
    }

    @Override
    protected JSONArray convert(@NonNull String s) throws ConvertException {
        try {
            return new JSONArray(s);
        } catch (JSONException e) {
            throw new ConvertException(e);
        }
    }
}
