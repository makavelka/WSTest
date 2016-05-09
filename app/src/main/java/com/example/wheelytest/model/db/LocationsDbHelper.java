package com.example.wheelytest.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsDbHelper extends SQLiteOpenHelper {

    private static LocationsDbHelper sInstance;

    public static final String DB_NAME = "locations.sqlite";
    public static final int DB_VERSION = 1;
    public static final String TABLE_LOCATIONS = "locations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "lat";
    public static final String COLUMN_LONGITUDE = "long";
    public static final String COLUMN_SEND = "send";

    private final String DATABASE_CREATE = "create table " + TABLE_LOCATIONS
            + " (" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_LATITUDE
            + " real not null, " + COLUMN_LONGITUDE + " real not null"
			+ ", " + COLUMN_SEND + " text not null);";

    public static synchronized LocationsDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocationsDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public LocationsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS locations");
        onCreate(db);
    }
}