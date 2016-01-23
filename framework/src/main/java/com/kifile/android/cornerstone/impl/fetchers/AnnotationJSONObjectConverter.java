package com.kifile.android.cornerstone.impl.fetchers;

import android.support.annotation.NonNull;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.annotations.Property;
import com.kifile.android.cornerstone.impl.annotations.Singler;
import com.kifile.android.cornerstone.impl.helper.SingleData;
import com.kifile.android.cornerstone.utils.ReflectUtils;

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

    private Singler mSingler;

    public AnnotationJSONObjectConverter(DataFetcher<JSONObject> proxy, Class<DATA> clazz) {
        super(proxy);
        mDataClazz = clazz;
        mSingler = clazz.getAnnotation(Singler.class);
        Class<?> c = clazz;
        while (c != Object.class) {
            processAnnotation(c);
            c = c.getSuperclass();
        }
    }

    private void processAnnotation(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
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
            DATA data = null;
            if (mSingler != null) {
                String key = mSingler.primary();
                Field field = mAnnotationMap.get(key);
                if (field == null) {
                    throw new RuntimeException("Primary key must be existed and be annotated by Property.");
                }
                Object index = ReflectUtils.getValueFromField(jsonObject, key, field);
                data = SingleData.obtain(mDataClazz, index);
                if (data == null) {
                    data = mDataClazz.newInstance();
                    SingleData.updateData(mDataClazz, index, data);
                }
            }
            if (data == null) {
                data = mDataClazz.newInstance();
            }
            for (Map.Entry<String, Field> entry : mAnnotationMap.entrySet()) {
                Field field = entry.getValue();
                String key = entry.getKey();
                Object value = ReflectUtils.getValueFromField(jsonObject, key, field);
                if (value != null) {
                    ReflectUtils.setValue(data, field, value);
                }
            }
            return data;
        } catch (InstantiationException e) {
            throw new ConvertException(e);
        } catch (IllegalAccessException e) {
            throw new ConvertException(e);
        }
    }

}
