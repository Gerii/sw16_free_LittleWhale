package com.example.andrea.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by clemens on 28.04.16.
 */
public class LocationDb {

    private static LocationDb instance;

    private LocationDb () {}

    public static LocationDb getInstance () {
        if (LocationDb.instance == null) {
            LocationDb.instance = new LocationDb ();
        }
        return LocationDb.instance;
    }

    public long addLocation(Context context, String name, Double lat, Double lon){
        LocationDbHelper dbHelper = new LocationDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_NAME, name);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LADITUDE, lat);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, lon);

        long newRowId;
        newRowId = db.insert(
                LocationContract.LocationEntry.TABLE_NAME,
                "null",
                values);

        db.close();

        return newRowId;
    }

    public Location getLocation(Context context, Long id){
        LocationDbHelper dbHelper = new LocationDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LocationContract.LocationEntry._ID,
                LocationContract.LocationEntry.COLUMN_NAME_NAME,
                LocationContract.LocationEntry.COLUMN_NAME_LADITUDE,
                LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE,
        };

        String whereId = LocationContract.LocationEntry._ID + " like ?";

        String sortOrder =
                LocationContract.LocationEntry.COLUMN_NAME_NAME + " ASC";

        Cursor c = db.query(
                LocationContract.LocationEntry.TABLE_NAME,
                projection,
                whereId,
                new String[] { id.toString() },
                null,
                null,
                sortOrder
        );

        Location retLocation = null;

        while(c.moveToNext()){
            Long tmpId = Long.parseLong(c.getString(0));
            Double lat = Double.parseDouble(c.getString(2));
            Double lon = Double.parseDouble(c.getString(3));

            Location tmpLoc = new Location(tmpId, c.getString(1), lat, lon);
            retLocation = tmpLoc;
        }

        db.close();

        return  retLocation;
    }

    public ArrayList<Location> getAllLocations(Context context)
    {
        ArrayList<Location> locations = new ArrayList<>();

        LocationDbHelper dbHelper = new LocationDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LocationContract.LocationEntry._ID,
                LocationContract.LocationEntry.COLUMN_NAME_NAME,
                LocationContract.LocationEntry.COLUMN_NAME_LADITUDE,
                LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                LocationContract.LocationEntry.COLUMN_NAME_NAME + " ASC";

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
            Long id = Long.parseLong(c.getString(0));
            Double lat = Double.parseDouble(c.getString(2));
            Double lon = Double.parseDouble(c.getString(3));

            Location tmpLoc = new Location(id, c.getString(1), lat, lon);
            locations.add(tmpLoc);
        }

        db.close();

        return locations;
    }
}
