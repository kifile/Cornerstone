package com.kifile.android.cornerstone.impl.fetchers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;

/**
 * Convert a InputStream to String.
 *
 * @author kifile
 */
public class InputStream2StringConverter extends AbstractFetcherConverter<InputStream, String> {

    public InputStream2StringConverter(DataFetcher<InputStream> proxy) {
        super(proxy);
    }

    @Override
    protected String convert(InputStream inputStream) {
        if (inputStream != null) {
            StringWriter writer;
            writer = new StringWriter();
            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                char[] buffer = new char[4 * 1024];
                int size;
                while ((size = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, size);
                }
                return writer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
