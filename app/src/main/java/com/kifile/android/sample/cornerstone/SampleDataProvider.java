package com.kifile.android.sample.cornerstone;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.AbstractDataProvider;
import com.kifile.android.cornerstone.impl.fetchers.String2JSONObjectConverter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The sample of DataProvider.
 *
 * @author kifile
 */
public class SampleDataProvider extends AbstractDataProvider<JSONObject> {

    public static final String KEY = SampleDataProvider.class.getName();

    public SampleDataProvider() {
        /**
         * {@link String2JSONObjectConverter} is a inner class to convert String to JSONObject.
         */
        DataFetcher<JSONObject> fetcher = new String2JSONObjectConverter(new DataFetcher<String>() {
            @Override
            public String fetch() {
                // Just create a stander JSONObject String.
                JSONObject object = new JSONObject();
                try {
                    object.put("Test", "test");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return object.toString();
            }
        });
        setFetcher(fetcher);
    }
}
