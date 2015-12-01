package com.kifile.android.cornerstone.core;

/**
 * ConvertException means you cannot convert the data to another type.
 *
 * @author kifile
 */
public class ConvertException extends Exception {

    private Object o;

    public ConvertException() {
    }

    public ConvertException(String detailMessage) {
        super(detailMessage);
    }

    public ConvertException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ConvertException(Throwable throwable) {
        super(throwable);
    }

    public void setError(Object error) {
        o = error;
    }

    public Object getError() {
        return o;
    }
}
