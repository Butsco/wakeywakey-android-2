package dance.wakeywakey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bert on 18/10/14.
 */
public class SetAlarmFragment extends Fragment {

    /*
    private static SetAlarmFragment instance;

    public static final SetAlarmFragment newInstance()
    {
        if (instance == null) {
            instance = new SetAlarmFragment();
        }
        return instance;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_set_alarm, container, false);
        return rootView;
    }
}
