package com.gadgetroid.collegemate;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;


public class NewAssignment extends ActionBarActivity {
    public static TextView timeView;
    public static TextView dateView;
    public static String title;
    public static String date;
    public static String curTime;
    DialogFragment dateDialogFragment;
    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assignment);
        dateView = (TextView)findViewById(R.id.dateView);
        timeView = (TextView)findViewById(R.id.timeView);

        openDB();

        Calendar c = Calendar.getInstance();
        int curTimeHour = c.get(Calendar.HOUR_OF_DAY);
        int curTimeMinute = c.get(Calendar.MINUTE);
        curTime = getPaddedString(curTimeHour) + ":" + getPaddedString(curTimeMinute);
        timeView.setText(curTime);

        String curDay = c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.SHORT, Locale.ENGLISH);
        int curDate = c.get(Calendar.DATE);
        String curMon = c.getDisplayName(Calendar.MONTH,Calendar.SHORT,Locale.ENGLISH);
        int curYear = c.get(Calendar.YEAR);
        date = curDay + " " + String.valueOf(curDate) + " " + curMon + ", " + String.valueOf(curYear);
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

    private void addNewAssignment(String date, String curTime) {
        EditText mTitle = (EditText)findViewById(R.id.editText);
        title = mTitle.getText().toString();
        long newId = myDb.insertRow(title,date,curTime);
        Toast toast = Toast.makeText(this,"Added!",Toast.LENGTH_SHORT);
        toast.show();


    }

    private String getPaddedString(int value) {
        return String.format("%02d",value);
    }

    public static String getMonth(int month) {
        String[] mMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep",
                "Oct","Nov","Dec"};
        return mMonths[month];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
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
        getMenuInflater().inflate(R.menu.menu_new_assignment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            addNewAssignment(date,curTime);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
