package com.kifile.android.cornerstone.impl.fetchers;

import android.annotation.NonNull;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Convert String to JSONObject.
 *
 * @author kifile
 */
public class String2JSONObjectConverter extends AbstractFetcherConverter<String, JSONObject> {

    public String2JSONObjectConverter(DataFetcher<String> proxy) {
        super(proxy);
    }

    @Override
    protected JSONObject convert(@NonNull String s) throws ConvertException {
        try {
            return new JSONObject(s);
        } catch (JSONException e) {
            throw new ConvertException(e);
        }
    }
}
