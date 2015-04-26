package com.gadgetroid.collegemate;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by gadgetroid on 21/04/15.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minutes,
                DateFormat.is24HourFormat(getActivity()));}

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //TODO Do something with the time chosen by the user
        String curHour = getPaddedString(hourOfDay);
        String curMinute = getPaddedString(minute);
        String curTime = curHour + ":" + curMinute;
        NewAssignment.timeView.setText(curTime);
    }

    private String getPaddedString(int value) {
        return String.format("%02d",value);
    }
}
