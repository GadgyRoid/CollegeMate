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
    ListView assignmentList;
    CheckBox checkBox;
    //public static ArrayAdapter<Assignment> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);
        assignmentList = (ListView)findViewById(R.id.assList);

        openDB();
        populateListViewFromDb();

        /*FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_add_new))
                .withButtonColor(getResources().getColor(R.color.greenAccent))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();*/

        com.melnykov.fab.FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(assignmentList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewAssignment.class);
                startActivity(intent);
            }
        });


        checkBox = (CheckBox)findViewById(R.id.checkBox);
        /*checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = ((CheckBox)v).isChecked();

                if(done) {
                    //TODO delete the item from database
                    TextView textView = (TextView)findViewById(R.id.assText);
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });*/

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
                ActivityCompat.startActivity(AssignmentList.this,intent,options.toBundle());
            }
        });
    }

    /*public void ifDone(View view) {
        boolean done = ((CheckBox)view).isChecked();
        if(done) {
            TextView textView = (TextView)findViewById(R.id.assText);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }*/

    private void populateListViewFromDb() {
        Cursor cursor = myDb.getAllRows();
        startManagingCursor(cursor);
        String[] fromFieldNames = new String[] {DBAdapter.KEY_TITLE};
        int[] toViewIds = new int[] {R.id.assText};
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
