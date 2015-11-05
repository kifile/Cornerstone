package com.kifile.android.cornerstone.utils;

import android.os.Looper;

/**
 * The utils of Thread.
 *
 * @author kifile
 */
public class ThreadUtils {

    /**
     * @return If currentThread is main thread, return true.
     */
    public static boolean isMain() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
