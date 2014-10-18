package dance.wakeywakey;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by bert on 18/10/14.
 */
public class ContactFetcher {
    public static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
    /**
     * http://developer.android.com/guide/topics/providers/contacts-provider.html
     */
    static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts.STARRED
    };

    static final String SELECTION = "((" +
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " NOTNULL) AND (" +
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " != '' )) AND " +
            ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";
    static final String SORTING = "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + ") ASC ";
    static final String[] PHONE_PROJECTION = {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    static final Uri PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    static final String PHONE_SORTING = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " DESC";

    public static Loader<Cursor> getAllContactsThatHaveAPhoneNumber(Context context) {
        return new CursorLoader(context, URI, PROJECTION, SELECTION, null, SORTING);
    }

    public static Loader<Cursor> getAllPhoneNumbers(Context context) {
        return new CursorLoader(context, PHONE_URI, PHONE_PROJECTION, null, null, PHONE_SORTING);
    }
}
