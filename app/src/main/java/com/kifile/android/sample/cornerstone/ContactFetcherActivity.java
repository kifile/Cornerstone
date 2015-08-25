package com.kifile.android.sample.cornerstone;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kifile.android.cornerstone.core.DataObserver;
import com.kifile.android.cornerstone.impl.Cornerstone;
import com.kifile.android.sample.cornerstone.data.ContactsProvider;

import java.util.List;

/**
 * Created by kifile on 15/8/25.
 */
public class ContactFetcherActivity extends ListActivity {

    private ContactsProvider mProvider;

    private DataObserver<List<ContactsProvider.Contact>> mContactObserver = new DataObserver<List<ContactsProvider.Contact>>() {
        @Override
        public void onDataChanged(List<ContactsProvider.Contact> contacts) {
            setListAdapter(new ContactAdapter(ContactFetcherActivity.this, contacts));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProvider == null) {
            mProvider = (ContactsProvider) Cornerstone.obtainProvider(ContactsProvider.KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProvider.registerDataObserver(mContactObserver);
    }

    @Override
    protected void onPause() {
        mProvider.unregisterDataObserver(mContactObserver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mProvider != null) {
            Cornerstone.releaseProvider(ContactsProvider.KEY);
            mProvider = null;
        }
        super.onDestroy();
    }

    public class ContactAdapter extends BaseAdapter {

        private Context mContext;
        private List<ContactsProvider.Contact> mContacts;

        public ContactAdapter(Context context, List<ContactsProvider.Contact> contacts) {
            mContext = context;
            mContacts = contacts;
        }

        @Override
        public int getCount() {
            return mContacts != null ? mContacts.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
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
            view.setText(mContacts.get(position).name);
            return view;
        }
    }
}
