package com.example.wheelytest.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.wheelytest.model.GeoData;

import java.util.ArrayList;

public class LocationsDbDataSource {
    private SQLiteDatabase mDatabase;
    private final LocationsDbHelper mDbHelper;
    private String[] mAllColumns = { LocationsDbHelper.COLUMN_ID, 
            LocationsDbHelper.COLUMN_LATITUDE,
            LocationsDbHelper.COLUMN_LONGITUDE,  
            LocationsDbHelper.COLUMN_SEND };

    public LocationsDbDataSource(Context context) {
        mDbHelper = LocationsDbHelper.getInstance(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public GeoData createGeoData(String lat, String lon, boolean isSend) {
        ContentValues values = new ContentValues();
        values.put(LocationsDbHelper.COLUMN_LATITUDE, lat);
        values.put(LocationsDbHelper.COLUMN_LONGITUDE, lon);
        values.put(LocationsDbHelper.COLUMN_SEND, isSend);
        mDatabase.insert(LocationsDbHelper.TABLE_LOCATIONS, null,
                values);
        Cursor cursor = mDatabase.query(LocationsDbHelper.TABLE_LOCATIONS, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        GeoData geoData = cursorToGeoData(cursor);
        cursor.close();
        return geoData;
    }

    public ArrayList<GeoData> getAllGeoDatas() {
        ArrayList<GeoData> gpsPoints = new ArrayList<GeoData>();
        Cursor cursor = mDatabase.query(LocationsDbHelper.TABLE_LOCATIONS,
                mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GeoData point = cursorToGeoData(cursor);
            gpsPoints.add(point);
            cursor.moveToNext();
        }
        cursor.close();
        return gpsPoints;
    }

    public ArrayList<GeoData> getNotSendedPoints() {
        ArrayList<GeoData> gpsPoints = new ArrayList<GeoData>();
        String selection = LocationsDbHelper.COLUMN_SEND + " = ?";
        String[] selectionArgs = new String[] { "true" };
        Cursor cursor = mDatabase.query(LocationsDbHelper.TABLE_LOCATIONS, null, selection, selectionArgs, null, null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GeoData point = cursorToGeoData(cursor);
            gpsPoints.add(point);
            cursor.moveToNext();
        }
        cursor.close();
        return gpsPoints;
    }

    public void addGeoData(GeoData point) {
        ContentValues values = new ContentValues();
        values.put(LocationsDbHelper.COLUMN_LATITUDE, point.getLat());
        values.put(LocationsDbHelper.COLUMN_LONGITUDE, point.getLon());
        values.put(LocationsDbHelper.COLUMN_SEND, point.isSended());
        mDatabase.insert(LocationsDbHelper.TABLE_LOCATIONS, null,
                values);
        Cursor cursor = mDatabase.query(LocationsDbHelper.TABLE_LOCATIONS, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
    }

    private GeoData cursorToGeoData(Cursor cursor) {
        GeoData point = new GeoData();
        point.setId(cursor.getLong(0));
        point.setLat(cursor.getDouble(1));
        point.setLon(cursor.getDouble(2));
        point.setSended(Boolean.valueOf(cursor.getString(3)));
        return point;
    }
}