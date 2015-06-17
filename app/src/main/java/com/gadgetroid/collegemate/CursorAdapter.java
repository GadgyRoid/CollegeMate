package com.gadgetroid.collegemate;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gadgetroid on 17/06/15.
 */
public class CursorAdapter extends android.widget.CursorAdapter {
    public CursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.asslist_row_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView title = (TextView) view.findViewById(R.id.assText);
        TextView dueDate = (TextView) view.findViewById(R.id.dueText);
        TextView contextText = (TextView) view.findViewById(R.id.contextTextView);
        // Extract properties from cursor
        String mTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String mContext = cursor.getString(cursor.getColumnIndexOrThrow("sub"));
        int mDay = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
        int mMonth = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
        int mYear = cursor.getInt(cursor.getColumnIndexOrThrow("year"));

        //Formatting date String & setting output
        String mMonthName=(String)android.text.format.DateFormat.format("MMM", mMonth);
        Calendar c = Calendar.getInstance();
        int curDay = c.get(Calendar.DAY_OF_MONTH);

        if(mDay >= curDay) {
            if(mDay > curDay) {
                if((mDay - curDay) > 8) {
                    String fDay = mDay + "/" + mMonth + "/" + mYear;
                    SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        f1.setLenient(true);
                        Date d1 = f1.parse(fDay);
                        SimpleDateFormat f2 = new SimpleDateFormat("MMMM");
                        String f2Day = mDay + " " + f2.format(d1);
                        dueDate.setText(f2Day);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    String inDate = mDay + "/" + mMonth + "/" + mYear;
                    SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        f1.setLenient(true);
                        Date d1 = f1.parse(inDate);
                        SimpleDateFormat f2 = new SimpleDateFormat("EEEE");
                        String outDay = f2.format(d1);
                        dueDate.setText(outDay);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } else if (mDay == curDay) {
                dueDate.setText("Today");
            }
        }

        // Populate fields with extracted properties
        title.setText(mTitle);
        contextText.setText(String.valueOf(mContext));
    }
}
