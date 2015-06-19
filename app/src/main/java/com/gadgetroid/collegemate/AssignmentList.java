package com.gadgetroid.collegemate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.melnykov.fab.FloatingActionButton;


public class AssignmentList extends ActionBarActivity {
    DBAdapter myDb;
    public static ListView assignmentList;
    public static CursorAdapter cursorAdapter;
    android.support.v7.view.ActionMode mActionMode;
    DrawerLayout mDrawerLayout;

    CheckBox checkBox;
    //public static ArrayAdapter<Assignment> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);
        assignmentList = (ListView) findViewById(R.id.assList);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar3);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Assignments");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        openDB();

        final android.support.v7.view.ActionMode.Callback modeCallBack = new android.support.v7.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
                actionMode.setTitle("Selected");
                actionMode.getMenuInflater().inflate(R.menu.menu_assignment_list_contextual,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(android.support.v7.view.ActionMode actionMode) {
                mActionMode = null;
            }
        };

        //Setting up the Spinner
        Spinner mSpinner = (Spinner)findViewById(R.id.AssListSpinner);
        Cursor mCursorForSpinner = myDb.getAllRows();
        String[]fromFieldNames=new String[]{DBAdapter.KEY_SUB};
        int[]toViewIds=new int[]{android.R.id.text1};
        SimpleCursorAdapter mSpinnerCursorAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,
                mCursorForSpinner,fromFieldNames,toViewIds);
        mSpinnerCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerCursorAdapter);

        //populateListViewFromDb();
        Cursor cursor = myDb.getAllRows();
        cursorAdapter = new CursorAdapter(this,cursor);
        assignmentList.setAdapter(cursorAdapter);
        assignmentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }
                mActionMode = startSupportActionMode(modeCallBack);
                view.setSelected(true);

                return true;
            }
        });



        com.melnykov.fab.FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(assignmentList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewAssignment.class);
                startActivity(intent);
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp,heightDp);
        if(smallestWidth >= 720) {
            fab.show(false);
        }
        else if(smallestWidth >= 600) {
            fab.show(false);
        }

        //Setting up nav drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.AssListNavDrawer);
        if(toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_hamburger);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }


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

        cursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor c= myDb.getAllRows();
        AssignmentList.cursorAdapter.changeCursor(c);
        AssignmentList.cursorAdapter.notifyDataSetChanged();
    }

    /*private void ifTaskCompleted() {
        Cursor cursor = myDb.getAllRows();
        startManagingCursor(cursor);
    }*/

    private void populateListViewFromDb(){
        Cursor cursor=myDb.getAllRows();
        startManagingCursor(cursor);
        //String dueOn = DBAdapter.KEY_DAY + "/" + DBAdapter.KEY_MONTH + "/" + DBAdapter.KEY_YEAR;
        String[]fromFieldNames=new String[]{DBAdapter.KEY_TITLE,DBAdapter.KEY_DAY,DBAdapter.KEY_SUB};
        int[]toViewIds=new int[]{R.id.assText,R.id.dueText,R.id.contextTextView};
        SimpleCursorAdapter myCursorAdapter=new SimpleCursorAdapter(this,R.layout.asslist_row_item,
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
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutAcitvity.class);
            startActivity(intent);
            return true;
        }

        /*if (id == R.id.addNew) {
            Intent intent = new Intent(this,NewAssignment.class);
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(item);
    }


}
