package dance.wakeywakey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by bert on 19/10/14.
 */
public class CompleteFragment extends Fragment {

    private TextView completeMessageTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_complete, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeMessageTextView = (TextView) view.findViewById(R.id.complete_message);
    }

    public void onPageSelected(int position) {
        MainActivity mainActivity = (MainActivity) getActivity();

        String name = mainActivity.getData("toFirstName");
        String time = mainActivity.getData("timeString");
        String message = String.format(getString(R.string.complete_message), name, time);
        completeMessageTextView.setText(message);
    }
}
