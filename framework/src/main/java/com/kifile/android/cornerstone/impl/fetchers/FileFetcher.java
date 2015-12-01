package com.kifile.android.cornerstone.impl.fetchers;

import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.FetchException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Fetch the InputStream of a file.
 *
 * @author kifile
 */
public class FileFetcher implements DataFetcher<InputStream> {

    private File mFile;

    public FileFetcher(String path) {
        mFile = new File(path);
    }

    public FileFetcher(File file) {
        mFile = file;
    }

    @Override
    public InputStream fetch() throws FetchException, ConvertException {
        try {
            return new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            throw new FetchException(e);
        }
    }
}
