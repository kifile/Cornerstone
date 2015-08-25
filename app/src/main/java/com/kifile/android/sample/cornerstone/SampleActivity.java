package com.kifile.android.sample.cornerstone;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.impl.Cornerstone;
import com.kifile.android.sample.cornerstone.data.SampleDataProvider;

import java.util.List;

/**
 * The sample of using DataProvider in Activity.
 *
 * @author kifile
 */
public class SampleActivity extends ListActivity {

    private SampleDataProvider mProvider;

    private DataObserver<List<Class>> mObserver = new DataObserver<List<Class>>() {
        @Override
        public void onDataChanged(List<Class> classes) {
            setListAdapter(new ActivityAdapter(SampleActivity.this, classes));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProvider == null) {
            mProvider =
                    (SampleDataProvider) Cornerstone.obtainProvider(SampleDataProvider.KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProvider.registerDataObserver(mObserver);
    }

    @Override
    protected void onPause() {
        mProvider.unregisterDataObserver(mObserver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mProvider != null) {
            Cornerstone.releaseProvider(SampleDataProvider.KEY);
            mProvider = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Class clazz = (Class) getListAdapter().getItem(position);
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public class ActivityAdapter extends BaseAdapter {

        private Context mContext;

        private List<Class> mClasses;

        public ActivityAdapter(Context context, List<Class> classes) {
            mContext = context;
            mClasses = classes;
        }

        @Override
        public int getCount() {
            return mClasses != null ? mClasses.size() : 0;
        }

        @Override
        public Class getItem(int position) {
            return mClasses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(mContext);
            }
            TextView view = (TextView) convertView;
            view.setText(getItem(position).getSimpleName());
            return view;
        }
    }

}
