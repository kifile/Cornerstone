package com.kifile.android.cornerstone.utils;

import android.support.annotation.NonNull;

import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.FetchException;
import com.kifile.android.cornerstone.impl.annotations.Property;
import com.kifile.android.cornerstone.impl.fetchers.AnnotationJSONArrayConverter;
import com.kifile.android.cornerstone.impl.fetchers.AnnotationJSONObjectConverter;
import com.kifile.android.cornerstone.impl.fetchers.DataConverter;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author kifile
 */
public class ReflectUtils {

    /**
     * 通过反射,从JSONObject中获取数据.
     *
     * @param jsonObject
     * @param key
     * @param field
     * @return
     */
    public static Object getValueFromField(@NonNull JSONObject jsonObject, String key, Field field) {
        Class<?> clazz = field.getType();
        if (clazz.equals(String.class)) {
            // For String data.
            return jsonObject.optString(key, null);
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            // For int data.
            return jsonObject.optInt(key);
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            // For long data.
            return jsonObject.optLong(key);
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            // For double data.
            return jsonObject.optDouble(key);
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            // For boolean data.
            return jsonObject.optBoolean(key);
        } else if (clazz.equals(List.class)) {
            // For list class type,
            Property property = field.getAnnotation(Property.class);
            Class<?> itemType = property.type();
            try {
                return new AnnotationJSONArrayConverter<>(
                        new DataConverter<>(jsonObject.optJSONArray(key)), itemType).fetch();
            } catch (ConvertException ignore) {
                // Sometimes convert failed.
            } catch (FetchException ignore) {
                // Sometimes convert failed.
            }
            return null;
        } else {
            // For other class type,
            try {
                return new AnnotationJSONObjectConverter<>(
                        new DataConverter<>(jsonObject.optJSONObject(key)), clazz).fetch();
            } catch (FetchException e) {
                e.printStackTrace();
            } catch (ConvertException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
