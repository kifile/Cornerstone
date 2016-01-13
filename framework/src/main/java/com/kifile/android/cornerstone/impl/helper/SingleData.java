package com.kifile.android.cornerstone.impl.helper;

import android.util.Log;
import android.util.LruCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 唯一数据存放位置.
 *
 * @author kifile
 */
public class SingleData {

    public static final String TAG = SingleData.class.getSimpleName();

    private static final int MAX_COUNT = 100;

    private static final Map<Class<?>, LruCache> clazzDataMap = new HashMap<>();

    private SingleData() {
        // Make no instance.
    }

    public static synchronized <DATA> DATA obtain(Class<DATA> clazz, Object index) {
        LruCache dataMap = clazzDataMap.get(clazz);
        if (dataMap != null) {
            DATA data = (DATA) dataMap.get(index);
            if (data != null) {
                Log.d(TAG, String.format("%s of %s exist.", clazz.getSimpleName(), String.valueOf(index)));
                Log.d(TAG, "Current Size" + dataMap.size());
            }
            return data;
        }
        return null;
    }

    public static synchronized <DATA> void updateData(Class<DATA> clazz, Object index, DATA value) {
        LruCache<Object, DATA> dataMap = clazzDataMap.get(clazz);
        if (dataMap == null) {
            dataMap = new LruCache<>(MAX_COUNT);
            clazzDataMap.put(clazz, dataMap);
        }
        Log.d(TAG, String.format("save %s into %s.", String.valueOf(index), clazz.getSimpleName()));
        Log.d(TAG, "Current Size" + dataMap.size());
        dataMap.put(index, value);
    }

}
