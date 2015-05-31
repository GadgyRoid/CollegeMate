package com.gadgetroid.collegemate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class AssignmentDetails extends ActionBarActivity {
    DBAdapter myDb;
    String title,subject,notes;
    int day, month, year, minute, hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);

        Intent intent = getIntent();
        long itemId = Long.valueOf(intent.getStringExtra("id"));

        openDb();
        Cursor cursor = myDb.getRow(itemId);
        if(cursor.moveToFirst()) {
            long idDB = cursor.getLong(DBAdapter.COL_ROWID);
            title = cursor.getString(DBAdapter.COL_TITLE);
            day = cursor.getInt(DBAdapter.COL_DAY);
            month = cursor.getInt(DBAdapter.COL_MONTH);
            year = cursor.getInt(DBAdapter.COL_YEAR);
            minute = cursor.getInt(DBAdapter.COL_MINUTE);
            hour = cursor.getInt(DBAdapter.COL_HOUR);
            subject = cursor.getString(DBAdapter.COL_SUB);
            notes = cursor.getString(DBAdapter.COL_NOTES);
        }

        TextView mTitle = (TextView)findViewById(R.id.info_text);
        mTitle.setText(title);
        String subtitle = "Due on " + day + "/" + month + "/" + year + " at " + hour + ":" + minute;
        TextView mSubtitle = (TextView)findViewById(R.id.sub_text);
        mSubtitle.setText(subtitle);
        TextView mSubject = (TextView)findViewById(R.id.sub_text1);
        mSubject.setText(subject);
        TextView mNotes = (TextView)findViewById(R.id.cvNotes);
        mNotes.setText(notes);
    }

    public void deleteEntry(View view) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this assignment?")
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent intent = getIntent();
                       long itemId = Long.valueOf(intent.getStringExtra("id"));
                       myDb.deleteRow(itemId);
                       AssignmentList.assignmentList.deferNotifyDataSetChanged();
                       supportFinishAfterTransition();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
        return builder.create();
    }

    private void openDb() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDb() {
        myDb.close();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assignment_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
