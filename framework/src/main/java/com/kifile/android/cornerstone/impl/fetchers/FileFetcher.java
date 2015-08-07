package com.kifile.android.cornerstone.impl.fetchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.kifile.android.cornerstone.core.DataFetcher;

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
    public InputStream fetch() {
        try {
            return new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
