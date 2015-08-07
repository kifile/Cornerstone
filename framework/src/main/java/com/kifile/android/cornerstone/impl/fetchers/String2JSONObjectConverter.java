package com.kifile.android.cornerstone.impl.fetchers;

import org.json.JSONException;
import org.json.JSONObject;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;

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
    protected JSONObject convert(String s) {
        JSONObject object = null;
        if (s != null) {
            try {
                object = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
