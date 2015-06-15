package com.gadgetroid.collegemate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;


public class NewAssignment extends ActionBarActivity {
    Context context;
    public static TextView timeView;
    public static TextView dateView;
    public static String title;
    public static String date;
    public static String curTime;
    public static String subject;
    public static String notes;
    DialogFragment dateDialogFragment;
    DBAdapter myDb;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NewAssTheme);
        setContentView(R.layout.activity_new_assignment);
        dateView = (TextView)findViewById(R.id.dateView);
        timeView = (TextView)findViewById(R.id.timeView);
        Toolbar mToolbar;
        mToolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_clear_mtrl_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.abc_primary_text_material_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        openDB();

        c = Calendar.getInstance();
        int curTimeHour = c.get(Calendar.HOUR_OF_DAY);
        int curTimeMinute = c.get(Calendar.MINUTE);
        curTime = getPaddedString(curTimeHour) + ":" + getPaddedString(curTimeMinute);
        timeView.setText(curTime);

        String curDay = c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.ENGLISH);
        int curDate = c.get(Calendar.DATE);
        String curMon = c.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.ENGLISH);
        int curYear = c.get(Calendar.YEAR);
        date = curDay + " " + String.valueOf(curDate) + " " + curMon + " " + String.valueOf(curYear);
        dateView.setText(date);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Launch a DatePickerDialog
                dateDialogFragment = new DatePickerFragment();
                dateDialogFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Launch a TimePickerDialog
                DialogFragment timeDialogFragment = new TimePickerFragment();
                timeDialogFragment.show(getSupportFragmentManager(),"timePicker");
            }
        });
    }

    private void addNewAssignment(String date, String curTime) throws ParseException {
        EditText mTitle = (EditText)findViewById(R.id.editText);
        EditText mNotes = (EditText)findViewById(R.id.editText1);
        EditText mSub = (EditText)findViewById(R.id.editText2);
        title = mTitle.getText().toString();
        subject = mSub.getText().toString();
        notes = mNotes.getText().toString();
        long newId = myDb.insertRow(title,DatePickerFragment.day,DatePickerFragment.month,
                DatePickerFragment.year,TimePickerFragment.hour,TimePickerFragment.minutes,notes,subject,0);
        //Toast toast = Toast.makeText(this,"Added!",Toast.LENGTH_SHORT);
        //toast.show();
        setNotificationForCurrentReminder(newId);
    }

    private void setNotificationForCurrentReminder(long newId) {
        PendingIntent pendingIntent;
        Cursor cursor = myDb.getRow(newId);
        c.set(Calendar.YEAR, cursor.getInt(DBAdapter.COL_YEAR));
        c.set(Calendar.MONTH, cursor.getInt(DBAdapter.COL_MONTH));
        c.set(Calendar.DAY_OF_MONTH, cursor.getInt(DBAdapter.COL_DAY));
        c.set(Calendar.HOUR_OF_DAY, cursor.getInt(DBAdapter.COL_HOUR));
        c.set(Calendar.MINUTE, cursor.getInt(DBAdapter.COL_MINUTE));
        c.set(Calendar.SECOND, 0);

        Cursor notificationCursor = myDb.getAllRows();
        AlarmManager alarmManager;
        Intent intent = new Intent(NewAssignment.this, Receiver.class);
        intent.putExtra("title", cursor.getString(DBAdapter.COL_TITLE));
        intent.putExtra("id",cursor.getString(DBAdapter.COL_ROWID));
        pendingIntent = PendingIntent.getBroadcast
                (NewAssignment.this, Integer.parseInt(cursor.getString
                        (DBAdapter.COL_ROWID)), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
        Toast toast = Toast.makeText(this, "Reminder set for " + c.getTime(), Toast.LENGTH_LONG);
        toast.show();
    }

    /*private String getTimeFromDb(Cursor cursor) {
        //String time = cursor.getString(DBAdapter.COL_TIME);
        return time;
    }

    private String getDateFromDb(Cursor cursor) {
        //String date = cursor.getString(DBAdapter.COL_DATE);
        return date;
    }

    public static String getMonth(int month) {
        String[] mMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep",
                "Oct","Nov","Dec"};
        return mMonths[month];
    }*/

    private String getPaddedString(int value) {
        return String.format("%02d",value);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
        supportFinishAfterTransition();
    }

    private void closeDB() {
        myDb.close();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_assignment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            try {
                addNewAssignment(date,curTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finish();
            return true;
        }

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            closeDB();
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }
}
