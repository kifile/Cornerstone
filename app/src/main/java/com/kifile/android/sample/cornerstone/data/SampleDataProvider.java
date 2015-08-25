package com.kifile.android.sample.cornerstone.data;

import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.AbstractDataProvider;
import com.kifile.android.sample.cornerstone.ContactFetcherActivity;
import com.kifile.android.sample.cornerstone.SampleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * This provider will provider the information of sample activities.
 * <p/>
 * Created by kifile on 15/8/25.
 */
public class SampleDataProvider extends AbstractDataProvider<List<Class>> {

    public static final String KEY = SampleDataProvider.class.getSimpleName();

    public SampleDataProvider() {
        SampleActivityFetcher fetcher = new SampleActivityFetcher();
        fetcher.addActivity(SampleActivity.class);
        fetcher.addActivity(ContactFetcherActivity.class);
        setFetcher(fetcher);
    }

    public class SampleActivityFetcher implements DataFetcher<List<Class>> {

        private List<Class> mActivities;

        public SampleActivityFetcher() {
            mActivities = new ArrayList<>();
        }

        public void addActivity(Class clazz) {
            mActivities.add(clazz);
        }

        @Override
        public List<Class> fetch() {
            return mActivities;
        }
    }

}
