package com.kifile.android.sample.cornerstone.data;

import android.provider.ContactsContract;
import android.util.Log;

import com.kifile.android.cornerstone.core.AbstractFetcherConverter;
import com.kifile.android.cornerstone.core.ConvertErrorCallback;
import com.kifile.android.cornerstone.core.DataFetcher;
import com.kifile.android.cornerstone.impl.annotations.Property;
import com.kifile.android.cornerstone.impl.fetchers.AnnotationCursorConverter;
import com.kifile.android.cornerstone.impl.providers.ContentDataProvider;
import com.kifile.android.sample.cornerstone.SampleApplication;

import java.util.List;


/**
 * Created by kifile on 15/8/25.
 */
public class ContactsProvider extends ContentDataProvider<List<ContactsProvider.Contact>> implements ConvertErrorCallback {

    public static final String KEY = ContactsProvider.class.getSimpleName();

    public ContactsProvider() {
        super(SampleApplication.instance, ContactsContract.Contacts.CONTENT_URI);
        setFetcher(new AnnotationCursorConverter<>(buildCursorFetcher(), Contact.class));
    }

    @Override
    public void onConvertError(Object error) {
        Log.d(KEY, String.valueOf(error));
    }

//    public class ContactConverter extends AbstractFetcherConverter<Cursor, List<Contact>> {
//
//        /**
//         * Wrap the DataFetcher will be transformed.
//         *
//         * @param proxy
//         */
//        public ContactConverter(DataFetcher<Cursor> proxy) {
//            super(proxy);
//        }
//
//        @Override
//        protected List<Contact> convert(Cursor cursor) {
//            if (cursor != null) {
//                int nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                List<Contact> contacts = new ArrayList<>();
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    String name = cursor.getString(nameColumn);
//                    Contact contact = new Contact();
//                    contact.name = name;
//                    contacts.add(contact);
//                }
//                cursor.close();
//                return contacts;
//            }
//            return null;
//        }
//    }

    public static class Contact {

        @Property(name = ContactsContract.Contacts.DISPLAY_NAME)
        public String name;

    }

    private class ErrorConverter extends AbstractFetcherConverter<List<Contact>, List<Contact>> {

        public ErrorConverter(DataFetcher<List<Contact>> proxy) {
            super(proxy);
        }

        public ErrorConverter(DataFetcher<List<Contact>> proxy, ConvertErrorCallback errorCallback) {
            super(proxy, errorCallback);
        }

        @Override
        protected List<Contact> convert(List<Contact> contact) {
            if (contact != null) {
                handleError("String");
            }
            return null;
        }
    }
}
