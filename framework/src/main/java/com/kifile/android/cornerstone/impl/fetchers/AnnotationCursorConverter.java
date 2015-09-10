package com.kifile.android.cornerstone.impl.fetchers;

import android.database.Cursor;
import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.annotations.Property;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use annotation build result.
 * <p/>
 * Created by kifile on 15/9/10.
 */
public class AnnotationCursorConverter<DATA> extends AbstractFetcherConverter<Cursor, List<DATA>> {

    private final Class<DATA> mDataClazz;

    private Map<String, Field> mAnnotationMap = new HashMap<>();

    /**
     * Wrap the DataFetcher will be transformed.
     *
     * @param proxy
     */
    public AnnotationCursorConverter(DataFetcher<Cursor> proxy, Class<DATA> clazz) {
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
    protected List<DATA> convert(Cursor cursor) {
        if (cursor != null) {
            List<DATA> datas = new ArrayList<>();
            // Get column index of data;
            Map<String, Integer> columnMap = new HashMap<>();
            for (String key : mAnnotationMap.keySet()) {
                int column = cursor.getColumnIndex(key);
                if (column < 0) {
                    throw new RuntimeException("Property not exist in cursor");
                }
                columnMap.put(key, column);
            }
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    try {
                        DATA data = mDataClazz.newInstance();
                        for (Map.Entry<String, Field> entry : mAnnotationMap.entrySet()) {
                            Field field = entry.getValue();
                            Class<?> clazz = field.getType();
                            int column = columnMap.get(entry.getKey());
                            if (clazz.equals(String.class)) {
                                // For String data.
                                field.set(data, cursor.getString(column));
                            } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                                // For int data.
                                field.set(data, cursor.getInt(column));
                            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                                // For long data.
                                field.set(data, cursor.getLong(column));
                            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                                // For double data.
                                field.set(data, cursor.getDouble(column));
                            } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                                // For boolean data.
                                field.set(data, cursor.getInt(column) != 0);
                            } else {
                                throw new RuntimeException("Unsupported type:" + field.getName() + field.getType().getName());
                            }
                        }
                        datas.add(data);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                cursor.close();
            }
            return datas;
        }
        return null;
    }
}
