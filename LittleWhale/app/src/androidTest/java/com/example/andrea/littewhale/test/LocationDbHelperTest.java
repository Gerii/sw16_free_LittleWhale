package com.example.andrea.littewhale.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.example.andrea.model.LocationContract;
import com.example.andrea.model.LocationDbHelper;

import junit.framework.TestCase;

import java.util.Objects;


/**
 * Created by andrea on 21.04.16.
 */
public class LocationDbHelperTest extends TestCase {

    private LocationDbHelper dbHelper;
    protected Context mContext;

    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(mContext, "test_");
        dbHelper = new LocationDbHelper(context);

    }

    public void tearDown() throws Exception {
        dbHelper.close();
        super.tearDown();
    }

    public void testDbConnection () {
        assertNotNull(dbHelper);
    }

    public void testInsertEntry() {

        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_NAME, "test");
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LADITUDE, 12.0);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, 15.0);

        long newRowId;

        newRowId = writeDb.insert(
                LocationContract.LocationEntry.TABLE_NAME,
                "null",
                values);


        /*String[] whereValues = new String[] { Long.toString(newRowId) };
        String[] whereCondition;

        SQLiteDatabase readDb = dbHelper.getReadableDatabase();

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

        Cursor c = readDb.query(
                LocationContract.LocationEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(c.moveToNext()){
            Log.w("STURM", "ID: " + c.getString(0));
            Log.w("STURM", "Name: " + c.getString(1));
            Log.w("STURM", "Lat: " + c.getString(2));
            Log.w("STURM", "Lon: " + c.getString(3));
        }*/
    }
}