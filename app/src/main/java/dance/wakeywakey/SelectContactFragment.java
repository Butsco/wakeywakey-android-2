package dance.wakeywakey;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by bert on 18/10/14.
 */
public class SelectContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public static final String TAG = "SelectContactsFragment";

    private static int CONTACT_LOADER = 0;
    private static int PHONE_NUMBER_LOADER = 1;

    private boolean contactsLoaded = false;
    private boolean numbersLoaded = false;

    private Cursor contactsCursor;
    private Cursor numberCursor;
    ArrayList<MyContact> data;
    ContactAdapter adapter;
    StickyListHeadersListView listView;

    private int selectedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_contact, container, false);

        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        getLoaderManager().initLoader(PHONE_NUMBER_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (StickyListHeadersListView) getActivity().findViewById(android.R.id.list);
        listView.setFastScrollEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        if (loaderId == CONTACT_LOADER) {
            return ContactFetcher.getAllContactsThatHaveAPhoneNumber(getActivity());
        } else if (loaderId == PHONE_NUMBER_LOADER) {
            return ContactFetcher.getAllPhoneNumbers(getActivity());
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursorLoader.getId() == CONTACT_LOADER) {
            contactsLoaded = true;
            contactsCursor = cursor;
        } else if (cursorLoader.getId() == PHONE_NUMBER_LOADER) {
            numbersLoaded = true;
            numberCursor = cursor;
        }

        showList();
    }

    private void showList() {
        if (contactsLoaded && numbersLoaded) {
            setData();

            adapter = new ContactAdapter(this, data);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }
    }

    private void setData() {
        data = new ArrayList<MyContact>();
        HashMap<Integer, MyContact> contacts = new HashMap<Integer, MyContact>();

        for (contactsCursor.moveToFirst(); !contactsCursor.isAfterLast(); contactsCursor.moveToNext()) {
            MyContact contact = new MyContact();
            contact.id = contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));

            String displayName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String[] nameParts = StringUtils.split(displayName, " ");

            if (nameParts.length > 1) {
                contact.firstName = nameParts[0];
                contact.lastName = StringUtils.join(Arrays.copyOfRange(nameParts, 1, nameParts.length), " ");
            } else {
                contact.firstName = displayName;
                contact.lastName = "";
            }

            contacts.put(Integer.valueOf(contact.id), contact);
        }

        for (numberCursor.moveToFirst(); !numberCursor.isAfterLast(); numberCursor.moveToNext()) {
            Integer id = Integer.valueOf(numberCursor.getInt(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
            String number = numberCursor.getString(numberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            try {
                if (number != null) {
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, "BE");
                    boolean isValid = phoneUtil.isValidNumber(phoneNumber);
                    if (isValid) {
                        number = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                    }
                }
            } catch (NumberParseException e) {
                Log.i(TAG, "cannot parse number " + number);
            }

            if (contacts.containsKey(id)) {
                contacts.get(id).tels.add(number);
            }
        }

        contactsCursor.close();
        numberCursor.close();

        Iterator it = contacts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Integer key = (Integer) pairs.getKey();
            MyContact contact = (MyContact) pairs.getValue();

            if (contact.tels == null) {
                contacts.remove(key);
            } else {
                data.add(contact);
            }

            Log.d(TAG, "contact: " + contact.firstName + " " + contact.lastName + " " + StringUtils.join(contact.tels, ", "));
        }

        // sort by name
        Collections.sort(data, new NameComparator());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setSelectedPosition(position);
        adapter.notifyDataSetChanged();

        if (data != null && data.size() >= position) {
            MyContact contact = data.get(position);
            ((MainActivity) getActivity()).addDataToPost("to", contact.getFirstMsisdn());
            ((MainActivity) getActivity()).nextSlideWithDelay();
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
