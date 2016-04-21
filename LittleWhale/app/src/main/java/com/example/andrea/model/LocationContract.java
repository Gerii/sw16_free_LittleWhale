package com.example.andrea.model;

import android.provider.BaseColumns;

/**
 * Created by andrea on 21.04.16.
 */
public final class LocationContract {
    public LocationContract() {}

    /* Inner class that defines the table contents */
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        // public static final String COLUMN_NAME_ENTRY_ID = "locationid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LADITUDE = "laditude";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LONGITUDE + REAL_TYPE + COMMA_SEP +
                    LocationEntry.COLUMN_NAME_LADITUDE + REAL_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;


}

