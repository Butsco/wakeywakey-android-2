package dance.wakeywakey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

/**
 * Created by bert on 18/10/14.
 */
public class SetAlarmFragment extends Fragment {

    TimePicker timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_set_alarm, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timePicker = (TimePicker) getView().findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(8);
        timePicker.setCurrentMinute(0);

    }
}
