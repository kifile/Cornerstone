package com.kifile.android.sample.cornerstone.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.AbstractDataProvider;
import com.kifile.android.cornerstone.impl.fetchers.CursorFetcher;
import com.kifile.android.sample.cornerstone.SampleApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifile on 15/8/25.
 */
public class ContactsProvider extends AbstractDataProvider<List<ContactsProvider.Contact>> {

    public static final String KEY = ContactsProvider.class.getSimpleName();

    public ContactsProvider() {
        Context context = SampleApplication.instance;
        setFetcher(new ContactConverter(new CursorFetcher(context.getContentResolver(), ContactsContract.Contacts.CONTENT_URI)));
    }


    public class ContactConverter extends AbstractFetcherConverter<Cursor, List<Contact>> {

        /**
         * Wrap the DataFetcher will be transformed.
         *
         * @param proxy
         */
        public ContactConverter(DataFetcher<Cursor> proxy) {
            super(proxy);
        }

        @Override
        protected List<Contact> convert(Cursor cursor) {
            if (cursor != null) {
                int nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                List<Contact> contacts = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String name = cursor.getString(nameColumn);
                    Contact contact = new Contact();
                    contact.name = name;
                    contacts.add(contact);
                }
                cursor.close();
                return contacts;
            }
            return null;
        }
    }

    public static class Contact {

        public String name;

    }
}
