package com.kifile.android.cornerstone.impl.fetchers;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.annotations.Property;
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
    protected List<DATA> convert(JSONArray array) {
        if (array != null) {
            List<DATA> datas = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    DATA data = mDataClazz.newInstance();
                    for (Map.Entry<String, Field> entry : mAnnotationMap.entrySet()) {
                        Field field = entry.getValue();
                        String key = entry.getKey();
                        Class<?> clazz = field.getType();
                        if (clazz.equals(String.class)) {
                            // For String data.
                            field.set(data, object.optString(key));
                        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                            // For int data.
                            field.set(data, object.optInt(key));
                        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                            // For long data.
                            field.set(data, object.optLong(key));
                        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                            // For double data.
                            field.set(data, object.optDouble(key));
                        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                            // For boolean data.
                            field.set(data, object.optBoolean(key));
                        } else {
                            throw new RuntimeException("Unsupported type:" + field.getName() + field.getType().getName());
                        }
                    }
                    datas.add(data);
                } catch (InstantiationException | IllegalAccessException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return datas;
        }
        return null;
    }
}
