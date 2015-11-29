package com.kifile.android.sample.cornerstone.data;

import com.kifile.android.cornerstone.impl.fetchers.HttpFetcher;
import com.kifile.android.cornerstone.impl.fetchers.InputStream2StringConverter;
import com.kifile.android.cornerstone.impl.providers.AbstractDataProvider;

/**
 * Created by kifile on 15/11/14.
 */
public class HttpSampleProvider extends AbstractDataProvider<String> {

    public static final String KEY = HttpSampleProvider.class.getName();

    public HttpSampleProvider() {
        HttpFetcher fetcher = new HttpFetcher(HttpFetcher.METHOD_GET, "https://baidu.com");
        setAsync(true);
        setFetcher(new InputStream2StringConverter(fetcher));
    }
}
