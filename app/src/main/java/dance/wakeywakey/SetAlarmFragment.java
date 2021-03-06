package dance.wakeywakey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
                timePickerDialog.show(getActivity().getSupportFragmentManager(), Constants.TAG);
            }
        });

        timePickerTextView.setText(String.format("x:xx", alarmHour, alarmMinutes));
    }

    private void setAlarmTime(int hour, int minutes) {
        alarmHour = hour;
        alarmMinutes = minutes;

        String timeString = String.format("%d:%02d", alarmHour, alarmMinutes);
        timePickerTextView.setText(timeString);

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getDefault());
        gc.set(GregorianCalendar.HOUR_OF_DAY, alarmHour);
        gc.set(GregorianCalendar.MINUTE, alarmMinutes);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.add(Calendar.DATE, 1);

        int timeInSeconds = (int) (gc.getTimeInMillis() / 1000l);

        ((MainActivity) getActivity()).addDataToPost("timestamp", String.valueOf(timeInSeconds));
        ((MainActivity) getActivity()).addDataToPost("timeString", timeString);
        ((MainActivity) getActivity()).nextSlideWithDelay();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        setAlarmTime(hourOfDay, minute);
    }
}
