package com.gadgetroid.collegemate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gadgetroid on 21/04/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public static int year;
    public static int month;
    public static int day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int myear, int mmonth, int mday) {
        year = myear;
        month = mmonth;
        day = mday;
        //TODO Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        String curDay = c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT,Locale.ENGLISH);
        String curMon = c.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.ENGLISH);
        String date = curDay + " " + String.valueOf(day) + " " + curMon + ", " + String.valueOf(year);
        NewAssignment.dateView.setText(date);
    }


}
