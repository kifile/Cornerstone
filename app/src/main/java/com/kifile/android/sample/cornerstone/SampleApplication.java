package com.kifile.android.sample.cornerstone;

import android.app.Application;
import com.kifile.android.cornerstone.impl.Cornerstone;
import com.kifile.android.sample.cornerstone.data.ContactsProvider;
import com.kifile.android.sample.cornerstone.data.SampleDataProvider;

/**
 * Register the providers int application class.
 * <p/>
 * Created by kifile on 15/8/25.
 */
public class SampleApplication extends Application {

    public static Application instance;

    static {
        Cornerstone.registerProvider(SampleDataProvider.KEY, SampleDataProvider.class);
        Cornerstone.registerProvider(ContactsProvider.KEY, ContactsProvider.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
