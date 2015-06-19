package com.gadgetroid.collegemate;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gadgetroid on 19/06/15.
 */
public class ContextCursorAdapter extends android.widget.CursorAdapter {
    public ContextCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.spinner_context, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Setting up the views to instantiate
        TextView contextSpinnerTitle = (TextView)view.findViewById(R.id.AssListSpinnerTextView);

        //Extract the data to display from Cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow("sub"));
    }
}
