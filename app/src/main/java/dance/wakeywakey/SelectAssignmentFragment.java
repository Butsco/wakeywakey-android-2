package dance.wakeywakey;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by bert on 18/10/14.
 */
public class SelectAssignmentFragment extends Fragment implements AdapterView.OnItemClickListener {

    private int selectedItem = -1;

    ListView listView;
    AssignmentArrayAdapter adapter;
    String[] assignments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_assignment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assignments = getResources().getStringArray(R.array.assingments);

        listView = (ListView) view.findViewById(android.R.id.list);
        adapter = new AssignmentArrayAdapter(getActivity(), R.layout.assignment, android.R.id.text1, assignments);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = position;
        adapter.notifyDataSetChanged();

        ((MainActivity) getActivity()).addDataToPost("mood", assignments[position]);
        ((MainActivity) getActivity()).nextSlideWithDelay();
    }


    private class AssignmentArrayAdapter extends ArrayAdapter<String> {
        public AssignmentArrayAdapter(Context context, int resource, int textViewResourceId, String[] assignments) {
            super(context, resource, textViewResourceId, assignments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            if (position == selectedItem) {
                convertView.setBackgroundColor(getResources().getColor(R.color.background_grey));
                ((TextView) convertView.findViewById(android.R.id.text1)).setTextColor(getResources().getColor(R.color.background_light_red));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.background_black));
                ((TextView) convertView.findViewById(android.R.id.text1)).setTextColor(getResources().getColor(R.color.text_white));
            }

            return convertView;
        }
    }
}


