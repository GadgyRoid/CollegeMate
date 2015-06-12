package com.gadgetroid.collegemate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;


public class AssignmentList extends ActionBarActivity {
    DBAdapter myDb;
    public static ListView assignmentList;
    CheckBox checkBox;
    //public static ArrayAdapter<Assignment> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);
        assignmentList = (ListView) findViewById(R.id.assList);

        openDB();
        populateListViewFromDb();

        com.melnykov.fab.FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(assignmentList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewAssignment.class);
                startActivity(intent);
            }
        });


        //checkBox = (CheckBox) findViewById(R.id.checkBox);


        final View sharedTextView = findViewById(R.id.assText);

        assignmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssignmentList.this, AssignmentDetails.class);
                String s = String.valueOf(id);
                intent.putExtra("id", s);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        AssignmentList.this,
                        new Pair<View, String>(view.findViewById(R.id.assText),
                                getString(R.string.text_transition)));
                ActivityCompat.startActivity(AssignmentList.this, intent, options.toBundle());
            }
        });

        //ifTaskCompleted();
    }

    /*private void ifTaskCompleted() {
        Cursor cursor = myDb.getAllRows();
        startManagingCursor(cursor);
    }*/

    private void populateListViewFromDb() {
        Cursor cursor = myDb.getAllRows();
        startManagingCursor(cursor);
        //String dueOn = DBAdapter.KEY_DAY + "/" + DBAdapter.KEY_MONTH + "/" + DBAdapter.KEY_YEAR;
        String[] fromFieldNames = new String[] {DBAdapter.KEY_TITLE,DBAdapter.KEY_DAY,DBAdapter.KEY_SUB};
        int[] toViewIds = new int[] {R.id.assText,R.id.dueText,R.id.contextTextView};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,R.layout.asslist_row_item,
                cursor,fromFieldNames,toViewIds);
        assignmentList.setAdapter(myCursorAdapter);

    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void closeDB() {
        myDb.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assignment_list, menu);
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

        /*if (id == R.id.addNew) {
            Intent intent = new Intent(this,NewAssignment.class);
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(item);
    }
}
