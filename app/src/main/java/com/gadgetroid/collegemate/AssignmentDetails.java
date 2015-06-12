package com.gadgetroid.collegemate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class AssignmentDetails extends ActionBarActivity {
    DBAdapter myDb;
    String title,subject,notes;
    int day, month, year, minute, hour, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);

        Intent intent = getIntent();
        long itemId = Long.valueOf(intent.getStringExtra("id"));

        TextView assDetTitleTextView = (TextView)findViewById(R.id.info_text);

        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolBar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
            done = cursor.getInt(DBAdapter.COL_DONE);
        }

        assDetTitleTextView.setText(title);
        String assDetDateString = day + "/" + month + "/" + year;
        TextView assDetDateTextView = (TextView)findViewById(R.id.AssDetDateTextView);
        assDetDateTextView.setText(assDetDateString);
        String assDetTimeString = minute + ":" + hour;
        TextView assDetTimeTextView = (TextView)findViewById(R.id.AssDetTimeTextView);
        assDetTimeTextView.setText(assDetTimeString);
        TextView assDetContextTextView = (TextView)findViewById(R.id.AssDetContextTextView);
        assDetContextTextView.setText(subject);
        TextView assDetNotesTextView = (TextView)findViewById(R.id.AssDetNotesTextView);
        assDetNotesTextView.setText(notes);

        //checkTaskStatus();
    }

    /*private void checkTaskStatus() {
        if(done == 1) {
            markDone();
        }
        else if (done == 0) {
            markUndone();
        }
    }

    private void markUndone() {
        TextView statusText = (TextView)findViewById(R.id.statusText);
        statusText.setText("PENDING");
        LinearLayout statusViewLayout = (LinearLayout)findViewById(R.id.layoutPendingOrNot);
        statusViewLayout.setBackgroundColor(Color.parseColor("#FF9800"));
        Button button = (Button)findViewById(R.id.button);
        button.setText("MARK AS DONE");
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
    }

    private void markDone() {
        TextView statusText = (TextView)findViewById(R.id.statusText);
        statusText.setText("DONE");
        LinearLayout statusViewLayout = (LinearLayout)findViewById(R.id.layoutPendingOrNot);
        statusViewLayout.setBackgroundColor(Color.parseColor("#8BC34A"));
        Button button = (Button)findViewById(R.id.button);
        button.setText("SET UNDONE");
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
    }

    public void markTaskAsDone(View view) {
        showDialog(2);
    }

    public void deleteEntry(View view) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 1) {
            builder.setMessage("Are you sure you want to delete this assignment?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getIntent();
                            long itemId = Long.valueOf(intent.getStringExtra("id"));
                            myDb.deleteRow(itemId);
                            Intent intent2 = new Intent(AssignmentDetails.this,Receiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast
                                    (AssignmentDetails.this, (int) itemId, intent2,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
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
        }
        else if(id == 2) {
            Intent intent3 = getIntent();
            final long itemId1 = Long.valueOf(intent3.getStringExtra("id"));
            if(done == 0) {
                builder.setMessage("Are you sure you want to mark this assignment as done?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDb.updateRow(itemId1, title, day, month, year, hour, minute,
                                        notes, subject, 1);
                                TextView statusText = (TextView)findViewById(R.id.statusText);
                                statusText.setText("DONE");
                                LinearLayout statusViewLayout = (LinearLayout)findViewById(R.id.layoutPendingOrNot);
                                statusViewLayout.setBackgroundColor(Color.parseColor("#8BC34A"));
                                Button button = (Button)findViewById(R.id.button);
                                button.setText("SET UNDONE");
                                getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                                AssignmentList.assignmentList.deferNotifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            else if (done == 1) {
                builder.setMessage("Are you sure you want to mark this assignment as undone?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDb.updateRow(itemId1, title, day, month, year, hour, minute,
                                        notes, subject, 0);
                                TextView statusText = (TextView)findViewById(R.id.statusText);
                                statusText.setText("PENDING");
                                LinearLayout statusViewLayout = (LinearLayout)findViewById(R.id.layoutPendingOrNot);
                                statusViewLayout.setBackgroundColor(Color.parseColor("#FF9800"));
                                Button button = (Button)findViewById(R.id.button);
                                button.setText("MARK AS DONE");
                                getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                                AssignmentList.assignmentList.deferNotifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        }
            return builder.create();
    }*/

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
