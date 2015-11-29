package com.kifile.android.cornerstone.core;

/**
 * 数据转换错误处理接口.
 *
 * @author kifile
 */
public interface ConvertErrorCallback {

    /**
     * 数据转换错误回调接口,一般在非主线程调用.
     *
     * @param error
     */
    void onConvertError(Object error);
}
