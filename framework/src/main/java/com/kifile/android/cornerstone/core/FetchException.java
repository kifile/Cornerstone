package com.kifile.android.cornerstone.core;

/**
 * FetchException means no data fetched.
 *
 * @author kifile
 */
public class FetchException extends Exception {

    public FetchException() {
    }

    public FetchException(String detailMessage) {
        super(detailMessage);
    }

    public FetchException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FetchException(Throwable throwable) {
        super(throwable);
    }
}
