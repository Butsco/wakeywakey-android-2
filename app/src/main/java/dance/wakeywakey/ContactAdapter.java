package dance.wakeywakey;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by bert on 18/10/14.
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer, StickyListHeadersAdapter {

    private final SelectContactFragment context;
    private LayoutInflater inflater;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;

    private ArrayList<MyContact> contacts = new ArrayList<MyContact>();

    public ContactAdapter(SelectContactFragment context, ArrayList<MyContact> contacts) {
        this.context = context;
        this.inflater = LayoutInflater.from(context.getActivity());
        this.contacts = contacts;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public MyContact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.contact, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.image = (ImageView) convertView.findViewById(R.id.userImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == context.getSelectedPosition()) {
            convertView.setBackgroundColor(context.getActivity().getResources().getColor(R.color.background_orange));
        } else {
            convertView.setBackgroundColor(context.getActivity().getResources().getColor(R.color.background_black));
        }

        // name
        holder.name.setText(contacts.get(position).getName());

        // avatar
        holder.image.setImageResource(R.drawable.default_avatar);
        //imageUrl = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contacts.get(position).id));

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.contacts_section_header, parent, false);
            holder.header = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        CharSequence headerChar = contacts.get(position).getName().subSequence(0, 1);
        holder.header.setText(headerChar);

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return contacts.get(position).getName().toUpperCase().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if (!contacts.isEmpty()) {

            char lastFirstChar = contacts.get(0).getName().charAt(0);
            sectionIndices.add(0);
            for (int i = 1; i < contacts.size(); i++) {
                if (contacts.get(i).getName().charAt(0) != lastFirstChar) {
                    lastFirstChar = contacts.get(i).getName().charAt(0);
                    sectionIndices.add(i);
                }
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = contacts.get(mSectionIndices[i]).getName().toUpperCase().charAt(0);
        }
        return letters;
    }

    class ViewHolder {
        TextView name;
        ImageView image;
    }

    class HeaderViewHolder {
        TextView header;
    }
}
