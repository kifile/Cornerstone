package com.kifile.android.cornerstone.impl.fetchers;

import org.json.JSONArray;
import org.json.JSONException;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;

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
    protected JSONArray convert(String s) {
        JSONArray array = null;
        if (s != null) {
            try {
                array = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }
}
