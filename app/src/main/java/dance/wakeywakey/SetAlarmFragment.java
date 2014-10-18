package dance.wakeywakey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by bert on 18/10/14.
 */
public class SetAlarmFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    private TextView timePickerTextView;

    private int alarmHour = 8;
    private int alarmMinutes = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_set_alarm, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Calendar calendar = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        timePickerTextView = (TextView) getView().findViewById(R.id.time_picker);
        timePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.initialize(SetAlarmFragment.this, alarmHour, alarmMinutes, true, false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), Contants.TAG);
            }
        });

        setAlarmTime(alarmHour, alarmMinutes);
    }

    private void setAlarmTime(int hour, int minutes) {
        alarmHour = hour;
        alarmMinutes = minutes;

        timePickerTextView.setText(String.format("%d:%02d", alarmHour, alarmMinutes));
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        setAlarmTime(hourOfDay, minute);
    }
}
