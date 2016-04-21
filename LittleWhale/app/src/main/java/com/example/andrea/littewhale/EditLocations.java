package com.example.andrea.littewhale;

import com.example.andrea.model.LocationContract;
import com.example.andrea.model.LocationDbHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.andrea.littewhale.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditLocations extends AppCompatActivity {

    List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //listview
        for(int i = 0; i < 10; i++){
            Map<String, String> tmp = new HashMap<String, String>(2);
            tmp.put("line1", "line" + (i + 1));
            tmp.put("line2", "value of second line" + (i + 1));
            valueList.add(tmp);
        }

        final ListAdapter adapter = new SimpleAdapter(this, valueList,
                android.R.layout.simple_list_item_2,
                new String[] {"line1", "line2" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        final ListView lv = (ListView)findViewById(R.id.listView);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String blub = lv.getAdapter().getItem(arg2).toString();
                Log.w("w", blub + " " + arg3);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("fab", "fab pressed");

                try {


                    LocationDbHelper dbHelper = new LocationDbHelper(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(LocationContract.LocationEntry.COLUMN_NAME_NAME, "test");
                    values.put(LocationContract.LocationEntry.COLUMN_NAME_LADITUDE, 12.0);
                    values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, 15.0);

                    long newRowId;
                    newRowId = db.insert(
                            LocationContract.LocationEntry.TABLE_NAME,
                            "null",
                            values);


                    SQLiteDatabase db2 = dbHelper.getReadableDatabase();

                    // Define a projection that specifies which columns from the database
                    // you will actually use after this query.
                    String[] projection = {
                            LocationContract.LocationEntry._ID,
                            LocationContract.LocationEntry.COLUMN_NAME_NAME,
                            LocationContract.LocationEntry.COLUMN_NAME_LADITUDE,
                            LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE,

                    };

                    // How you want the results sorted in the resulting Cursor
                    String sortOrder =
                            LocationContract.LocationEntry.COLUMN_NAME_NAME + " DESC";

                    Cursor c = db.query(
                            LocationContract.LocationEntry.TABLE_NAME,  // The table to query
                            projection,                               // The columns to return
                            null,                                // The columns for the WHERE clause
                            null,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            sortOrder                                 // The sort order
                    );

                    while(c.moveToNext()){
                        Log.w("STURM", "ID: " + c.getString(0));
                        Log.w("STURM", "Name: " + c.getString(1));
                        Log.w("STURM", "Lat: " + c.getString(2));
                        Log.w("STURM", "Lon: " + c.getString(3));
                    }
                }
                catch (Exception e)
                {
                    Log.w("graz", e);
                }

            }
        });
    }

}
