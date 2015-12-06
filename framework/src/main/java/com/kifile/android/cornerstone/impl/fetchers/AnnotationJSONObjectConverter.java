package com.kifile.android.cornerstone.impl.fetchers;

import android.support.annotation.NonNull;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.core.FetchException;
import com.kifile.android.cornerstone.impl.annotations.Property;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON对象的注解转换器.
 *
 * @author kifile
 */
public class AnnotationJSONObjectConverter<DATA> extends AbstractFetcherConverter<JSONObject, DATA> {

    private final Class<DATA> mDataClazz;

    private Map<String, Field> mAnnotationMap = new HashMap<>();

    public AnnotationJSONObjectConverter(DataFetcher<JSONObject> proxy, Class<DATA> clazz) {
        super(proxy);
        mDataClazz = clazz;
        processAnnotation();
    }

    private void processAnnotation() {
        Field[] fields = mDataClazz.getFields();
        if (fields != null) {
            for (Field field : fields) {
                Property property = field.getAnnotation(Property.class);
                if (property != null) {
                    String name = property.name();
                    mAnnotationMap.put(name, field);
                }

            }
        }
    }

    @Override
    protected DATA convert(@NonNull JSONObject jsonObject) throws ConvertException {
        try {
            DATA data = mDataClazz.newInstance();
            for (Map.Entry<String, Field> entry : mAnnotationMap.entrySet()) {
                Field field = entry.getValue();
                String key = entry.getKey();
                Class<?> clazz = field.getType();
                if (clazz.equals(String.class)) {
                    // For String data.
                    field.set(data, jsonObject.optString(key));
                } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                    // For int data.
                    field.set(data, jsonObject.optInt(key));
                } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                    // For long data.
                    field.set(data, jsonObject.optLong(key));
                } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                    // For double data.
                    field.set(data, jsonObject.optDouble(key));
                } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                    // For boolean data.
                    field.set(data, jsonObject.optBoolean(key));
                } else {
                    // For other class type,
                    Object value = new AnnotationJSONObjectConverter<>(
                            new DataConverter<>(jsonObject.optJSONObject(key)), clazz).fetch();
                    field.set(data, value);
                }
            }
            return data;
        } catch (InstantiationException e) {
            throw new ConvertException(e);
        } catch (IllegalAccessException e) {
            throw new ConvertException(e);
        } catch (FetchException e) {
            throw new ConvertException(e);
        }
    }
}
