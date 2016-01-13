package com.kifile.android.cornerstone.impl.fetchers;

import android.support.annotation.NonNull;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.ConvertException;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.annotations.Property;
import com.kifile.android.cornerstone.impl.annotations.Singler;
import com.kifile.android.cornerstone.impl.helper.SingleData;
import com.kifile.android.cornerstone.utils.ReflectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use annotation build result.
 * <p/>
 * Created by kifile on 15/9/11.
 */
public class AnnotationJSONArrayConverter<DATA> extends AbstractFetcherConverter<JSONArray, List<DATA>> {

    private final Class<DATA> mDataClazz;

    private Map<String, Field> mAnnotationMap = new HashMap<>();

    private Singler mSingler;

    /**
     * Wrap the DataFetcher will be transformed.
     *
     * @param proxy
     */
    public AnnotationJSONArrayConverter(DataFetcher<JSONArray> proxy, Class<DATA> clazz) {
        super(proxy);
        mDataClazz = clazz;
        processAnnotation();
    }

    private void processAnnotation() {
        mSingler = mDataClazz.getAnnotation(Singler.class);
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
    protected List<DATA> convert(@NonNull JSONArray array) throws ConvertException {
        List<DATA> datas = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    DATA data = null;
                    if (mSingler != null) {
                        String key = mSingler.primary();
                        Field field = mAnnotationMap.get(key);
                        if (field == null) {
                            throw new RuntimeException("Primary key must be existed and be annotated by Property.");
                        }
                        Object index = ReflectUtils.getValueFromField(object, key, field);
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
                        Object value = ReflectUtils.getValueFromField(object, key, field);
                        if (value != null) {
                            field.set(data, value);
                        }
                    }
                    datas.add(data);
                } catch (JSONException ignore) {
                    // Maybe the JSONArray just hold the value array.
                    DATA object = (DATA) array.opt(i);
                    datas.add(object);
                }
            }
        } catch (InstantiationException e) {
            throw new ConvertException(e);
        } catch (IllegalAccessException e) {
            throw new ConvertException(e);
        }
        return datas;
    }

}
